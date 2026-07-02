// time complexity: O((V + E) log V) with a binary-heap priority queue
// space complexity: O(V + E) for adjacency list and dist array
// Dijkstra's shortest-path algorithm:
//   Given a weighted directed graph with non-negative edge weights, finds
//   the minimum-cost path from a source vertex to every other vertex.
//   Uses a min-heap to always relax the closest unvisited vertex first.
import java.util.*;

public class Dijkstra {

    // Immutable weighted edge stored in the adjacency list
    private static final class Edge {
        final int to, weight;
        Edge(int to, int weight) { this.to = to; this.weight = weight; }
    }

    // Entry queued in the min-heap: (cost so far, vertex)
    private static final class State implements Comparable<State> {
        final int vertex, cost;
        State(int vertex, int cost) { this.vertex = vertex; this.cost = cost; }
        @Override public int compareTo(State o) { return Integer.compare(this.cost, o.cost); }
    }

    private final int V;
    private final List<List<Edge>> adj;

    public Dijkstra(int vertices) {
        this.V = vertices;
        this.adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
    }

    // Add a directed edge u -> v with the given weight (weight must be >= 0)
    public void addEdge(int u, int v, int weight) {
        if (weight < 0) throw new IllegalArgumentException("Negative edge weight: " + weight);
        adj.get(u).add(new Edge(v, weight));
    }

    // Add an undirected edge by inserting both directed halves
    public void addUndirectedEdge(int u, int v, int weight) {
        addEdge(u, v, weight);
        addEdge(v, u, weight);
    }

    /**
     * Compute shortest distances from {@code src} to every other vertex.
     *
     * @return int[] where result[i] = shortest distance from src to i,
     *         or Integer.MAX_VALUE if vertex i is unreachable.
     */
    public int[] shortestPaths(int src) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        PriorityQueue<State> pq = new PriorityQueue<>();
        pq.offer(new State(src, 0));

        while (!pq.isEmpty()) {
            State cur = pq.poll();

            // Skip stale entries (a shorter path was already found)
            if (cur.cost > dist[cur.vertex]) continue;

            for (Edge e : adj.get(cur.vertex)) {
                if (dist[cur.vertex] == Integer.MAX_VALUE) continue; // guard overflow
                int newCost = dist[cur.vertex] + e.weight;
                if (newCost < dist[e.to]) {
                    dist[e.to] = newCost;
                    pq.offer(new State(e.to, newCost));
                }
            }
        }
        return dist;
    }

    /**
     * Reconstruct the actual shortest path from src to dst using a parent array.
     *
     * @return list of vertex indices on the shortest path (inclusive), or an
     *         empty list if dst is unreachable from src.
     */
    public List<Integer> shortestPath(int src, int dst) {
        int[] dist   = new int[V];
        int[] parent = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[src] = 0;

        PriorityQueue<State> pq = new PriorityQueue<>();
        pq.offer(new State(src, 0));

        while (!pq.isEmpty()) {
            State cur = pq.poll();
            if (cur.cost > dist[cur.vertex]) continue;
            if (cur.vertex == dst) break; // early exit once destination settled

            for (Edge e : adj.get(cur.vertex)) {
                if (dist[cur.vertex] == Integer.MAX_VALUE) continue;
                int newCost = dist[cur.vertex] + e.weight;
                if (newCost < dist[e.to]) {
                    dist[e.to] = newCost;
                    parent[e.to] = cur.vertex;
                    pq.offer(new State(e.to, newCost));
                }
            }
        }

        if (dist[dst] == Integer.MAX_VALUE) return Collections.emptyList();

        LinkedList<Integer> path = new LinkedList<>();
        for (int v = dst; v != -1; v = parent[v]) path.addFirst(v);
        return path;
    }

    // ── Demo ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        // Graph with 6 vertices (0-5):
        //   0 --4--> 1 --1--> 3
        //   0 --2--> 2 --5--> 3
        //   2 --3--> 1
        //   1 --2--> 4
        //   3 --1--> 4
        //   4 --3--> 5
        Dijkstra g = new Dijkstra(6);
        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 2);
        g.addEdge(2, 1, 3);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 5);
        g.addEdge(1, 4, 2);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 5, 3);

        int src = 0;
        int[] dist = g.shortestPaths(src);

        System.out.println("Shortest distances from vertex " + src + ":");
        for (int i = 0; i < dist.length; i++) {
            String d = dist[i] == Integer.MAX_VALUE ? "∞" : String.valueOf(dist[i]);
            List<Integer> path = g.shortestPath(src, i);
            System.out.printf("  to %d: %s  path: %s%n", i, d, path);
        }
        // Expected:
        //   to 0: 0  path: [0]
        //   to 1: 5  path: [0, 2, 1]
        //   to 2: 2  path: [0, 2]
        //   to 3: 6  path: [0, 2, 1, 3]
        //   to 4: 7  path: [0, 2, 1, 4]
        //   to 5: 10 path: [0, 2, 1, 4, 5]
    }
}
