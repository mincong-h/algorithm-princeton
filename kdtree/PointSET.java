import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * Brute-force implementation. A mutable data type that represents a set of
 * points in the unit square. Implemented by using a red-black BST of
 * {@code java.util.TreeSet}.
 *
 * @author Mincong Huang
 */
public class PointSET {

    private TreeSet<Point2D> points;

    /**
     * Construct an empty set of points
     */
    public PointSET() {
        points = new TreeSet<>();
    }

    /**
     * Is the set empty?
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /**
     * Number of points in the set
     */
    public int size() {
        return points.size();
    }

    /**
     * Add the point to the set (if it is not already in the set)
     */
    public void insert(Point2D p) {
        checkNull(p);
        if (!points.contains(p)) {
            points.add(p);
        }
    }

    /**
     * Does the set contain point p?
     */
    public boolean contains(Point2D p) {
        checkNull(p);
        return points.contains(p);
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        // TODO
    }

    /**
     * All points that are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        Point2D minPoint = new Point2D(rect.xmin(), rect.ymin());
        Point2D maxPoint = new Point2D(rect.xmax(), rect.ymax());
        List<Point2D> pointsInRect = new LinkedList<>();
        // The sub-set is inclusive for both extremities
        for (Point2D p : points.subSet(minPoint, true, maxPoint, true)) {
            // The y-coordinate has been validated by the minPoint & maxPoint
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax()) {
                pointsInRect.add(p);
            }
        }
        return pointsInRect;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {

        checkNull(p);
        if (isEmpty()) {
            return null;
        }

        // 1. Find the 2 neighbour points in natural order, then find the
        //    closest distance `d`.
        // 2. Widen the navigatable set to a circle of `d`, save the nearest.
        Point2D next = points.ceiling(p);
        Point2D prev = points.floor(p);
        if (next == null && prev == null) {
            return null;
        }

        double distNext = next == null ? Double.MAX_VALUE : p.distanceTo(next);
        double distPrev = prev == null ? Double.MAX_VALUE : p.distanceTo(prev);
        double d = Math.min(distNext, distPrev);

        Point2D minPoint = new Point2D(p.x(), p.y() - d);
        Point2D maxPoint = new Point2D(p.x(), p.y() + d);
        Point2D nearest = next == null ? prev : next;  // cannot be both null

        // The sub-set is inclusive for both extremities
        for (Point2D candidate: points.subSet(minPoint, true, maxPoint, true)) {
            if (p.distanceTo(candidate) < p.distanceTo(nearest)) {
                nearest = candidate;
            }
        }
        return nearest;
    }

    private void checkNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    /**
     * Unit testing of the methods (optional)
     */
    public static void main(String[] args) {
    }
}
