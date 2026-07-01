// time complexity: O(E log V) where E = edges, V = grid cells (same as Dijkstra with a priority queue)
// space complexity: O(V) for the open set, closed set, and g-score table
// A* Search finds the shortest path on a weighted grid using a heuristic to guide exploration.
// It expands the node with the lowest f(n) = g(n) + h(n) where:
//   g(n) = exact cost from start to n
//   h(n) = heuristic estimate from n to goal (Manhattan distance — admissible on a 4-directional grid)
// When h(n) is admissible (never overestimates), A* is guaranteed to find the optimal path.
// A* with h=0 reduces to Dijkstra's; with h=infinity it reduces to Greedy Best-First Search.
import java.util.*;

class AStarSearch {

    record Cell(int row, int col) {}

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};

    // Manhattan distance — admissible heuristic for 4-directional grids
    private static int heuristic(Cell a, Cell b) {
        return Math.abs(a.row() - b.row()) + Math.abs(a.col() - b.col());
    }

    /**
     * Finds the shortest path from start to goal on a grid.
     *
     * @param grid  2-D boolean array: true = passable, false = wall
     * @param start source cell
     * @param goal  destination cell
     * @return ordered list of cells from start to goal (inclusive), or empty list if no path exists
     */
    public static List<Cell> search(boolean[][] grid, Cell start, Cell goal) {
        int rows = grid.length, cols = grid[0].length;

        // g[r][c] = best known cost from start to (r,c)
        int[][] g = new int[rows][cols];
        for (int[] row : g) Arrays.fill(row, Integer.MAX_VALUE);
        g[start.row()][start.col()] = 0;

        // parent map for path reconstruction
        Map<Cell, Cell> parent = new HashMap<>();

        // Min-heap ordered by f = g + h
        PriorityQueue<Cell> open = new PriorityQueue<>(
            Comparator.comparingInt(c -> g[c.row()][c.col()] + heuristic(c, goal))
        );
        open.add(start);

        Set<Cell> closed = new HashSet<>();

        while (!open.isEmpty()) {
            Cell cur = open.poll();
            if (cur.equals(goal)) return reconstruct(parent, goal);
            if (!closed.add(cur)) continue; // already expanded with a better path

            for (int d = 0; d < 4; d++) {
                int nr = cur.row() + DR[d], nc = cur.col() + DC[d];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                if (!grid[nr][nc]) continue; // wall
                Cell next = new Cell(nr, nc);
                if (closed.contains(next)) continue;

                int tentative = g[cur.row()][cur.col()] + 1; // uniform step cost
                if (tentative < g[nr][nc]) {
                    g[nr][nc] = tentative;
                    parent.put(next, cur);
                    open.add(next); // re-add with updated priority
                }
            }
        }
        return Collections.emptyList(); // no path found
    }

    private static List<Cell> reconstruct(Map<Cell, Cell> parent, Cell goal) {
        LinkedList<Cell> path = new LinkedList<>();
        for (Cell c = goal; c != null; c = parent.get(c)) path.addFirst(c);
        return path;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter grid rows and cols: ");
        int rows = sc.nextInt(), cols = sc.nextInt();

        boolean[][] grid = new boolean[rows][cols];
        System.out.println("Enter grid row by row (1=passable, 0=wall):");
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = sc.nextInt() == 1;

        System.out.print("Enter start (row col): ");
        Cell start = new Cell(sc.nextInt(), sc.nextInt());
        System.out.print("Enter goal  (row col): ");
        Cell goal  = new Cell(sc.nextInt(), sc.nextInt());

        List<Cell> path = search(grid, start, goal);

        if (path.isEmpty()) {
            System.out.println("No path found.");
        } else {
            System.out.println("Path length: " + (path.size() - 1) + " steps");
            System.out.println("Path: " + path.stream()
                .map(c -> "(" + c.row() + "," + c.col() + ")")
                .reduce((a, b) -> a + " -> " + b).orElse(""));

            // Print grid with path marked as '*'
            char[][] vis = new char[rows][cols];
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++)
                    vis[r][c] = grid[r][c] ? '.' : '#';
            for (Cell cell : path) vis[cell.row()][cell.col()] = '*';
            vis[start.row()][start.col()] = 'S';
            vis[goal.row()][goal.col()] = 'G';
            System.out.println("Grid (* = path, S = start, G = goal, # = wall):");
            for (char[] row : vis) System.out.println(new String(row));
        }
        sc.close();
    }
}
