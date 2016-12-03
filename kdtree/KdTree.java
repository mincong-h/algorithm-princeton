import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.LinkedList;
import java.util.List;

/**
 * 2d-tree implementation. A mutable data type that uses a 2d-tree to implement
 * the same API (but replace {@code PointSET} with {@code KdTree}). A 2d-tree is
 * a generalization of a BST to two-dimensional keys. The idea is to build a BST
 * with points in the nodes, using the x- and y-coordinates of the points as
 * keys in strictly alternating sequence.
 *
 * @author Mincong Huang
 */
public class KdTree {

    private enum Separator { VERTICAL, HORIZONTAL }
    private Node root;
    private int size;

    private static class Node {

        private final Separator separator;
        private final Point2D p;
        private RectHV rect;
        private Node nodeLB;  // child node left/bottom
        private Node nodeRT;  // child node right/top

        Node(Point2D p, Separator separator, RectHV rect) {
            this.p = p;
            this.separator = separator;
            this.rect = rect;
        }

        public Separator nextSeparator() {
            return separator == Separator.VERTICAL ?
                Separator.HORIZONTAL : Separator.VERTICAL;
        }

        public RectHV rectLB() {
            return separator == Separator.VERTICAL
                    ? new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax())
                    : new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
        }

        public RectHV rectRT() {
            return separator == Separator.VERTICAL
                    ? new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax())
                    : new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
        }

        public boolean isRightOrTopOf(Point2D q) {
            return (separator == Separator.HORIZONTAL && p.y() > q.y())
                    || (separator == Separator.VERTICAL && p.x() > q.x());
        }
    }

    /**
     * Construct an empty set of points
     */
    public KdTree() {
        root = null;
        size = 0;
    }

    /**
     * Is the set empty?
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Number of points in the set
     */
    public int size() {
        return size;
    }

    /**
     * Add the point to the set (if it is not already in the set)
     */
    public void insert(Point2D p) {
        checkNull(p);
        if (root == null) {
            root = new Node(p, Separator.VERTICAL, new RectHV(0, 0, 1, 1));
            size++;
            return;
        }

        Node prev = null;
        Node curr = root;
        do {
            if (curr.p.equals(p)) {
                return;
            }
            prev = curr;
            curr = curr.isRightOrTopOf(p) ? curr.nodeLB : curr.nodeRT; 
        } while (curr != null);

        // Prepare new node and insert
        if (prev.isRightOrTopOf(p)) {
            prev.nodeLB = new Node(p, prev.nextSeparator(), prev.rectLB());
        } else {
            prev.nodeRT = new Node(p, prev.nextSeparator(), prev.rectRT());
        }
        size++;
    }

    /**
     * Does the set contain point p?
     */
    public boolean contains(Point2D p) {
        checkNull(p);
        Node node = root;
        while (node != null) {
            if (node.p.equals(p)) {
                return true;
            }
            node = node.isRightOrTopOf(p) ? node.nodeLB : node.nodeRT;
        }
        return false;
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
        List<Point2D> results = new LinkedList<>();
        addAll(root, rect, results);
        return results;
    }

    /**
     * Add all points under target node using DFS.
     */
    private void addAll(Node node, RectHV rect, List<Point2D> results) {
        if (node == null) {
            return;
        }
        Separator sepr = node.separator;
        Point2D p = node.p;
        if (rect.contains(p)) {
            results.add(p);
        }
        if ((sepr == Separator.HORIZONTAL && p.y() >= rect.ymin())
                || (sepr == Separator.VERTICAL && p.x() >= rect.xmin())) {
            addAll(node.nodeLB, rect, results);
        }
        if ((sepr == Separator.HORIZONTAL && p.y() <= rect.ymax())
                || (sepr == Separator.VERTICAL && p.x() <= rect.xmax())) {
            addAll(node.nodeRT, rect, results);
        }
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        checkNull(p);
        return isEmpty() ? null : nearest(p, root.p, root);
    }

    private Point2D nearest(Point2D target, Point2D closest, Node node) {
        double nodeDist = node.p.distanceTo(target);
        double closestDist = closest.distanceTo(target);
        // Challenge the current closest point
        if (nodeDist < closestDist) {
            closest = node.p;
            closestDist = nodeDist;
        }
        // Recursively search left/bottom or right/top
        // if it could contain a closer point
        Node nodeLB = node.nodeLB;
        Node nodeRT = node.nodeRT;
        if (node.isRightOrTopOf(target)) {
            // go left/bottom, then right/top
            if (nodeLB != null) {
                if (nodeLB.rect.distanceTo(target) < closestDist) {
                    closest = nearest(target, closest, nodeLB);
                }
            }
            if (nodeRT != null) {
                if (nodeRT.rect.distanceTo(target) < closestDist) {
                    closest = nearest(target, closest, nodeRT);
                }
            }
        } else {
            // go right/top, then left/bottom
            if (nodeRT != null) {
                if (nodeRT.rect.distanceTo(target) < closestDist) {
                    closest = nearest(target, closest, nodeRT);
                }
            }
            if (nodeLB != null) {
                if (nodeLB.rect.distanceTo(target) < closestDist) {
                    closest = nearest(target, closest, nodeLB);
                }
            }
        }
        return closest;
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
