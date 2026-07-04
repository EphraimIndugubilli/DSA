// LRU (Least Recently Used) Cache
// time complexity: O(1) for both get and put
// space complexity: O(capacity)
//
// Combines a HashMap with a doubly-linked list:
//   - HashMap gives O(1) key lookup (maps key -> node)
//   - Doubly-linked list maintains access order (MRU at head, LRU at tail)
//
// On every get/put the accessed node is moved to the head.
// When capacity is exceeded the tail node (LRU) is evicted.
import java.util.*;

public class LRUCache {

    private static class Node {
        int key, value;
        Node prev, next;
        Node(int k, int v) { key = k; value = v; }
    }

    private final int capacity;
    private final Map<Integer, Node> map;
    private final Node head, tail; // sentinel nodes

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    // Returns value for key, or -1 if not present. Marks key as most-recently used.
    public int get(int key) {
        Node node = map.get(key);
        if (node == null) return -1;
        moveToHead(node);
        return node.value;
    }

    // Inserts or updates key-value. Evicts the LRU entry when over capacity.
    public void put(int key, int value) {
        Node node = map.get(key);
        if (node != null) {
            node.value = value;
            moveToHead(node);
        } else {
            if (map.size() == capacity) {
                Node lru = tail.prev;
                removeNode(lru);
                map.remove(lru.key);
            }
            Node newNode = new Node(key, value);
            insertAtHead(newNode);
            map.put(key, newNode);
        }
    }

    public int size() { return map.size(); }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void insertAtHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void moveToHead(Node node) {
        removeNode(node);
        insertAtHead(node);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Cache capacity: ");
        LRUCache cache = new LRUCache(sc.nextInt());

        System.out.print("Number of operations: ");
        int ops = sc.nextInt();
        for (int i = 0; i < ops; i++) {
            System.out.print("Operation (get <key> | put <key> <value>): ");
            String op = sc.next();
            if (op.equals("get")) {
                int key = sc.nextInt();
                System.out.println("get(" + key + ") = " + cache.get(key));
            } else if (op.equals("put")) {
                int key = sc.nextInt(), val = sc.nextInt();
                cache.put(key, val);
                System.out.println("put(" + key + ", " + val + ") — size=" + cache.size());
            } else {
                System.out.println("Unknown op: " + op);
            }
        }
        sc.close();
    }
}
