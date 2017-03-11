package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Stack interface.
 */
public interface Queue {

    public void enqueue(int value);

    public int dequeue() throws NoSuchElementException;
}
