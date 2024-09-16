package main

import "github.com/sirupsen/logrus"

func stringContain(a, b string) bool {
	// 如果 len(b) > len(a) 交换
	if len(b) > len(a) {
		a, b = b, a
	}
	var ans bool = true

	// 暴力循环
	for i := 0; i < len(b); i++ {
		var j = 0
		for j = 0; j < len(a) && a[j] != b[i]; j++ {

		}
		// 没有找到对应的值
		if len(a) <= j {
			ans = false
			break
		}
	}
	return ans
}

func main() {
	var a = "ABCD"
	var b = "ABC"
	logrus.Info(a, " ", b, " ", stringContain(a, b))
	b = "BCE"
	logrus.Info(a, " ", b, " ", stringContain(a, b))
}
