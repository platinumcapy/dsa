package com.github.platinumcapy;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;
    private int modCount;

    private class Node {
        Item item;
        Node next;

        public Node() {}

        public Node(Item item) {
            this.item = item;
        }

        public Node(Item item, Node next) {
            this.item = item;
            this.next = next;
        }
    }

    public LinkedList() {
        first = last = null;
        size = 0;
        modCount = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        first = new Node(item, first);
        if (last == null)
            last = first;
        size++;
        modCount++;
    }

    public void addLast(Item item) {
        if (last == null) addFirst(item);
        else {
            last.next = new Node(item);
            last = last.next;
            size++;
            modCount++;
        }
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("List is empty");
        Item item = first.item;
        first = first.next;
        if (first == null)
            last = null;
        size--;
        modCount++;
        return item;
    }

    public Item peekFirst() {
        if (isEmpty())
            throw new NoSuchElementException("List is empty");
        return first.item;
    }

    public Item peekLast() {
        if (isEmpty())
            throw new NoSuchElementException("List is empty");
        return last.item;
    }

    public void add(int index, Item item) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("index " + index + " is out of bounds for list of size " + size);
        if (index == 0) addFirst(item);
        else if (index == size) addLast(item);
        else {
            Node pre = getNode(index - 1);
            pre.next = new Node(item, pre.next);
            size++;
            modCount++;
        }
    }

    public Item remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index " + index + " is out of bounds for list of size " + size);
        if (index == 0) return removeFirst();
        else {
            Node pre = getNode(index - 1);
            Item item = pre.next.item;
            pre.next = pre.next.next;
            size--;
            modCount++;
            return item;
        }
    }

    public Item get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index " + index + " is out of bounds for list of size " + size);
        return getNode(index).item;
    }

    private Node getNode(int index) {
        assert index >= 0 && index < size;
        Node cur = first;
        while (index-- > 0) {
            cur = cur.next;
        }
        return cur;
    }

    @Override
    public Iterator<Item> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<Item> {
        private final int expectedModCount;
        private Node cur;

        public LinkedListIterator() {
            expectedModCount = modCount;
            cur = first;
        }

        @Override
        public boolean hasNext() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException("Linked list was modified");
            return cur != null;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("Iterator exhausted");
            Item item = cur.item;
            cur = cur.next;
            return item;
        }
    }
}
