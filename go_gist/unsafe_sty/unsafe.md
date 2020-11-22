# go 语言的指针如何像 c 那样运算
> 通过例子来学习 unsafe 包



**环境信息**

```go
go version go1.15.5  // 不同版本的 api 可能会不一样
```



### unsafe 有什么用？ 真的有这么厉害吗？

1. unsafe 包提供的操作会绕过 go 程序的类型类型安全
2. unsafe 包提供的操作不是可移植的

###### ArbitraryType

```go
type ArbitraryType int
```

> `ArbitraryType` 在这里用作文档的说明， 实际上不是 unsafe 包的一部分。 它可以用来表示任何 `go` 类型。



###### Pointer

```go
type Pointer *ArbitraryType
```

> Pointer 表示任意类型的指针。（提取主干: Pointer是指针）
>
> 有四个特殊的操作适用于 Pointer 类型， 但是不适用于其他的类型：
>
> -任何类型的指针值都可以转换为Pointer。
> -指针可以转换为任何类型的指针值。
> -uintptr可以转换为Pointer。
> -指针可以转换为uintptr。
>
> 基于以上四点， 指针可以允许程序绕过系统的类型检查并读写任意的内存， 因此， 使用使用指针时应该格外地小心。 (Fatal ⚠️🐶)



###### 使用 Pointer 还有模式？

> 以下涉及到的 Pointer 的模式是有效的

**不是使用这些模式的代码可能在现在或者将来无效** (ps: 就是不遵循这些标准可能会使自己写的代码变成一个坑)



怎么知道自己的代码是否遵循这些标准了呢？

```shell
go vet 
```

使用 go vet 命令可以找出不符合 Pointer 模式的代码， 但是 go vet 并不能保证代码有效（结论: 结果还是坑）



###### Pointer 有哪些使用模式呢？

1. 将* T1转换为指向* T2的指针

   > 前提是： T2 不大于 T1(占用的内存空间), 并且和两个享有相同的内存布局。则此转换允许将一种类型的数据重新解释为另一种类型的数据。

   🌰 float64 占用 8 字节大小， uint64 也占用 8 字节大小， `IEEE 754` 标准

   ```go
   func Float64bits(f float64) uint64 { return *(*uint64)(unsafe.Pointer(&f)) }
   ```

    

2. 将Pointer转换为uintptr

   > 将 Pointer 转换为 uintptr 会生成pointer 所指向值得内存的(整数)地址。 这种用法通常就是用来打印输出？
   >
   > **将 uintptr** 转回到 Pointer 通常是无效的。

   划重点:  uintptr 是整数， 而不是引用。 将 Pointer 转换为 uintptr 会创建一个没有指针意义的整数值。 即使 uintptr 保留了某个对象的地址， 垃圾♻️器也不会在对象移动时更新改对象的值， 也不会阻止 uintptr 所指向的对象被♻️掉。 

3. 使用算术运算将 Pointer 转为 uintptr 并返回Pointer

   > 如果 p  所指向的对象已经分配内存， 则可以通过转换为 uintptr 的方式， 添加偏移量的方式转回 Pointer 的方式推进 Pointer
   >
   > ```go
   > p = unsafe.Pointer(uintptr(p) + offset)
   > ```
   >
   > 这种方式常用在访问结构体中的字段或者数组中的元素
   >
   > ```go
   > //等价操作，假定访问的是结构体
   > //f:= unsafe.Pointer(&s.f)
   > 
   > f:= unsafe.Pointer(uinptr(unsafe.Pointer(&s)) + unsafe.Offsetof(s.f))
   > ```
   >
   > 操作对象是数组：
   >
   > ```go
   > // 等价操作
   > // e:= unsafe.Pointer(&x[i])
   > 
   > e:= unsafe.Pointer(uintptr(unsafe.Pointer(&x)) + i *  unsafe.Offsetof(x[0]))
   > 
   > 
   > ```
   >
   > 这两种操作通过指针增加或者减去偏移量都是有效的。 
   >
   > 使用 `&^` 来操作指针也是有用的， 不过这种通常用于内存对齐操作
   >
   > **所有的例子的中， 结构都必须指向原来分配的对象**

   

   和 `C` 不一样的是， 将指针移动到它原始分配空间的最末尾是无效的

   比如：

   ```go
   // end 已经指向了分配的空间之外了
   var s otherType
   end = unsafe.Pointer(uintptr(unsafe.Pointer(&s)) + unsafe.Sizeof(s))
   ```

   在比如像这样的：

   ```go
   //	b := make([]byte, n)
   //	end = unsafe.Pointer(uintptr(unsafe.Pointer(&b[0])) + uintptr(n))
   ```

   `end` 已经指向了所分配的空间之外了

   注意： 两个转换之间必须出现在相同的表达式之中， 并且他们之后只有算术运算

   像这样是无效的：

   ```go
   // 在返回 p 之前， u 之间隔了一个表达式了， 在危险的情况下， 可能已经释放了指向的对象了
   u:=uintptr(p) 
   
   p = unsafe.Pointer(u + offset)
   
   ```

   注意： Pointer 必须是指向已经分配的对象， 所以它不能为 `nil`

   `nil` 是无效的

   ```go
   u:= unsafe.Pointer(nil)
   p:=unsafe.Pointer(uintptr(u) + offset)
   ```

   

