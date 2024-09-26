package com.xx.character2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SelectionSort extends Example2 {

    public static void main(String[] args) {
        SelectionSort ins = new SelectionSort();
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

    /**
     * @param a 传入参数
     */
    @Override
    public void sort(Comparable[] a) {
        // 选择排序算法, 从无序队列中， 选择一个最小值插入到有序队列的后面
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int min = i;
            for (int j = i + 1; j < N; j++) {
                if (less(a[j], a[min])) {
                    min = j;
                }
            }
            exch(a, i, min);
        }
    }

}
