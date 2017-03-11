package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 *
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List {

    private int[] array;

    public MyArrayList() {
        array = new int[10];
        size = 0;
    }

    public MyArrayList(int capacity) {
        if (capacity >= 0) {
            array = new int[capacity];
        } else {
            array = new int[10];
        }
        size = 0;
    }

    @Override
    public void add(int item) {
        if (array.length != size) {
            array[size] = item;
            size++;
        } else {
            int capacity = array.length * 2 + 1;
            int[] newArray = new int[capacity];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
            array[size] = item;
            size++;
        }
    }

    @Override
    public int remove(int idx) throws NoSuchElementException {
        if (idx < 0 || idx >= size) {
            throw new NoSuchElementException();
        } else {
            int removed = array[idx];
            System.arraycopy(array, idx + 1, array, idx, array.length - 1 - idx);
            size--;
            return removed;
        }
    }

    @Override
    public int get(int idx) throws NoSuchElementException {
        if (idx < 0 || idx >= size) {
            throw new NoSuchElementException();
        } else {
            return array[idx];
        }
    }
}
