/**
 * @date:
 */
package com.xx.mycontainer;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Stack<Item> implements Iterable<Item> {

    public static void main(String[] args) {
        Stack<Integer> is = new Stack<>();
        is.push(233);
        is.push(2);
        for (Integer i : is) {
            System.out.println(i);
        }
        System.out.println(is.size());
        System.out.println(is.pop());
        System.out.println(is.size());
        System.out.println("peek:" + is.peek());
        System.out.println(is.pop());
        System.out.println(is.size());
        System.out.println(is.pop());

    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return this.N;
    }

    /**
     * 获取栈顶元素
     *
     * @return
     */
    public Item peek() {
        // 如果栈为空, 返回null
        if (this.isEmpty()) {
            return null;
        }
        // 返回栈顶元素
        return this.first.item;
    }

    /**
     * 添加元素
     *
     * @param item
     */
    public void push(Item item) {
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        this.N++;
    }

    /**
     * 弹栈
     *
     * @return
     */
    public Item pop() {
        if (this.isEmpty()) {
            return null;
        }
        Item item = first.item;
        first = first.next;
        this.N--;
        return item;
    }

    @NotNull
    @Override
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    @Override
    public void forEach(Consumer<? super Item> action) {

    }

    @Override
    public Spliterator<Item> spliterator() {
        return null;
    }

    private Node first; // 头指针
    private int N; // 记录元素的数量

    private class Node {
        Item item; // 数据域
        Node next; // 指针域
    }

    /**
     * 内部实现的迭代器类
     */
    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public Item next() {
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {

        }
    }
}
