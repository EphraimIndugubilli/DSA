// time complexity: O(log n) for point/range update and range query
// space complexity: O(n) for the tree, O(n) lazy array
// A Segment Tree with lazy propagation supports:
//   1. range update  — add a value to every element in [l, r]
//   2. range query   — sum of elements in [l, r]
// Both operations run in O(log n) via deferred "lazy" pushdowns.
import java.util.*;

class SegmentTree {
    private long[] tree;  // tree[i] = sum of the segment
    private long[] lazy;  // lazy[i] = pending addend not yet pushed to children
    private int n;

    public SegmentTree(int n) {
        this.n = n;
        this.tree = new long[4 * n];
        this.lazy = new long[4 * n];
    }

    // Build from an existing array in O(n)
    public SegmentTree(int[] arr) {
        this.n = arr.length;
        this.tree = new long[4 * n];
        this.lazy = new long[4 * n];
        build(arr, 1, 0, n - 1);
    }

    private void build(int[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
            return;
        }
        int mid = (start + end) / 2;
        build(arr, 2 * node, start, mid);
        build(arr, 2 * node + 1, mid + 1, end);
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    // Push lazy value down to children before visiting them
    private void pushDown(int node, int start, int end) {
        if (lazy[node] == 0) return;
        int mid = (start + end) / 2;
        int leftLen  = mid - start + 1;
        int rightLen = end - mid;
        tree[2 * node]     += lazy[node] * leftLen;
        tree[2 * node + 1] += lazy[node] * rightLen;
        lazy[2 * node]     += lazy[node];
        lazy[2 * node + 1] += lazy[node];
        lazy[node] = 0;
    }

    // Add delta to every element in [l, r] (0-indexed)
    public void rangeUpdate(int l, int r, long delta) {
        rangeUpdate(1, 0, n - 1, l, r, delta);
    }

    private void rangeUpdate(int node, int start, int end, int l, int r, long delta) {
        if (r < start || end < l) return;
        if (l <= start && end <= r) {
            tree[node] += delta * (end - start + 1);
            lazy[node] += delta;
            return;
        }
        pushDown(node, start, end);
        int mid = (start + end) / 2;
        rangeUpdate(2 * node, start, mid, l, r, delta);
        rangeUpdate(2 * node + 1, mid + 1, end, l, r, delta);
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    // Point update: set arr[i] to value (implemented as a range update of length 1)
    public void pointSet(int i, long value) {
        long current = rangeQuery(i, i);
        rangeUpdate(i, i, value - current);
    }

    // Sum of elements in [l, r] (0-indexed)
    public long rangeQuery(int l, int r) {
        return rangeQuery(1, 0, n - 1, l, r);
    }

    private long rangeQuery(int node, int start, int end, int l, int r) {
        if (r < start || end < l) return 0;
        if (l <= start && end <= r) return tree[node];
        pushDown(node, start, end);
        int mid = (start + end) / 2;
        return rangeQuery(2 * node, start, mid, l, r)
             + rangeQuery(2 * node + 1, mid + 1, end, l, r);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter array size: ");
        int n = sc.nextInt();
        int[] arr = new int[n];
        System.out.print("Enter " + n + " elements: ");
        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();

        SegmentTree st = new SegmentTree(arr);

        System.out.print("Range sum query [l r] (0-indexed): ");
        int l = sc.nextInt(), r = sc.nextInt();
        System.out.println("Sum [" + l + ", " + r + "] = " + st.rangeQuery(l, r));

        System.out.print("Range update [l r delta] — add delta to every element in range: ");
        int ul = sc.nextInt(), ur = sc.nextInt();
        long delta = sc.nextLong();
        st.rangeUpdate(ul, ur, delta);
        System.out.println("After adding " + delta + " to [" + ul + ", " + ur + "]:");
        System.out.println("Sum [" + l + ", " + r + "] = " + st.rangeQuery(l, r));

        sc.close();
    }
}
