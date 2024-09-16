# 素数相乘法判断解决字符串包含的关系

1. 按照从大到小的思路，用 26 个字母分别代替字符串 a 中的所有字母的乘积，
2. 遍历长字符串 a, 求得 a 中所有字母对应的素数乘积。
3. 遍历短字符串 b, 判断上一步得到的乘积能否被 b 中的字母对应的素数相除。
4. 如果可以被整除，表示字符串 a 包含字符串 b 的当前字母，否则不包含
5. 输出结果

```go
package main

import "github.com/sirupsen/logrus"

// 先排序然后轮询的思路更像是 **贪心**

// 素数相乘法
func stringContain(a, b string) bool {
	var p = [26]int{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 71, 79, 83, 89, 97, 101}
	var f = 1
	for i := 0; i < len(a); i++ {
		var x = int(p[a[i]-'A'])
		if (f % x) > 0 {
			f *= x
		}
	}
	for i := 0; i < len(b); i++ {
		var x = int(p[b[i]-'A'])
		// 如果不整除，表示 当前的字母没有在 a 中出现过
		if (f % x) > 0 {
			return false
		}
	}
	return true
}

func main() {
	var a = "ABBBBCD"
	var b = "ABBC"
	logrus.Info(a, " ", b, " ", stringContain(a, b))
	b = "BCE"
	logrus.Info(a, " ", b, " ", stringContain(a, b))
}


```
