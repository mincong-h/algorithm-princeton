import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * A client program that
 * <ul>
 * <li>takes a command-line integer {@code k};
 * <li>reads in a sequence of {@code N} strings from standard input using
 *     {@code StdIn.readString()};
 * <li>prints out exactly {@code k} of them, uniformly at random.
 * <p>
 * Each item from the sequence can be printed out at most once. You may assume
 * that {@code 0 ≤ k ≤ n}, where {@code n} is the number of string on standard
 * input.
 *
 * @author Mincong Huang
 */
public class Subset {

    public static void main(String[] args) {
        final int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        String[] tokens = StdIn.readAllStrings();
        for (String token : tokens) {
            queue.enqueue(token);
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}
