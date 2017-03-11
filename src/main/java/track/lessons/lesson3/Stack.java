package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Queue interface.
 */

public interface Stack {

    public void push(int value);

    public int pop() throws NoSuchElementException;
}
