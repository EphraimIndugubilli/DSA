// time complexity: O(log n) for both update and prefix-sum query
// space complexity: O(n)
// A Fenwick Tree (Binary Indexed Tree) supports two operations on an array:
//   1. point update  — add a value to index i
//   2. prefix query  — sum of elements from index 1 to i
// Range sum [l, r] = query(r) - query(l - 1)
import java.util.*;

class FenwickTree {
    private int[] tree;
    private int n;

    public FenwickTree(int n) {
        this.n = n;
        this.tree = new int[n + 1]; // 1-indexed
    }

    // Build from existing array in O(n)
    public FenwickTree(int[] arr) {
        this.n = arr.length;
        this.tree = new int[n + 1];
        for (int i = 0; i < n; i++) update(i + 1, arr[i]);
    }

    // Add delta to position i (1-indexed)
    public void update(int i, int delta) {
        for (; i <= n; i += i & (-i))
            tree[i] += delta;
    }

    // Prefix sum from 1 to i (1-indexed)
    public int query(int i) {
        int sum = 0;
        for (; i > 0; i -= i & (-i))
            sum += tree[i];
        return sum;
    }

    // Range sum from l to r (1-indexed, inclusive)
    public int rangeQuery(int l, int r) {
        return query(r) - query(l - 1);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter array size: ");
        int n = sc.nextInt();
        int[] arr = new int[n];
        System.out.print("Enter " + n + " elements: ");
        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();

        FenwickTree ft = new FenwickTree(arr);

        System.out.print("Enter query range [l r] (1-indexed): ");
        int l = sc.nextInt(), r = sc.nextInt();
        System.out.println("Sum [" + l + ", " + r + "] = " + ft.rangeQuery(l, r));

        System.out.print("Point update — index and delta: ");
        int idx = sc.nextInt(), delta = sc.nextInt();
        ft.update(idx, delta);
        System.out.println("After update, sum [" + l + ", " + r + "] = " + ft.rangeQuery(l, r));

        sc.close();
    }
}
