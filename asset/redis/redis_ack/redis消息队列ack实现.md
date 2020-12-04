# 基于 redis 的 list 怎么实现 ack 功能

> 为什么需要实现这个功能❓
>
> 怎么实现这个功能❓
>
> 这个方法有什么缺点❓
>
> 还有什么其他的方法吗❓



## 为什么需要实现这个功能

> 比如一个商城系统, 订单服务和库存服务是分开的, 在抢购模块的实现中使用消息来补偿的方式(通过redis的list)来修改商品的库存。 如果在处理消息的过程中, 库存服务异常重启了, 其中就有可能丢失了一个或者多个有效的消息。 那么对于系统的鲁棒性来说都不是一个较好的实现。



🌰中的消息是怎么丢的？ 还能恢复回来吗？

由于 list 的 pop 是直接发消息丢出来的, 那么这个过程中 redis 就会把数据从服务器中删除, 所以 pop 消息出来的时候, 本质上消息就已经从服务器中删除了。 所以消息是不能够保证消息是否已经被消费过的。



基于系统的可靠性来说, 需要做消息的确认功能的



## 怎么实现这个功能

<span style="color:red">redis 是通过调用命令来实现数据的操作的</span>。 

所以流程是这样的。 

```shell
128.0.0.2:6379>lpop list_key_name;
> value_is_ok;
127.0.0.1:6379> set random_key_name value_is_ok;
> ok
```

需要先把值拿出， 然后再用客户端设置回去, 问题是如果 `value_is_ok` 可以顺利获取回来, 但是在设置值的时候 客户端出错了。。。 嗯, 确实不太对。 那也是会丢数据的啊



总觉得那里出了问题， 但是又说不出来。。。

💡  所以， 需要把这个操作在一个命令中来完成。 故而需要使用上 lua 来实现值的 "备份" 功能。 所以脚本需要完成把值冲 list 中 pop 出来， 然后设置到一个 string 或者 hash 中。



##### 把消息 pop 出队列， 并把消息拷贝一份到 string

``` lua
-- 处理客户端传入的参数 一个是 list key, 一个是 random_str_key 也可以是分布式 id, 反正不重复就好
local list_key = KEYS[1];
local random_str_key = KEYS[2];
local res = {};

local opt_value = redis.call('lpop', list_key);
-- 操作失败
if (not opt_value) then
    res[1] = 2;
    res[2] = random_str_key;
    res[3] = 'nil';
    return res;
end

redis.call('set', random_str_key, opt_value);
res[1] = 1;
res[2] = random_str_key;
res[3] = opt_value;

return res;
```



##### 确认消息消费完成

```lua
-- 这个是实现消息的确认功能, 主要就是删除string
local random_str_key = KEYS[1];
return redis.call('del',random_str_key);
```



##### 消息消费出错， 需要重新投放会消息队列

```lua
local list_key = KEYS[1];
local random_str_key = KEYS[2];
local res = {};

-- 先把消息获取出来
local opt_value = redis.call('get', random_str_key);
-- 删除消息
redis.call('del', random_str_key);
-- 重新返回到 list
redis.call('rpush', list_key, opt_value);

res[1] = 1;
res[2] = random_str_key;
res[3] = opt_value;
return res;
```



所以整个就三个 `scripts`

```lua
local pullScript = "local list_key = KEYS[1]; local random_str_key = KEYS[2]; local res = {}; local opt_value = redis.call('lpop', list_key); if (not opt_value) then res[1] = 2; res[2] = random_str_key; res[3] = 'nil'; return res; end redis.call('set', random_str_key, opt_value); res[1] = 1; res[2] = random_str_key; res[3] = opt_value; return res;"

local ackScript ="local random_str_key = KEYS[1]; return redis.call('del',random_str_key);";



local cancelScript = "local list_key = KEYS[1]; local random_str_key = KEYS[2]; local res = {}; local opt_value = redis.call('get', random_str_key); redis.call('del', random_str_key); redis.call('rpush', list_key, opt_value); res[1] = 1; res[2] = random_str_key; res[3] = opt_value; return res;";


```



## 这个方法有什么缺点

这个方法目前有个潜在的问题， 就是客户端如果崩溃的话， 客户端就没有能力操作消息了， 除非人工介入

还有就是 lpop 方法会有消息不是阻塞读的, 没有办法做到响应式, 所以需要客户端去不断地轮询





## 还有什么其他的方法吗

* 需要做到响应式的话, 需要在消息投递的时候放入 list 的时候插入一个消息的 id, 并且复制一份到 string 里面去, 消费消息之后把对结果删除就好, 取消的话, 把值重新投放到list 里面


求个👍

[示例代码](./main.go) 

