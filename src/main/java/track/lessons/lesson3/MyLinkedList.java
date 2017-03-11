package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * Односвязный список
 */
public class MyLinkedList extends List implements Stack, track.lessons.lesson3.Queue {
    /**
     * private - используется для сокрытия этого класса от других.
     * Класс доступен только изнутри того, где он объявлен
     * <p>
     * static - позволяет использовать Node без создания экземпляра внешнего класса
     */
    private static class Node {
        Node prev;
        Node next;
        int val;

        Node(Node prev, Node next, int val) {
            this.prev = prev;
            this.next = next;
            this.val = val;
        }
    }

    private Node first;
    private Node last;

    @Override
    public void add(int item) {
        if (last != null) {
            last = new Node(last, null, item);
            last.prev.next = last;
            size++;
        } else {
            first = new Node(null, null, item);
            last = first;
            size = 1;
        }
    }

    @Override
    public int remove(int idx) throws NoSuchElementException {
        if (idx < 0 || idx >= size) {
            throw new NoSuchElementException();
        } else {
            Node node;
            int nodeNum = 0;
            for (node = first; idx != nodeNum; node = node.next) {
                nodeNum++;
            }
            if (node.prev != null) {
                node.prev.next = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }
            size--;
            return node.val;
        }
    }

    @Override
    public int get(int idx) throws NoSuchElementException {
        if (idx < 0 || idx >= size) {
            throw new NoSuchElementException();
        } else {
            Node node = first;
            int nodeNum = 0;
            for (; idx != nodeNum; node = node.next) {
                nodeNum++;
            }
            return node.val;
        }
    }

    @Override
    public void push(int value) {
        this.add(value);
    }

    @Override
    public int pop() throws NoSuchElementException {
        if (last == null) {
            throw new NoSuchElementException();
        } else {
            if (last.prev != null) {
                last.prev.next = null;
            } else {
                first = null;
            }
            int removed = last.val;
            last = last.prev;
            size--;
            return removed;
        }
    }


    @Override
    public void enqueue(int value) {
        this.add(value);
    }

    @Override
    public int dequeue() throws NoSuchElementException {
        if (first == null) {
            throw new NoSuchElementException();
        } else {
            if (first.next != null) {
                first.next.prev = null;
            } else {
                last = null;
            }
            int removed = first.val;
            first = first.next;
            size--;
            return removed;
        }
    }
}