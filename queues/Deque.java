import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Mincong Huang
 */
public class Deque<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private int size;

    private class Node {

        Item item;
        Node prev;
        Node next;

        Node(Item item) {
            this.item = item;
        }
    }

    /**
     * Construct an empty deque
     */
    public Deque() {
        head = new Node(null);  // dummy head
        tail = new Node(null);  // dummy tail
        head.next = tail;
        tail.prev = head;
    }

    /**
     * Is the deque empty?
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Return the number of items on the deque
     */
    public int size() {
        return size;
    }

    /**
     * Add the item to the front
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException("Element e cannot be null.");
        }
        Node node = new Node(item);
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
        size++;
    }

    /**
     * Add the item to the end
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException("Element e connot be null.");
        }
        Node node = new Node(item);
        node.next = tail;
        node.prev = tail.prev;
        tail.prev.next = node;
        tail.prev = node;
        size++;
    }

    /**
     * Remove and return the item from the front
     */
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty.");
        }
        Node node = head.next;
        head.next = node.next;
        head.next.prev = head;
        size--;
        return node.item;
    }

    /**
     * Remove and return the item from the end
     */
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("Deque is empty.");
        }
        Node node = tail.prev;
        tail.prev = node.prev;
        tail.prev.next = tail;
        size--;
        return node.item;
    }

    /**
     * Return an iterator over items in order from front to end
     */
    @Override
    public Iterator<Item> iterator() {
        return new HeadFirstIterator();
    }

    private class HeadFirstIterator implements Iterator<Item> {

        private Node curr = head;

        @Override
        public boolean hasNext() {
            return curr.next != tail;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more item.");
            }
            curr = curr.next;
            return curr.item;
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
        for (Item item : this) {
            result += "," + item;
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

        // Test 1: public operations
        Deque<Integer> d1 = new Deque<>();
        StdOut.println("Test 1A passed? " + d1.isEmpty());
        StdOut.println("Test 1B passed? " + d1.toString().equals("[]"));
        d1.addLast(1);
        d1.addLast(2);
        StdOut.println("Test 1C passed? " + d1.toString().equals("[1,2]"));
        StdOut.println("Test 1D passed? " + (d1.size() == 2));
        StdOut.println("Test 1E passed? " + (d1.iterator().next() == 1));
        d1.addFirst(0);
        StdOut.println("Test 1F passed? " + d1.toString().equals("[0,1,2]"));
        d1.removeLast();
        StdOut.println("Test 1G passed? " + d1.toString().equals("[0,1]"));
        d1.removeFirst();
        StdOut.println("Test 1H passed? " + d1.toString().equals("[1]"));
        d1.removeFirst();
        StdOut.println("Test 1I passed? " + d1.toString().equals("[]"));
        StdOut.println("Test 1J passed? " + d1.isEmpty());
        StdOut.println("Test 1H passed? " + !d1.iterator().hasNext());

        // Test 2: exceptions
        Deque<Integer> d2 = new Deque<>();
        try {
            d2.removeFirst();
            StdOut.println("Test 2A passed? " + false);
        } catch (Exception e) {
            boolean result = e instanceof NoSuchElementException;
            StdOut.println("Test 2A passed? " + result);
        }
        try {
            d2.removeLast();
            StdOut.println("Test 2B passed? " + false);
        } catch (Exception e) {
            boolean result = e instanceof NoSuchElementException;
            StdOut.println("Test 2B passed? " + result);
        }
        try {
            d2.addFirst(null);
            StdOut.println("Test 2C passed? " + false);
        } catch (Exception e) {
            boolean result = e instanceof NullPointerException;
            StdOut.println("Test 2C passed? " + result); 
        }
        try {
            d2.addLast(null);
            StdOut.println("Test 2D passed? " + false); 
        } catch (Exception e) {
            boolean result = e instanceof NullPointerException;
            StdOut.println("Test 2D passed? " + result);
        }
        try {
            d2.iterator().remove();
            StdOut.println("Test 2F passed? " + false);
        } catch (Exception e) {
            boolean result = e instanceof UnsupportedOperationException;
            StdOut.println("Test 2F passed? " + result);
        }
        try {
            d2.iterator().next();
            StdOut.println("Test 2G passed? " + false);
        } catch (Exception e) {
            boolean result = e instanceof NoSuchElementException;
            StdOut.println("Test 2G passed? " + result);
        }

        // Test 3: types
        Deque<String> d3a = new Deque<>();
        d3a.addFirst("Hello Algorithm");
        StdOut.println("Test 3A passed? " + true);
        Deque<Double> d3b = new Deque<>();
        d3b.addLast(3.1415926);
        StdOut.println("Test 3B passed? " + true);

        StdOut.println("Tests finished.");
    }
}
