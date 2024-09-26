package com.xx.mysort;

import java.util.ArrayList;

/**
 * 堆排序
 */
public class HeapSort {

    public static void main(String[] args) {
        ArrayList<Integer> ar = new ArrayList<>();
        ar.add(23);
        ar.add(56);
        ar.add(99);
        ar.add(233);
        HeapSort.adjustHeap(ar, ar.size() / 2);
        System.out.println(ar);
        heapSort(ar);
        System.out.println(ar);
    }

    public static void heapSort(ArrayList<Integer> array) {
        //
        //
        adjustHeap(array, array.size()/2);
        for (int i = array.size() - 1; i > 0; i--) {
            int temp = array.get(i);
            array.set(i, array.get(0));
            array.set(0, temp);
            adjustHeap(array, i - 1);
        }
    }

    /**
     * @param array  要排序的数组
     * @param parent 要调整的堆头部分
     */
    public static void adjustHeap(ArrayList<Integer> array, int parent) {
        int son = parent * 2 + 1;
        if (parent > array.size()) {
            return;
        }
        while (son < array.size()) {
            if (son + 1 < array.size() && array.get(son + 1) > array.get(son)) {
                son = son + 1;
            }
            // 需要需要调整当前节点的父亲节点的值
            if (array.get(son) > array.get(parent)) {
                int temp = array.get(son);
                array.set(son, array.get(parent));
                array.set(parent, temp);
            }

            // 下层父亲节点
            parent = son;
            son = parent * 2 + 1;
        }
        System.out.println(array + " " + parent);
    }
}
