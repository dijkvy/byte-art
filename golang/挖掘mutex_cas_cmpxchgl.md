# CMPXCHGL 指令与 cas/mutex 
> mutex 性能一定会很差吗？
> cas 和 mutex 有什么关系？

深度挖掘 mutex.Lock 

### 最常用的 mutex 是怎么实现的
```golang

package main

import "sync"

func main() {
    mu := sync.Mutex{}
    mu.Lock() 
    defer mu.Unlock()
    
}

```

其中 `mu.Lock()` 代码如果成功获取到锁， 直接返回, 如果没有获取到🔐, 则进入自旋。其中获取锁的代码
```golang
// CompareAndSwapInt32 executes the compare-and-swap operation for an int32 value.
// CompareAndSwapInt32对int32值执行**比较**和**交换**操作。
func CompareAndSwapInt32(addr *int32, old, new int32) (swapped bool)
```
所以 `CompareAndSwapInt32` 如函数名一样, 进行比较和交换。 具体是怎么实现的呢？代码中就没有具体的实现了， 他的实现和硬件平台有关的

所以需要使用编译工具把代码编译然后提取出汇编代码。 也可以编译之后使用 objdump 工具, 
> 你怎么知道是过滤 `CMPXCHGL` 指令的呢？ 。。。。 其实编译工具也不是万能的, 就像 `unsafe.Pointer` 具体实现的汇编代码就没有办法提取出来了。。。
```shell script
go tool compile -S main.go | grep -C 5 CMPXCHGL > cas_cmp.s
```

```asm
	0x0043 00067 (<unknown line number>)	NOP
	0x0043 00067 (main.go:6)	MOVQ	AX, CX
	0x0046 00070 ($GOROOT/src/sync/mutex.go:74)	XORL	AX, AX
	0x0048 00072 ($GOROOT/src/sync/mutex.go:74)	MOVL	$1, DX
	0x004d 00077 ($GOROOT/src/sync/mutex.go:74)	LOCK
	`0x004e 00078 ($GOROOT/src/sync/mutex.go:74)	CMPXCHGL	DX, (CX)`
	0x0051 00081 ($GOROOT/src/sync/mutex.go:74)	SETEQ	AL
	0x0054 00084 ($GOROOT/src/sync/mutex.go:74)	TESTB	AL, AL
	0x0056 00086 ($GOROOT/src/sync/mutex.go:74)	JEQ	134
	0x0058 00088 (main.go:9)	LEAQ	sync.(*Mutex).Unlock·f(SB), AX
	0x005f 00095 (main.go:9)	MOVQ	AX, ""..autotmp_7+40(SP)

```

重点关注第六行的汇编代码, `CMPXCHGL	DX, (CX)`  也可搜一下 [itel CMPXCHG](https://www.intel.com/content/www/us/en/search.html?ws=text#q=cmpxchg&t=All)

### CMPXCHGL 指令解析
> 这个指令了一个操作数: EAX。 指令比较 EAX 的值与 DX 的值， 如果相等, 将 CX 的值赋值给 EAX, 并且将标志寄存器 ZF 的标志位设置为 1。 
> 如果不相等, 将 DX 赋值给 EAX, 并且将标志寄存器ZF位置设置为0。
[标志寄存器](https://baike.baidu.com/item/%E6%A0%87%E5%BF%97%E5%AF%84%E5%AD%98%E5%99%A8) OF 位置 = 1, 表示溢出； ZF 为零标志(不懂硬件设计); 

所以这个指令的大白话就是: 
```python
如果 DX == AX:
    AX = (CX)
    ZF = 1
否则:   
    AX = DX
    ZF = 0 
```
**但是这是一条汇编指令, 而不是多条汇编指令**.. 这个指令的的设计就是用来交换并且比较两个数, 并且需要在同一个指令中完成



如果没有抢到🔐的 goroutine 就会进入自旋的状态



### 需要注意

golang 的 mutex 不是可重入的。 如果在同一个 goroutine 中获取同一把🔐将会导致死锁的情况

>  * 互斥条件：一个资源每次只能被一个进程使用， 即在一段时间内某资源仅为一个进程所占有。 如果此时有其他的进程请求该资源， 则请求的进程只能等待。
>  * 请求与保持条件: 进程已经保持了至少一个资源， 但有提出了新的资源的请求， 该资源已被其他的进程锁占有， 此时请求进程被阻塞， 但是对自己已经或则的资源保持不释放。
>  * 不可剥夺条件: 进程所获得的的资源在未使用完毕之前， 不能被其他的进程强行夺走， 即只能有获得该资源的进程来自己释放
>  * 循环等待: 若干进程间形成收尾向量的循环等待资源的关系。

### 参考
[intel cmpxchg instruction](http://heather.cs.ucdavis.edu/~matloff/50/PLN/lock.pdf)