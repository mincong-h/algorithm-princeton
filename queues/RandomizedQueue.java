import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Mincong Huang
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] array;
    private int lastIndex;

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue() {
        // Type Item is only known at runtime.
        @SuppressWarnings("unchecked")
        Item[] a = (Item[]) new Object[1];
        array = a;
        lastIndex = -1;
    }

    /**
     * Is the queue empty?
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Return the number of items on the queue
     */
    public int size() {
        return lastIndex + 1;
    }

    /**
     * Add the item
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException("Element e cannot be null.");
        }
        // array is full
        if (lastIndex + 1 == array.length) {
            resize(array.length * 2);
        }
        array[++lastIndex] = item;
    }

    /**
     * Remove and return a random item
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("RandomizedQueue is empty.");
        }
        int i = StdRandom.uniform(lastIndex + 1);
        Item removed = array[i];
        array[i] = array[lastIndex];
        array[lastIndex--] = null;
        // resize array if it is only 25% full
        if (size() > 0 && size() == array.length / 4) {
            resize(array.length / 2);
        }
        return removed;
    }

    /**
     * Return (but do not remove) a random item
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("RandomizedQueue is empty.");
        }
        Item sample = null;
        while (sample == null) {
            sample = array[StdRandom.uniform(lastIndex + 1)];
        }
        return sample;
    }

    private void resize(int newCapacity) {
        @SuppressWarnings("unchecked")
        Item[] newArray = (Item[]) new Object[newCapacity];
        int i = 0, j = 0;
        while (i <= lastIndex) {
            newArray[j++] = array[i++];
        }
        array = newArray;
        lastIndex = j - 1;
    }

    /**
     * Return an independent iterator over items in random order
     */
    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }

    private class RandomizedIterator implements Iterator<Item> {

        private Item[] copiedArray;
        private int copiedLastIndex;

        RandomizedIterator() {
            @SuppressWarnings("unchecked")
            Item[] a = (Item[]) new Object[lastIndex + 1];
            for (int i = 0; i <= lastIndex; i++) {
                a[i] = array[i];
            }
            copiedArray = a;
            copiedLastIndex = lastIndex;
        }

        @Override
        public boolean hasNext() {
            return copiedLastIndex >= 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more item.");
            }
            int i = StdRandom.uniform(copiedLastIndex + 1);
            Item item = copiedArray[i];
            copiedArray[i] = copiedArray[copiedLastIndex];
            copiedArray[copiedLastIndex--] = null;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove unsupported.");
        }
    }

    /**
     * Serialization of the queue.
     *
     * TODO remove this method before your submission.
     */
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i <= lastIndex; i++) {
            result += "," + array[i];
        }
        if (!result.isEmpty()) {
            result = result.substring(1);
        }
        return "[" + result + "]";
    }

    /**
     * Unit testing.
     *
     * TODO remove these tests before your submission, otherwise submission will
     * fail due to the usage of public method {@code toString()}.
     */
    public static void main(String[] args) {

        StdOut.println("Tests start.");

        // Test 1: public opeations
        RandomizedQueue<Integer> q1 = new RandomizedQueue<>();
        StdOut.println("Test 1A passed? " + q1.isEmpty());
        StdOut.println("Test 1B passed? " + q1.toString().equals("[]"));
        q1.enqueue(1);
        q1.enqueue(2);
        StdOut.println("Test 1C passed? " + q1.toString().equals("[1,2]"));
        StdOut.println("Test 1D passed? " + (q1.size() == 2));
        int test1E = q1.iterator().next();
        StdOut.println("Test 1E passed? " + (test1E == 1 || test1E == 2));
        q1.enqueue(3);
        q1.enqueue(4);
        StdOut.println("Test 1F passed? " + q1.toString().equals("[1,2,3,4]"));
        q1.dequeue();
        String test1G = q1.toString();
        StdOut.println("Test 1G passed? "
                 + (test1G.equals("[4,2,3]")
                 || test1G.equals("[1,4,3]")
                 || test1G.equals("[1,2,4]")
                 || test1G.equals("[1,2,3]")));
        q1.dequeue();
        q1.dequeue();
        // Queue should be resized when 25% full: the size will be reduced by
        // 50%. However, the unused elements in the new array are not visible
        // because they're excluded by the {@code toString()} method.
        String test1H = q1.toString();
        StdOut.println("Test 1H passed? "
                 + (test1H.equals("[1]")
                 || test1H.equals("[2]")
                 || test1H.equals("[3]")
                 || test1H.equals("[4]")));
        q1.dequeue();
        StdOut.println("Test 1I passed? " + q1.toString().equals("[]"));
        StdOut.println("Test 1J passed? " + q1.isEmpty());
        StdOut.println("Test 1K passed? " + !q1.iterator().hasNext());
        StdOut.println("Test 1L passed? " + (q1.iterator() != q1.iterator()));
        q1.enqueue(1);
        StdOut.println("Test 1M passed? " + q1.toString().equals("[1]"));
        q1.enqueue(2);
        StdOut.println("Test 1N passed? " + q1.toString().equals("[1,2]"));

        // Test 2: exceptions
        RandomizedQueue<Integer> q2 = new RandomizedQueue<>();
        try {
            q2.dequeue();
            StdOut.println("Test 2A passed? " + false);
        } catch (Exception e) {
            boolean result = e instanceof NoSuchElementException;
            StdOut.println("Test 2A passed? " + result);
        }
        try {
            q2.sample();
            StdOut.println("Test 2B passed? " + false); 
        } catch (Exception e) {
            boolean result = e instanceof NoSuchElementException;
            StdOut.println("Test 2B passed? " + result);
        }
        try {
            q2.enqueue(null);
            StdOut.println("Test 2C passed? " + false); 
        } catch (Exception e) {
            boolean result = e instanceof NullPointerException;
            StdOut.println("Test 2C passed? " + result);
        }
        try {
            q2.iterator().remove();
            StdOut.println("Test 2D passed? " + false); 
        } catch (Exception e) {
            boolean result = e instanceof UnsupportedOperationException;
            StdOut.println("Test 2D passed? " + result);
        }
        try {
            q2.iterator().next();
            StdOut.println("Test 2E passed? " + false);
        } catch (Exception e) {
            boolean result = e instanceof NoSuchElementException;
            StdOut.println("Test 2E passed? " + result);
        }

        // Test 3: types
        RandomizedQueue<String> q3A = new RandomizedQueue<>();
        q3A.enqueue("Hello Algorithm");
        StdOut.println("Test 3A passed? " + true);
        RandomizedQueue<Double> q3B = new RandomizedQueue<>();
        q3B.enqueue(3.1415926);
        StdOut.println("Test 3B passed? " + true);

        StdOut.println("Tests finished.");
    }
}
