package track.lessons.lesson3;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class MyLinkedListTest {

    @Test(expected = NoSuchElementException.class)
    public void emptyList() throws Exception {
        List list = new MyLinkedList();
        Assert.assertTrue(list.size() == 0);
        list.get(0);
    }

    @Test(expected = NoSuchElementException.class)
    public void removeNegative() throws Exception {
        List list = new MyLinkedList();
        list.add(123);
        list.remove(-1);
    }

    @Test
    public void listAdd() throws Exception {
        List list = new MyLinkedList();
        list.add(1);

        Assert.assertTrue(list.size() == 1);
    }

    @Test
    public void listAddRemove() throws Exception {
        List list = new MyLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);

        Assert.assertEquals(3, list.size());

        Assert.assertEquals(1, list.get(0));
        Assert.assertEquals(2, list.get(1));
        Assert.assertEquals(3, list.get(2));

        list.remove(1);
        Assert.assertEquals(3, list.get(1));
        Assert.assertEquals(1, list.get(0));

        list.remove(1);
        list.remove(0);

        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void listRemove() throws Exception {
        List list = new MyLinkedList();
        list.add(1);
        list.remove(0);

        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void stackAddRemove() throws Exception {
        MyLinkedList list = new MyLinkedList();
        list.add(1);
        list.push(2);
        list.add(3);

        Assert.assertEquals(3, list.size());

        Assert.assertEquals(3, list.pop());

        Assert.assertEquals(2, list.get(1));
        Assert.assertEquals(2, list.pop());

        Assert.assertEquals(1, list.pop());

        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void queueAddRemove() throws Exception {
        MyLinkedList list = new MyLinkedList();
        list.add(1);
        list.enqueue(2);
        list.enqueue(3);

        Assert.assertEquals(3, list.size());

        Assert.assertEquals(3, list.pop());

        Assert.assertEquals(1, list.dequeue());
        Assert.assertEquals(2, list.get(0));

        Assert.assertEquals(2, list.dequeue());

        Assert.assertTrue(list.size() == 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void stackPopException() throws Exception {
        MyLinkedList list = new MyLinkedList();
        list.pop();
    }

    @Test(expected = NoSuchElementException.class)
    public void stackQueueException() throws Exception {
        MyLinkedList list = new MyLinkedList();
        list.dequeue();
    }
}