/*
 java 中的排序模板
 */

package com.xx.character2;

public class Example {

    // 排序函数
    public static void sort(Comparable[] a) {
        // todo
    }

    // 判断是否小于
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // 交换待比较中的元素
    private static void exch(Comparable[] a, int i, int j) {
        Comparable tem = a[i];
        a[i] = a[j];
        a[j] = tem;
    }

    // 打印数组
    private static void show(Comparable[] a) {
        for (Comparable ca : a) {
            System.out.print(ca);
        }
        System.out.println();
    }

    /**
     * 判断是否已经有序
     *
     * @param a 入参
     * @return true 有序, false 无序
     */
    public static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++) {
            if (less(a[i], a[i - 1])) {
                return false;
            }
        }
        return true;
    }
}
