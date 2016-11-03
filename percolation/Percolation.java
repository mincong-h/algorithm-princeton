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
 * @author Mincong Huang
 */
public class Percolation {

    private final WeightedQuickUnionUF normalQU;
    private final WeightedQuickUnionUF backwashQU;
    private final boolean[] isOpen;
    private final int topIndex;
    private final int btmIndex;
    private final int n;

    /**
     * Create n-by-n grid, with all sites blocked
     *
     * @param n length and width of the grid
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0.");
        }
        this.n = n;
        topIndex = 0;
        btmIndex = n * n + 1;
        backwashQU = new WeightedQuickUnionUF(n * n + 2);
        normalQU = new WeightedQuickUnionUF(n * n + 1);  // wihout bottom index
        isOpen = new boolean[n * n + 2];
        isOpen[topIndex] = true;
        isOpen[btmIndex] = true;
    }

    /**
     * Convert a 2D coordinate to 1D.
     *
     * @param row base-1 index of row
     * @param col base-1 index of column
     */
    private int indexOf(int row, int col) {
        // check bounds
        if (row < 1 || row > n) {
            throw new IndexOutOfBoundsException("Row is out of bounds.");
        }
        if (col < 1 || col > n) {
            throw new IndexOutOfBoundsException("Column is out of bounds.");
        }
        return (row - 1) * n + col;
    }

    /**
     * Open site (row, col) if it is not open already
     *
     * @param row base-1 index of row
     * @param col base-1 index of column
     */
    public void open(int row, int col) {
        int currIndex = indexOf(row, col);
        isOpen[currIndex] = true;

        if (row == 1) {
            backwashQU.union(currIndex, topIndex);  // Top
            normalQU.union(currIndex, topIndex);
        }
        if (row == n) {
            backwashQU.union(currIndex, btmIndex);  // Bottom
        }
        tryUnion(row, col, row - 1, col);  // North 
        tryUnion(row, col, row + 1, col);  // South        
        tryUnion(row, col, row, col - 1);  // West
        tryUnion(row, col, row, col + 1);  // East
    }

    private void tryUnion(int rowA, int colA, int rowB, int colB) {
        // I assume that (rowA, colA) is correct.
        if (0 < rowB && rowB <= n && 0 < colB && colB <= n
                && isOpen(rowB, colB)) {
            backwashQU.union(indexOf(rowA, colA), indexOf(rowB, colB));
            normalQU.union(indexOf(rowA, colA), indexOf(rowB, colB));
        }
    }

    /**
     * Is site (row, col) open?
     *
     * @param row base-1 index of row
     * @param col base-1 index of column
     */
    public boolean isOpen(int row, int col) {
        return isOpen[indexOf(row, col)];
    }

    /**
     * Is site (row, col) full?
     *
     * @param row base-1 index of row
     * @param col base-1 index of column
     */
    public boolean isFull(int row, int col) {
        return normalQU.connected(topIndex, indexOf(row, col));
    }

    /**
     * Does the system percolate?
     */
    public boolean percolates() {
        return backwashQU.connected(topIndex, btmIndex);
    }

    public static void main(String[] args) {
        StdOut.println("Please run PercolationStats instead.");
    }
}
