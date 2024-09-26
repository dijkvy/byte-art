package com.xx.character2;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Example2 {
    // 排序函数
    public  void sort(Comparable[] a) throws Exception {
        // todo
        throw new Exception("this method should be impl");
    }

    // 判断是否小于
    public   boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // 交换待比较中的元素
    public   void exch(Comparable[] a, int i, int j) {
        Comparable tem = a[i];
        a[i] = a[j];
        a[j] = tem;
    }

    // 打印数组
    public   void show(Comparable[] a) {
        for (Comparable ca : a) {
            System.out.print(ca + ", ");
        }
        System.out.println();
    }

    /**
     * 判断是否已经有序
     *
     * @param a 入参
     * @return true 有序, false 无序
     */
    public  boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++) {
            if (less(a[i], a[i - 1])) {
                return false;
            }
        }
        return true;
    }
}
