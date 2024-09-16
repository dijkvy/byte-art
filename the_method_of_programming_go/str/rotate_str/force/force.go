package main

import (
	"github.com/sirupsen/logrus"
	"time"
)

func init() {
	// 这个是什么
	// hasMonotonic
	time.Since(time.Now())
}

// 移动一个字符
func LeftShiftOne(buff []byte) {
	// 处理特殊情况
	if len(buff) < 2 {
		return
	}

	var tmp = buff[0] // 保存第一个字符的内容
	// 将 n -1 个字符都向前搬移一位
	for i := 1; i < len(buff); i++ {
		buff[i-1] = buff[i]
	}

	// 将 tmp 设置到 最后一个位置
	buff[len(buff)-1] = tmp
}

/**
*   buff: 字符串
*   m:    一定 m 个字符
 */
func LeftRotateString(buff []byte, m int) {
	for m = m % len(buff); m > 0; m-- {
		LeftShiftOne(buff)
	}
}

func main() {
	var buff = []byte("abcdefghijklmn")
	LeftRotateString(buff, len(buff)+3)
	logrus.Info(string(buff))
}
