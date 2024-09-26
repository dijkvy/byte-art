/**
 * 求两个数的最大公约数
 */
package com.xx;

import edu.princeton.cs.algs4.StdOut;

public class GCD {
    /**
     * @param a 输入 数 a
     * @param b 输入 b
     * @return
     * @desc 计算 两个数的最大公约数
     */
    public static int gcd(int a, int b) {
        int temp = 0;
        if (a > b) {
            temp = a;
            a = b;
            b = temp;
        }
        // 如果其中一个为 0，直接返回 0
        if (a == 0 || b == 0) {
            return 0;
        }
        while (b > 0) {
            temp = a % b; // a % b  等价于 a - (a/b)*b
            a = b;
            b = temp;
        }
        return a;
    }

    public static void main(String[] args) {
        StdOut.println(gcd(24, 3));
        StdOut.println(gcd(24, 6));
        StdOut.println(gcd(24, 1));
        StdOut.println(gcd(24, 0));
    }
}
