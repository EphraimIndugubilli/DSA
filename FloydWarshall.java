// time complexity: O(V³) — three nested loops over all vertex pairs and intermediates
// space complexity: O(V²) for the distance and predecessor matrices
// Floyd-Warshall computes shortest paths between ALL pairs of vertices in a
// weighted graph. Unlike Dijkstra (single-source, non-negative weights) or
// Bellman-Ford (single-source, detects negative cycles), Floyd-Warshall handles
// negative edge weights and surfaces negative cycles directly.
// Core idea: dist[i][j] = min(direct path i→j, any path i→k→j for all k).
// After V relaxation rounds the matrix holds true shortest-path distances.
import java.util.*;

class FloydWarshall {
    static final int INF = Integer.MAX_VALUE / 2; // half of MAX_VALUE avoids overflow on addition

    private final int V;
    private final long[][] dist;   // dist[i][j] = shortest path weight from i to j
    private final int[][]  next;   // next[i][j] = first hop on the shortest path i→j

    /**
     * Build a Floyd-Warshall instance for a graph with V vertices.
     * Call addEdge() for every directed edge, then run() to compute all-pairs distances.
     */
    public FloydWarshall(int V) {
        this.V    = V;
        this.dist = new long[V][V];
        this.next = new int[V][V];
        for (long[] row : dist) Arrays.fill(row, INF);
        for (int[] row : next) Arrays.fill(row, -1);
        for (int i = 0; i < V; i++) {
            dist[i][i] = 0;
            next[i][i] = i;
        }
    }

    /** Add a directed edge u→v with given weight. For undirected graphs call twice. */
    public void addEdge(int u, int v, long weight) {
        if (weight < dist[u][v]) {
            dist[u][v] = weight;
            next[u][v] = v;
        }
    }

    /**
     * Run the Floyd-Warshall algorithm.
     * After this call, dist[i][j] holds the shortest-path distance from i to j,
     * or INF if unreachable. dist[i][i] < 0 after this call signals a negative cycle.
     */
    public void run() {
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                if (dist[i][k] == INF) continue; // pruning: skip unreachable pairs
                for (int j = 0; j < V; j++) {
                    if (dist[k][j] == INF) continue;
                    long through = dist[i][k] + dist[k][j];
                    if (through < dist[i][j]) {
                        dist[i][j] = through;
                        next[i][j] = next[i][k]; // route i→j via k
                    }
                }
            }
        }
    }

    /** Return true if the graph contains at least one negative-weight cycle. */
    public boolean hasNegativeCycle() {
        for (int i = 0; i < V; i++)
            if (dist[i][i] < 0) return true;
        return false;
    }

    /** Shortest-path distance from src to dst, or INF if unreachable. */
    public long distance(int src, int dst) {
        return dist[src][dst];
    }

    /**
     * Reconstruct the actual path from src to dst as an ordered list of vertices.
     * Returns an empty list if dst is unreachable from src.
     * Returns null if any vertex on the path lies on a negative cycle.
     */
    public List<Integer> path(int src, int dst) {
        if (dist[src][dst] == INF) return Collections.emptyList();
        List<Integer> p = new ArrayList<>();
        int cur = src;
        while (cur != dst) {
            if (cur == -1) return null; // negative cycle in path
            p.add(cur);
            cur = next[cur][dst];
            if (p.size() > V) return null; // negative cycle detected
        }
        p.add(dst);
        return p;
    }

    /** Print the full V×V distance matrix. */
    public void printMatrix() {
        System.out.println("Distance matrix (INF = " + INF + "):");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (dist[i][j] == INF) System.out.printf("%6s", "INF");
                else                   System.out.printf("%6d", dist[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Number of vertices: ");
        int V = sc.nextInt();
        System.out.print("Number of directed edges: ");
        int E = sc.nextInt();

        FloydWarshall fw = new FloydWarshall(V);

        System.out.println("Enter each edge as: u v weight (0-indexed vertices)");
        for (int i = 0; i < E; i++) {
            int u = sc.nextInt(), v = sc.nextInt();
            long w = sc.nextLong();
            fw.addEdge(u, v, w);
        }

        fw.run();

        if (fw.hasNegativeCycle()) {
            System.out.println("Graph contains a negative-weight cycle — distances are not meaningful.");
        } else {
            fw.printMatrix();
            System.out.print("Enter source and destination for path query: ");
            int src = sc.nextInt(), dst = sc.nextInt();
            long d = fw.distance(src, dst);
            if (d == INF) {
                System.out.println("No path from " + src + " to " + dst + ".");
            } else {
                System.out.println("Shortest distance " + src + " → " + dst + " = " + d);
                List<Integer> p = fw.path(src, dst);
                if (p != null) System.out.println("Path: " + p);
            }
        }
        sc.close();
    }
}
