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

    /**
     * Directions for inner class: {@code Node}.
     */
    private enum Direction { VERTICAL, HORIZONTAL }

    private Node root;

    private int size;

    /**
     * Inner class tree-node for 2D tree.
     */
    private class Node {

        private final Direction direction;
        private final Point2D p;
        private RectHV rect;
        private Node left;  // left child node (on the left or bottom)
        private Node right; // right child node (on the right or top)

        Node(Point2D p, Direction direction, RectHV rect) {
            this.p = p;
            this.direction = direction;
            this.rect = rect;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Direction direction() {
            return direction;
        }

        public Point2D point() {
            return p;
        }

        public RectHV rect() {
            return rect;
        }

        public RectHV getLeftRect() {
            return direction == Direction.VERTICAL
                    ? new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax())
                    : new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
        }

        public RectHV getRightRect() {
            return direction == Direction.VERTICAL
                    ? new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax())
                    : new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
        }

        public boolean greaterThan(Point2D q) {
            return (direction == Direction.HORIZONTAL && q.y() < p.y())
                    || (direction == Direction.VERTICAL && q.x() < p.x());
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
            root = new Node(p, Direction.VERTICAL, new RectHV(0, 0, 1, 1));
            size++;
            return;
        }

        Node prev = null;
        Node curr = root;
        do {
            if (curr.point().equals(p)) {
                return;
            }
            prev = curr;
            curr = curr.greaterThan(p) ? curr.getLeft() : curr.getRight(); 
        } while (curr != null);

        // Prepare new node and insert
        Direction dir = opposite(prev.direction());
        if (prev.greaterThan(p)) {
            prev.setLeft(new Node(p, dir, prev.getLeftRect()));
        } else {
            prev.setRight(new Node(p, dir, prev.getRightRect()));
        }
        size++;
    }

    private Direction opposite(Direction d) {
        return d == Direction.VERTICAL ?
                Direction.HORIZONTAL : Direction.VERTICAL;
    }

    /**
     * Does the set contain point p?
     */
    public boolean contains(Point2D p) {
        checkNull(p);
        Node node = root;
        while (node != null) {
            if (node.point().equals(p)) {
                return true;
            }
            node = node.greaterThan(p) ? node.getLeft() : node.getRight();
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
        List<Point2D> pointsInRect = new LinkedList<>();
        dfs(root, rect, pointsInRect);
        return pointsInRect;
    }

    private void dfs(Node node, RectHV rect, List<Point2D> results) {
        if (node == null) {
            return;
        }
        Direction dir = node.direction();
        Point2D p = node.point();
        if (rect.contains(p)) {
            results.add(p);
        }
        if ((dir == Direction.HORIZONTAL && p.y() >= rect.ymin())
                || (dir == Direction.VERTICAL && p.x() >= rect.xmin())) {
            dfs(node.getLeft(), rect, results);
        }
        if ((dir == Direction.HORIZONTAL && p.y() <= rect.ymax())
                || (dir == Direction.VERTICAL && p.x() <= rect.xmax())) {
            dfs(node.getRight(), rect, results);
        }
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        checkNull(p);
        return isEmpty() ? null : nearest(p, root.point(), root);
    }

    private Point2D nearest(Point2D target, Point2D closest, Node node) {
        double nodeDist = node.point().distanceTo(target);
        double closestDist = closest.distanceTo(target);
        // Challenge the current closest point
        if (nodeDist < closestDist) {
            closest = node.point();
            closestDist = nodeDist;
        }
        // Recursively search left/bottom or right/top
        // if it could contain a closer point
        Node left = node.getLeft();
        Node right = node.getRight();
        if (node.greaterThan(target)) {
            // go left, then right
            if (left != null && left.rect().distanceTo(target) < closestDist) {
                closest = nearest(target, closest, left);
            }
            if (right != null && right.rect().distanceTo(target)
                    < closestDist) {
                closest = nearest(target, closest, right);
            }
        } else {
            // go right, then left
            if (right != null && right.rect().distanceTo(target)
                    < closestDist) {
                closest = nearest(target, closest, right);
            }
            if (left != null && left.rect().distanceTo(target) < closestDist) {
                closest = nearest(target, closest, left);
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