4. 调用 `syscall.Syscall` 时将指针转为 uintptr

   `syscall`包的 `Syscall` 函数直接传递 uintptr 类型的参数到操作系统， 然后操作系统可以根据调用的详细信息将其中一些重新解析为指针。 也就是说， 操作系统实现将某些参数从 uintptr 类型隐式转换会指针。 

   如果必须将指针类型的参数转换为 uintptr 用作参数， 转换必须出现在调用表达式本身中：

   ```go
   syscall.Syscall(SYS_READ, uintptr(fd), uintptr(unsafe.Point(p)), uintptr(n))
   ```

   编译器通过安排引用的分配对象（如果有的话）被保留并且直到调用完成（即使来自类型）也不会移动，来处理在汇编中实现的函数的调用的参数列表中转换为uintptr的Pointer单独看来，在调用过程中不再需要该对象。

   **为了使编译器能够识别这种模式，转换必须出现在参数列表中**, 也就是说 uintptr 不能保存在中间变量中

   ```go
   u:=uintptr(unsafe.Pointer(p))
   syscall.Syscall(SYS_READ, uintptr(fd), u, uintptr(n))
   ```

5. 将reflect.Value.Pointer或reflect.Value.UnsafeAddr的结果从uintptr转换为Pointer

   反射包的名为Pointer和UnsafeAddr的Value方法返回类型uintptr而不是 `unsafe.Pointer` 意在防止调用方将结构修改为任意的类型。 但是这就意味着结构是比较脆弱的， 所以要在 调用完之后立即将其转换为 Pointer 在同一个调用表达式中

   ```go
   p := (*int)(unsafe.Pointer(reflect.ValueOf(new(int)).Pointer()))
   ```

   在将 uintptr 转换回 Pointer 结果之前， 不要存储 uintptr 的结构

   ```go
   //  错误示范
   //	u := reflect.ValueOf(new(int)).Pointer()
   //	p := (*int)(unsafe.Pointer(u))
   ```

   

6. 将reflect.SliceHeader或reflect.StringHeader数据字段与指针进行转换

   与前面的情况一样，反射数据结构SliceHeader和StringHeader将字段Data声明为uintptr，以防止调用者将结果更改为任意类型，而无需首先导入“ unsafe”。但是，这意味着SliceHeader和StringHeader仅在解释实际切片或字符串值的内容时才有效。

   ```go
   //	var s string
   //	hdr := (*reflect.StringHeader)(unsafe.Pointer(&s)) // case 1
   //	hdr.Data = uintptr(unsafe.Pointer(p))              // case 6 (this case)
   //	hdr.Len = n
   // 在这种用法中，hdr.Data实际上是在字符串标题中引用基础指针的替代方法，而不是uintptr变量本身。
   ```

   通常，reflect.SliceHeader和reflect.StringHeader只能用作指向实际切片或字符串的* reflect.SliceHeader和* reflect.StringHeader，而不能用作普通结构。

   ```go
   //程序不应声明或分配这些结构类型的变量。
   //
   //
   // var hdr Reflection.StringHeader
   // hdr.Data = uintptr（unsafe.Pointer（p））
   // hdr.Len = n
   // s：= *（* string）（unsafe.Pointer（＆hdr））// p可能已经丢失
   ```

   



### func Sizeof(x ArbitraryType) uintptr

  Sizeof接受任何类型的表达式x并返回假设变量v的字节大小，就好像v是通过var v = x声明的一样。
  该大小不包括x可能引用的任何内存。
  例如，如果x是切片，则Sizeof返回切片描述符的大小，而不是该切片所引用的内存的大小。
Sizeof的返回值是Go常数。



### func Offsetof(x ArbitraryType) uintptr

Offsetof返回x表示的字段的结构内的偏移量，该偏移量的格式必须为structValue.field。 换句话说，它返回结构开始与字段开始之间的字节数。 Offsetof的返回值为Go常数。



### func Alignof(x ArbitraryType) uintptr

alignof接受任何类型的表达式x并返回假设变量v的所需对齐方式，就好像v是通过var v = x声明的一样。 它是最大值m，因此v的地址始终为零mod m。 它与reflect.TypeOf（x）.Align（）返回的值相同。 作为一种特殊情况，如果变量s是结构类型，而f是该结构中的字段，则Alignof（s.f）将返回结构中该类型字段的所需对齐方式。 这种情况与reflect.TypeOf（s.f）.FieldAlign（）返回的值相同。Alignof的返回值是一个Go常量。





### 总结

Pointer的使用模式

1.  可以从 `*T1` 转为 `*T2` 
2. 将Pointer转换为uintptr， 但是 uintptr 指向的对象可能是无效的
3. 使用算术运算将 Pointer 转为 uintptr 并返回Pointer
4. 调用 `syscall.Syscall` 时将指针转为 uintptr， 需要在调用函数中完成转换，不要使用中间变量
5. 将reflect.Value.Pointer或reflect.Value.UnsafeAddr的结果从uintptr转换为Pointer， 需要在同一个表达式中完成



### unsafe 的一些使用

```go
package main

import (
    "fmt"
    "unsafe"
)

func main() {
    // 有相同内存布局的类型转换
    var i64 int64 = 9
    var f64 = *(*float64)(unsafe.Pointer(&i64))
    fmt.Println("pattern 1:", f64)
    var array [16]int
    // 使用偏移量来访问数组
    * (*int)(unsafe.Pointer(uintptr(unsafe.Pointer(&array)) + unsafe.Sizeof(int(0))*2)) = 2006
    fmt.Println("pattern 3:", array)
}

```

