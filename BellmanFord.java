// Bellman-Ford: single-source shortest paths on a graph with negative edge weights
// time complexity:  O(V * E) — relax every edge V-1 times
// space complexity: O(V) — distance + predecessor arrays
//
// Key advantage over Dijkstra: handles negative-weight edges and detects
// negative-weight cycles (which make shortest paths undefined). Dijkstra
// is faster (O(E log V)) but requires non-negative edge weights.

import java.util.*;

class BellmanFord {

    static final long INF = Long.MAX_VALUE / 2;

    record Edge(int u, int v, long w) {}

    /**
     * Compute shortest distances from {@code src} to all other vertices.
     *
     * @param n     number of vertices (0-indexed)
     * @param edges list of directed edges (u -> v, weight w)
     * @param src   source vertex
     * @return dist[] where dist[i] is the shortest distance from src to i,
     *         or {@code null} if the graph contains a negative-weight cycle
     *         reachable from src.
     */
    static long[] shortestPaths(int n, List<Edge> edges, int src) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        // Relax every edge V-1 times.  After k iterations dist[v] holds the
        // shortest path using at most k edges; after V-1 iterations it is the
        // true shortest path (assuming no negative-weight cycle).
        for (int i = 0; i < n - 1; i++) {
            boolean updated = false;
            for (Edge e : edges) {
                if (dist[e.u()] != INF && dist[e.u()] + e.w() < dist[e.v()]) {
                    dist[e.v()] = dist[e.u()] + e.w();
                    updated = true;
                }
            }
            if (!updated) break; // early exit: no changes => converged
        }

        // One more pass to detect negative-weight cycles.
        // If any distance still decreases, a negative cycle is reachable.
        for (Edge e : edges) {
            if (dist[e.u()] != INF && dist[e.u()] + e.w() < dist[e.v()]) {
                return null; // negative-weight cycle detected
            }
        }

        return dist;
    }

    /**
     * Reconstruct the shortest path from src to dst using a predecessor array.
     * Returns an empty list if dst is unreachable from src.
     */
    static List<Integer> reconstructPath(int n, List<Edge> edges, int src, int dst) {
        long[] dist = new long[n];
        int[]  prev = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(prev, -1);
        dist[src] = 0;

        for (int i = 0; i < n - 1; i++) {
            for (Edge e : edges) {
                if (dist[e.u()] != INF && dist[e.u()] + e.w() < dist[e.v()]) {
                    dist[e.v()] = dist[e.u()] + e.w();
                    prev[e.v()] = e.u();
                }
            }
        }

        if (dist[dst] == INF) return Collections.emptyList();

        Deque<Integer> path = new ArrayDeque<>();
        for (int v = dst; v != -1; v = prev[v]) path.addFirst(v);
        return new ArrayList<>(path);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of vertices: ");
        int n = sc.nextInt();
        System.out.print("Enter number of edges: ");
        int m = sc.nextInt();

        List<Edge> edges = new ArrayList<>();
        System.out.println("Enter edges as 'u v w' (0-indexed, directed):");
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt(), v = sc.nextInt();
            long w = sc.nextLong();
            edges.add(new Edge(u, v, w));
        }

        System.out.print("Enter source vertex: ");
        int src = sc.nextInt();

        long[] dist = shortestPaths(n, edges, src);
        if (dist == null) {
            System.out.println("Graph contains a negative-weight cycle reachable from " + src + " — shortest paths are undefined.");
        } else {
            System.out.println("Shortest distances from vertex " + src + ":");
            for (int i = 0; i < n; i++) {
                System.out.println("  -> " + i + " : " + (dist[i] == INF ? "unreachable" : dist[i]));
            }

            System.out.print("Show path to which vertex? ");
            int dst = sc.nextInt();
            List<Integer> path = reconstructPath(n, edges, src, dst);
            if (path.isEmpty()) {
                System.out.println("No path from " + src + " to " + dst);
            } else {
                System.out.println("Path: " + path);
                System.out.println("Distance: " + dist[dst]);
            }
        }

        sc.close();
    }
}
