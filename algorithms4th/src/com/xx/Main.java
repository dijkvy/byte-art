package com.xx;

import edu.princeton.cs.algs4.StdOut;

public class Main {
    public static int gcd(int m, int n) {
        int rem;
        while (n > 0) {
            rem = m % n;
            m = n;
            n = rem;
        }
        return m;
    }

    public static void main(String[] args) {
        System.out.println("study algorithms 4th code");
        StdOut.println(StdOut.class.toString());
        System.out.println(System.class.toString()); // 输出类名
        System.out.println(gcd(24, 4));
        System.out.println(gcd(24, 1));
        System.out.println(gcd(24, 2));
        System.out.println(gcd(24, 3));
        System.out.println(Main.isMain);

    }

    final static int a = 1;
    static final int b = 2;
    final static public boolean isMain = true && ((a ^ b ) == 0) ;
}
