import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation. Given a composite systems comprised of randomly distributed
 * insulating and metallic materials: what fraction of the materials need to be
 * metallic so that the composite system is an electrical conductor? Given a
 * porous landscape with water on the surface (or oil below), under what
 * conditions will the water be able to drain through to the bottom (or the oil
 * to gush through to the surface)? Scientists have defined an abstract process
 * known as percolation to model such situations.
 *
 * @see {@link http://coursera.cs.princeton.edu/algs4/assignments/percolation.html}
 * @author Mincong Huang
 */
public class Percolation {

    private WeightedQuickUnionUF weightedQU;
    private boolean isOpen[];
    private int indexTop;
    private int indexBottom;
    private int N;

    /**
     * Create n-by-n grid, with all sites blocked
     *
     * @param n length and width of the grid
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Value n must be greater than 0.");
        }
        N = n;
        indexTop = 0;
        indexBottom = n * n + 1;
        weightedQU = new WeightedQuickUnionUF(indexOf(n, n) + 2);
        isOpen = new boolean[indexOf(n, n) + 2];
        isOpen[indexTop] = true;
        isOpen[indexBottom] = true;
    }

    /**
     * Convert a 2D coordinate to 1D. The indexes are first converted from
     * base-1 to base-0, then converted from 2D to 1D.
     *
     * @param row base-1 index of row
     * @param col base-1 index of column
     */
    private int indexOf(int row, int col) {
        int row0 = row + 1;
        int col0 = col + 1;
        return (row0 - 1) * (N + 1) + col0;
    }

    /**
     * Open site (row, col) if it is not open already
     *
     * @param row base-1 index of row
     * @param col base-1 index of column
     */
    public void open(int row, int col) {

        checkBounds(row, 1, N);
        checkBounds(col, 1, N);

        int currIndex = indexOf(row, col);
        isOpen[currIndex] = true;

        if (row == 1) {
            weightedQU.union(currIndex, indexTop);              // Top
        }
        if (row == N) {
            weightedQU.union(currIndex, indexBottom);           // Bottom
        }
        if (row > 1 && isOpen(row - 1, col)) {
            weightedQU.union(currIndex, indexOf(row - 1, col));  // North
        }
        if (row < N && isOpen(row + 1, col)) {
            weightedQU.union(currIndex, indexOf(row + 1, col));  // South
        }
        if (col > 1 && isOpen(row, col - 1)) {
            weightedQU.union(currIndex, indexOf(row, col - 1));  // West
        }
        if (col < N && isOpen(row, col + 1)) {
            weightedQU.union(currIndex, indexOf(row, col + 1));  // East
        }
    }

    /**
     * Is site (row, col) open?
     *
     * @param row base-1 index of row
     * @param col base-1 index of column
     */
    public boolean isOpen(int row, int col) {
        checkBounds(row, 1, N);
        checkBounds(col, 1, N);
        return isOpen[indexOf(row, col)];
    }

    /**
     * Is site (row, col) full?
     *
     * @param row base-1 index of row
     * @param col base-1 index of column
     */
    public boolean isFull(int row, int col) {
        checkBounds(row, 1, N);
        checkBounds(col, 1, N);
        return weightedQU.connected(indexTop, indexOf(row, col));
    }

    /**
     * Does the system percolate?
     */
    public boolean percolates() {
        return weightedQU.connected(indexTop, indexBottom);
    }

    private void checkBounds(int val, int min, int max) {
        if (val < min || val > max)
            new IndexOutOfBoundsException(
                    "Value should be between " + min + " and " + max);
    }

    public static void main(String[] args) {
        StdOut.println("Please run PercolationStats instead.");
    }
}
