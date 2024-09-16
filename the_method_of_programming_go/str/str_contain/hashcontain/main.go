// 使用哈希的思想来构造

package main

import "github.com/sirupsen/logrus"

func stringContain(a, b string) (ans bool) {
	ans = true
	var hash int64
	// 构造长字符串的哈希值
	for _, v := range a {
		hash |= int64(1 << (v - 'A'))
	}

	// 使用位运算来判断 b 的字母是否在 a 中
	for _, v := range b {
		h := int64(1 << (v - 'A'))
		if (h & hash) != h {
			ans = false
			break
		}
	}

	return ans
}

func main() {
	var a = "ABBBBCD"
	var b = "ABBC"
	logrus.Info(a, " ", b, " ", stringContain(a, b))
	b = "BCE"
	logrus.Info(a, " ", b, " ", stringContain(a, b))
}
