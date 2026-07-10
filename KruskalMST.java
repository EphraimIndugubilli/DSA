// time complexity: O(E log E) for sorting edges; O(E α(V)) for union-find ops — total O(E log E)
// space complexity: O(V + E)
// Kruskal's Minimum Spanning Tree uses a greedy strategy: sort all edges by
// weight, then add each edge to the MST only if it doesn't form a cycle.
// Cycle detection runs in near-O(1) per edge via the Union-Find (DSU) structure.
// The result is a spanning tree of V-1 edges with minimum total weight.
//
// Comparison with Prim's: Kruskal is edge-centric and faster on sparse graphs
// (E ≪ V²); Prim is vertex-centric and faster on dense graphs with adjacency matrices.
import java.util.*;

class KruskalMST {

    // Union-Find (DSU) — path compression + union by rank for near-O(1) ops
    static int[] parent, rank;

    static void init(int n) {
        parent = new int[n];
        rank   = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]); // path compression
        return parent[x];
    }

    // Returns false if a and b are already in the same component (would form a cycle)
    static boolean union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;
        if      (rank[ra] < rank[rb]) parent[ra] = rb;
        else if (rank[ra] > rank[rb]) parent[rb] = ra;
        else { parent[rb] = ra; rank[ra]++; }
        return true;
    }

    // Edge representation: weight, u, v
    static int[][] kruskal(int vertices, int[][] edges) {
        init(vertices);

        // Sort edges by weight ascending
        Arrays.sort(edges, Comparator.comparingInt(e -> e[0]));

        List<int[]> mst = new ArrayList<>();
        long totalWeight = 0;

        for (int[] edge : edges) {
            int w = edge[0], u = edge[1], v = edge[2];
            if (union(u, v)) {            // no cycle — safe to add
                mst.add(edge);
                totalWeight += w;
                if (mst.size() == vertices - 1) break; // MST complete
            }
        }

        // Check connectivity: a valid MST on V vertices must have exactly V-1 edges
        if (mst.size() < vertices - 1) {
            System.out.println("Graph is disconnected — no spanning tree exists.");
            return new int[0][];
        }

        System.out.println("MST total weight: " + totalWeight);
        return mst.toArray(new int[0][]);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of vertices and edges: ");
        int V = sc.nextInt(), E = sc.nextInt();

        int[][] edges = new int[E][3];
        System.out.println("Enter edges as: weight u v (0-indexed vertices)");
        for (int i = 0; i < E; i++) {
            edges[i][0] = sc.nextInt(); // weight
            edges[i][1] = sc.nextInt(); // u
            edges[i][2] = sc.nextInt(); // v
        }

        System.out.println("\nKruskal's MST edges (weight, u -- v):");
        int[][] mst = kruskal(V, edges);
        for (int[] e : mst) {
            System.out.printf("  %d   %d -- %d%n", e[0], e[1], e[2]);
        }

        sc.close();
    }
}
