package com.xx.character1;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Stack<Item> implements Iterable<Item> {
    public static void main(String[] args) {
        Stack<Integer> is = new Stack<>();
        is.push(23);
        is.push(3);
        is.push(1);
        for (Integer i : is) {
            System.out.println(i.toString());
        }
    }

    private class Node {
        Item item;
        Node next;
    }

    private Node first;
    private int N;

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return this.N;
    }

    public void push(Item item) {
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        this.N++;
    }

    public Item pop() {
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

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
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
