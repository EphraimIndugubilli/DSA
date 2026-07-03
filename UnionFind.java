// time complexity: O(α(n)) amortised for both union and find — effectively O(1)
// space complexity: O(n)
// Union-Find (Disjoint Set Union) with path compression and union by rank.
// Supports two operations on a set of n elements (0-indexed):
//   1. union(a, b) — merge the sets containing a and b
//   2. find(a)     — return the canonical representative of a's set
// Connected-components query: find(a) == find(b) iff a and b are in the same set.
//
// Applications: Kruskal's MST, cycle detection in undirected graphs,
//               network connectivity, image segmentation, LCA offline.
import java.util.*;

class UnionFind {
    private int[] parent; // parent[i] = parent of node i (root if parent[i] == i)
    private int[] rank;   // rank[i] = upper bound on the height of the subtree rooted at i
    private int components; // number of disjoint sets

    public UnionFind(int n) {
        parent = new int[n];
        rank   = new int[n];
        components = n;
        for (int i = 0; i < n; i++) parent[i] = i; // each node is its own root initially
    }

    // Find the root of x with path compression (flattens the tree on every lookup)
    public int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]); // path compression: make x point directly to root
        return parent[x];
    }

    // Union by rank: attach the shorter tree under the taller one to keep depth minimal.
    // Returns true if a and b were in different sets (a merge happened).
    public boolean union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return false; // already in the same set
        if      (rank[ra] < rank[rb]) parent[ra] = rb;
        else if (rank[ra] > rank[rb]) parent[rb] = ra;
        else { parent[rb] = ra; rank[ra]++; }
        components--;
        return true;
    }

    // Returns true if a and b belong to the same set
    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    // Number of disjoint sets remaining
    public int countComponents() {
        return components;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of nodes: ");
        int n = sc.nextInt();
        UnionFind uf = new UnionFind(n);

        System.out.print("Enter number of edges: ");
        int e = sc.nextInt();
        System.out.println("Enter edges as pairs (u v), one per line:");

        for (int i = 0; i < e; i++) {
            int u = sc.nextInt(), v = sc.nextInt();
            boolean merged = uf.union(u, v);
            System.out.println("union(" + u + ", " + v + ") → " +
                (merged ? "merged" : "already connected"));
        }

        System.out.println("\nConnected components: " + uf.countComponents());

        System.out.print("Query — are two nodes connected? Enter a b: ");
        int a = sc.nextInt(), b = sc.nextInt();
        System.out.println(a + " and " + b + " are " +
            (uf.connected(a, b) ? "in the same component." : "in different components."));

        sc.close();
    }
}
