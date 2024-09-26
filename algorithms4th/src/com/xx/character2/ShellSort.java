package com.xx.character2;

import java.util.ArrayList;
import java.util.Random;

public class ShellSort extends Example2 {
    public static void main(String[] args) throws Exception {
        ShellSort ins = new ShellSort();
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
        int N = a.length;
        int h = 1;
        while (h < N / 3) h = 3 * h + 1;
        while (h >= 1) {
            for (int i = 0; i < N; i++) {
                for (int j = i; j >= h && less(a[j], a[j - h]); j -= h) {
                    exch(a, j, j - h);
                }
            }
            h = h / 3;
        }
    }
}
