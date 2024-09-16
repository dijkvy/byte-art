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
