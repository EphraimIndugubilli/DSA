// time complexity: O(V + E)
// space complexity: O(V + E)
// Topological Sort orders the vertices of a Directed Acyclic Graph (DAG) such
// that for every directed edge u -> v, u appears before v in the ordering.
// Implemented using Kahn's algorithm (BFS on in-degrees):
//   1. Compute in-degree of every vertex.
//   2. Push all vertices with in-degree 0 into a queue.
//   3. Repeatedly pop a vertex, append it to the result, and decrement the
//      in-degree of its neighbors, pushing any that drop to 0.
//   4. If fewer than V vertices are processed, the graph has a cycle and no
//      topological order exists.
import java.util.*;

class TopologicalSort {
    private int vertices;
    private List<List<Integer>> adj;

    public TopologicalSort(int vertices) {
        this.vertices = vertices;
        this.adj = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adj.add(new ArrayList<>());
        }
    }

    // Directed edge u -> v (u must come before v)
    public void addEdge(int u, int v) {
        adj.get(u).add(v);
    }

    // Returns a valid topological order, or an empty list if the graph has a cycle
    public List<Integer> sort() {
        int[] inDegree = new int[vertices];
        for (int u = 0; u < vertices; u++) {
            for (int v : adj.get(u)) {
                inDegree[v]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] == 0) queue.add(i);
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            for (int v : adj.get(u)) {
                if (--inDegree[v] == 0) queue.add(v);
            }
        }

        if (order.size() != vertices) {
            return new ArrayList<>(); // cycle detected, no valid topological order
        }
        return order;
    }

    public boolean hasCycle() {
        return sort().isEmpty() && vertices > 0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of vertices: ");
        int v = sc.nextInt();
        System.out.print("Enter number of directed edges: ");
        int e = sc.nextInt();

        TopologicalSort graph = new TopologicalSort(v);
        System.out.println("Enter each edge as 'u v' (meaning u -> v, 0-indexed):");
        for (int i = 0; i < e; i++) {
            int u = sc.nextInt(), w = sc.nextInt();
            graph.addEdge(u, w);
        }

        List<Integer> order = graph.sort();
        if (order.isEmpty() && v > 0) {
            System.out.println("Graph has a cycle — no valid topological order exists.");
        } else {
            System.out.println("Topological order: " + order);
        }

        sc.close();
    }
}
