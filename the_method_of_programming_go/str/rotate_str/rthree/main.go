// 一定要看这里的解法: https://github.com/julycoding/The-Art-Of-Programming-By-July/blob/master/ebook/zh/01.01.md

package main

import "github.com/sirupsen/logrus"

// 字符串翻转
// buff 被旋转的字符串
// left 左边第一个字符串的下标
// right 右边第一个字符串的下标
func reverseString(buff []byte, left, right int) {
	for left < right {
		buff[left], buff[right] = buff[right], buff[left]
		left++
		right--
	}
}

func LeftRotateString(buff []byte, m int) {
	m = m % len(buff)
	reverseString(buff, 0, m-1)         // 左边
	reverseString(buff, m, len(buff)-1) // 右边
	reverseString(buff, 0, len(buff)-1) // 整体再翻转一次

}
func main() {
	var buff = []byte("abcdefghijklmn")
	LeftRotateString(buff, len(buff)+3)
	logrus.Info(string(buff))
}
