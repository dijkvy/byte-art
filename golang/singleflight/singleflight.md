# golang 单飞模式
> 聚合相同工作的一种优化思路

组织:

1. 背景
2. 为什么要使用单飞模式
3. 什么场景下使用单飞模式
4. 如何实现一个单飞模式
5. 总结

## 背景
在使用缓存的场景中，会遇到这样一种情况，一个较热门的 key 在缓存中过期了，比较压抑的是，此时有较多客户端同时发起了对这个 key 的访问。是的，这是个经典的缓存穿透的问题。面对这种场景，较为常见的做法是， 采用先来先服务的原则，限制客户端回源数据库或者其他数据源，这种做法毋庸置疑减轻了数据库的压力，但是这种做法对需要获取数据的客户端真的好吗？

处理逻辑图可能是这样的: // todo 

其实仔细想想，多个客户端同时请求相同的key，只有一个客户端有返回，其他的客户端都没有能获取到正确的数据，这个设计对数据库是好的，但是还有没有其他的方案既能同时保护数据库又能使得不同的客户端对同一个key的访问能够获取到有效数据呢？

## 为什么要使用单飞模式

基于前述的背景信息，我们可以知道，我们常用的方案解决了大量的客户端同时访问同一个 key 的时候，对客户端做访问数量上的限制是保护了数据库，但是对于客户端了说，大多数是获取不到有效的数据的。

常用的解决方案有:
1. 限制客户端对数据库同时访问的数量，只放行一个客户端回源数据库，将获取到的数据保存到缓存
2. 对于大多数的请求客户端返回没有获取到数据
3. 没有获取到数据的客户端重新对服务器发起一个请求，直到获取到数据为止

这个过程的问题出现在哪了？
对于没有获取到访问数据库权限的客户端直接返回，然后重新发起对数据的请求。

为了方便描述，做了如下的定义:

**A 类客户端: 有权限回源数据库的客户端**

**B 类客户端: 没有权限回源数据库的客户端**

其实对于 B 类客户端，在没有获取到对数据库的访问权限之后，是不是可以等待 A 类客户端的返回值呢？
做到了这一步之后，然后就没有第 3 步了。

最后解决方案抽象成了:

1. 限制客户端对数据库同时访问的数量，只放行A 类客户端回源数据库，将获取到的数据保存到缓存
2. B类客户端共享A类客户端请求的数据，然后返回

做到这里，B 类客户端再次对服务器发起请求的数量明显减少了，并且接口请求的成功率明显提高了

小结: 使用单飞模式主要是保护数据库和减少客户端请求的次数

## 什么场景下使用单飞模式

在 *为什么要使用单飞模式* 使用了一个比较适合使用单飞模式解决问题的场景，但是，到底单飞模式可以适用于哪些场景呢？ 还是以上述的为例子。

``` text

1. 限制客户端对数据库同时访问的数量，只放行A 类客户端回源数据库，将获取到的数据保存到缓存
2. B类客户端共享A类客户端请求的数据，然后返回
```

步骤 1 主要是多个客户端同时对于同一个资源的访问，为了减少资源提供方的压力，只允许一个客户端的回源。

步骤 2 成功回源的客户端和其他客户端共享获取到的数据

总结: A 类客户端相当于做了一个苦力的活，然后A类客户端与B类客户端共享数据的场景是比较适合使用单飞模式的。

场景比如: CDN 资源回源主站，redis 缓存穿透(都是多个客户端对同一个资源的同时访问的场景)等等


## 如何实现一个单飞模式

实现一个单飞模式的底层需要注意的点: 
1. 对于 A 类客户端: 需要去完成对数据的访问，再请求处理完毕之后，需要通知 B 类客户端数据已经获取完毕

2. 对于B 类客户端: 在没有获取到工作的权限之后则需要等待 A 类客户端返回的数据

3. 对于已经不需要的任务，底层需要做自动释放的处理

> 以 go 语言为 🌰

```go
package singleflight

import (
	"fmt"
	"sync"
	"sync/atomic"
)

type Result struct {
	Value interface{}
	Err   error
}

type Group struct {
	mu     sync.Mutex       
	single map[string]*call 
}

func NewGroup() *Group {
	return &Group{}
}

type call struct {
	result interface{}   
	err    error        
	done   chan struct{} 
	refJob int32        
}

func (c *Group) DoChan(key string, execute func() (interface{}, error)) <-chan Result {
	r := make(chan Result)
	c.mu.Lock()
	defer c.mu.Unlock()
	var ca *call
	var ok bool
	if ca, ok = c.single[key]; !ok {
		ca = &call{done: make(chan struct{})}
		// if single is nil
		if c.single == nil {
			c.single = make(map[string]*call)
		}
		c.single[key] = ca
		// 执行工作任务
		go func() {
			defer func() {
				if err := recover(); err != nil {
					fmt.Errorf("execute panic:%s", err)
				}
			}()
			ca.result, ca.err = execute()
            // 请求已经完成, 通知 B类客户端
			ca.done <- struct{}{}
			close(ca.done)
		}()
	}
	// add job ref
	atomic.AddInt32(&ca.refJob, 1)

	go func() {
        // 等待 A 类客户端的数据
		<-ca.done
		r <- Result{Err: ca.err, Value: ca.result} // return job result
		close(r)
		if atomic.AddInt32(&ca.refJob, -1) == 0 { // 自定释放任务
			c.deleteJob(key, ca)
		}
	}()

    // 返回一个只读的 channel 
	return r
}

func (c *Group) DoCall(key string, execute func() (interface{}, error)) Result {
	r := c.DoChan(key, execute)
	return <-r
}

func (c *Group) deleteJob(key string, ca *call) {
	c.mu.Lock()
	if atomic.LoadInt32(&ca.refJob) == 0 {
		delete(c.single, key)
	}
	c.mu.Unlock()
}

```
至此，一个简单的单飞模式就可用了。

## 总结 

总的来说，单飞模式在同时处理多个客户端的相同工作方面上有一定上的优势。其原理相对简单，由于 go 语言的优势，在实现这一块的逻辑业务代码相对不复杂，性能代价也是比较小的。


one more,
对于不同的方案在性能和效率方面上的影响
