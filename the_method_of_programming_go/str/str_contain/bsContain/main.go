package main

import (
	"github.com/sirupsen/logrus"
	"sort"
)

// 排序后比较
func stringContain(a, b string) (ans bool) {
	if len(b) > len(a) {
		a, b = b, a
	}
	var ta, tb = []byte(a), []byte(b)
	sort.Slice(ta, func(i, j int) bool {
		return ta[i] < ta[j]
	})
	sort.Slice(tb, func(i, j int) bool {
		return tb[i] < tb[j]
	})
	a, b = string(ta), string(tb)

	var i int
	var j int
	for i = 0; i < len(b); i++ {
		for ; j < len(a) && a[j] < b[i]; j++ {

		}
		if j == len(a) {
			return false
		}
		j++
	}
	return i == len(b)
}

func main() {
	var a = "ABBBBCD"
	var b = "ABBC"
	logrus.Info(a, " ", b, " ", stringContain(a, b))
	b = "BCE"
	logrus.Info(a, " ", b, " ", stringContain(a, b))
}
