package com.xx.character2;

import java.util.ArrayList;
import java.util.Random;

public class InsertSort extends Example2 {

    public static void main(String[] args) throws Exception {
        InsertSort ins = new InsertSort();
        ArrayList<Integer> in = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            in.add(new Random().nextInt(100));
        }
        System.out.println(in);
        Integer[] objects = new Integer[10];
        in.toArray(objects);
        // 排序
        ins.sort(objects);
        ins.show(objects);
        System.out.println(ins.isSorted(objects));
    }

    @Override
    public void sort(Comparable[] a) throws Exception {
        // 插入排序算法的实现
        int n = a.length;
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0 && less(a[j], a[j - 1]); j--) {
                // 调整位置， 插入
                exch(a, j, j - 1);
            }
        }
    }
}
