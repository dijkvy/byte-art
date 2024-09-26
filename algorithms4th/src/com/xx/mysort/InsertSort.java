package com.xx.mysort;

import java.util.ArrayList;
import java.util.Arrays;

public class InsertSort {
    public static void main(String[] args) {

        ArrayList<Integer> array = new ArrayList<>();
        array.add(233);
        array.add(43);
        array.add(3);
        array.add(56);
        System.out.println(array.toString());
        insertSort(array);
        System.out.println(array.toString());
    }

    /**
     * 插入排序算法: 顺序地将待排序的数据元素按照关键字值的大小插入
     * 到已经有序的集合的合适位置。子集合的数据元素个数从只有一个数据元素开始， 逐次
     * 增大， 当子集合和集合大小相同时， 排序完毕
     *
     * @param array b
     */
    public static void insertSort(ArrayList<Integer> array) {
        int len = array.size();
        for (int i = 0; i < len - 1; i++) {
            int temp = array.get(i + 1);
            int j = i;
            while (j > -1 && temp < array.get(j)) {
                array.set(j + 1, array.get(j));
                j--;
            }
            array.set(j + 1, temp);

        }
    }
}
