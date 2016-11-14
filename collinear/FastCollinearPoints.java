import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * A faster, sorting-based solution. Remarkably, it is possible to solve the
 * problem much faster than the brute-force solution.
 *
 * @author Mincong Huang
 */
public class FastCollinearPoints {

    private final LineSegment[] lineSegments;

    /**
     * Finds all line segments containing 4 points or more points.
     */
    public FastCollinearPoints(Point[] points) {

        checkNull(points);
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkDuplicate(sortedPoints);

        final int N = points.length;
        final boolean[][] isMaxLineSegments = new boolean[N][N];
        int nbMaxLineSegments = 0;

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                isMaxLineSegments[i][j] = true;
                nbMaxLineSegments++;
            }
        }

        for (int i = 0; i < N; i++) {

            Point p = sortedPoints[i];
            Point[] pointsBySlope = sortedPoints.clone();
            Arrays.sort(pointsBySlope, p.slopeOrder());

            // Notice the difference between "sortedPoints" & "pointsBySlope":
            // the below points are taken from "pointsBySlope".
            int x = 1;
            while (x < N) {

                LinkedList<Point> candidates = new LinkedList<>();
                final double SLOPE_REF = p.slopeTo(pointsBySlope[x]);
                do {
                    candidates.add(pointsBySlope[x++]);
                } while (x < N && p.slopeTo(pointsBySlope[x]) == SLOPE_REF);

                // Condition 1:
                //     Candidates are collinear if at least 4 points are located
                //     at the same line: so at least 3 without "p".
                // Condition 2:
                //     The max line segment is created by the point "p" and the
                //     last point in candidates iif "p" is the smallest point
                //     having this slope comparing to all candidates.
                if (candidates.size() >= 3
                        && p.compareTo(candidates.peek()) < 0) {
                    // Its value in matrix "isMaxLineSegements" remains true.
                    candidates.removeLast();
                }
                // Other points aren't the max line segment, change the matrix.
                while (!candidates.isEmpty()) {
                    int j = Arrays.binarySearch(sortedPoints, candidates.pop());
                    if (isMaxLineSegments[i][j]) {
                        isMaxLineSegments[i][j] = false;
                        nbMaxLineSegments--;
                    }
                }
            }
        }

        lineSegments = new LineSegment[nbMaxLineSegments];
        int x = 0;
        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                if (isMaxLineSegments[i][j]) {
                    Point pointI = sortedPoints[i];
                    Point pointJ = sortedPoints[j];
                    lineSegments[x++] = new LineSegment(pointI, pointJ);
                }
            }
        }
    }

    private void checkNull(Point[] points) {
        if (points == null) {
            throw new NullPointerException("The array \"Points\" is null.");
        }
        for (Point p : points) {
            if (p == null) {
                throw new NullPointerException(
                        "The array \"Points\" contains null element.");
            }
        }
    }

    private void checkDuplicate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicate(s) found.");
            }
        }
    }

    /**
     * The number of line segments.
     */
    public int numberOfSegments() {
        return lineSegments.length;
    }

    /**
     * The line segments.
     */
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    /**
     * Simple client provided by Princeton University.
     */
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
