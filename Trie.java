// time complexity: O(m) per insert/search/startsWith where m = key length
// space complexity: O(ALPHABET_SIZE * m * n) where n = number of keys
// A Trie (prefix tree) stores strings character by character so that all keys
// sharing a common prefix share the same path from the root. This makes prefix
// lookups, autocomplete, and exact-match queries all O(m) regardless of how
// many keys are stored — far faster than sorting + binary search for prefix work.
import java.util.*;

class Trie {

    private static final int ALPHABET = 26;

    private static class Node {
        Node[] children = new Node[ALPHABET];
        boolean isEnd;
    }

    private final Node root = new Node();

    // Insert a lowercase ASCII word into the trie in O(m)
    public void insert(String word) {
        Node cur = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (cur.children[idx] == null) cur.children[idx] = new Node();
            cur = cur.children[idx];
        }
        cur.isEnd = true;
    }

    // Return true if the exact word exists in the trie in O(m)
    public boolean search(String word) {
        Node node = find(word);
        return node != null && node.isEnd;
    }

    // Return true if any word in the trie starts with prefix in O(m)
    public boolean startsWith(String prefix) {
        return find(prefix) != null;
    }

    // Return all words stored under the given prefix (empty list if none) in O(p + output)
    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        Node node = find(prefix);
        if (node != null) collectWords(node, new StringBuilder(prefix), results);
        return results;
    }

    // Delete a word from the trie; returns false if the word was not present
    public boolean delete(String word) {
        return deleteHelper(root, word, 0);
    }

    // Count total words stored in the trie in O(total characters)
    public int countWords() {
        int[] count = {0};
        countHelper(root, count);
        return count[0];
    }

    // ── Private helpers ───────────────────────────────────────────

    private Node find(String key) {
        Node cur = root;
        for (char c : key.toCharArray()) {
            int idx = c - 'a';
            if (cur.children[idx] == null) return null;
            cur = cur.children[idx];
        }
        return cur;
    }

    private void collectWords(Node node, StringBuilder sb, List<String> out) {
        if (node.isEnd) out.add(sb.toString());
        for (int i = 0; i < ALPHABET; i++) {
            if (node.children[i] != null) {
                sb.append((char) ('a' + i));
                collectWords(node.children[i], sb, out);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

    private boolean deleteHelper(Node node, String word, int depth) {
        if (depth == word.length()) {
            if (!node.isEnd) return false;
            node.isEnd = false;
            return isLeaf(node);
        }
        int idx = word.charAt(depth) - 'a';
        if (node.children[idx] == null) return false;
        boolean shouldDelete = deleteHelper(node.children[idx], word, depth + 1);
        if (shouldDelete) {
            node.children[idx] = null;
            return !node.isEnd && isLeaf(node);
        }
        return false;
    }

    private boolean isLeaf(Node node) {
        for (Node child : node.children) if (child != null) return false;
        return true;
    }

    private void countHelper(Node node, int[] count) {
        if (node.isEnd) count[0]++;
        for (Node child : node.children) if (child != null) countHelper(child, count);
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        Scanner sc = new Scanner(System.in);

        System.out.println("Trie Demo — commands: insert <word>, search <word>, prefix <pfx>, autocomplete <pfx>, delete <word>, count, quit");
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+", 2);
            String cmd = parts[0].toLowerCase();
            String arg = parts.length > 1 ? parts[1].toLowerCase() : "";

            switch (cmd) {
                case "insert"       -> { trie.insert(arg); System.out.println("Inserted: " + arg); }
                case "search"       -> System.out.println("Found: " + trie.search(arg));
                case "prefix"       -> System.out.println("Has prefix \"" + arg + "\": " + trie.startsWith(arg));
                case "autocomplete" -> System.out.println("Completions for \"" + arg + "\": " + trie.autocomplete(arg));
                case "delete"       -> System.out.println("Deleted \"" + arg + "\": " + trie.delete(arg));
                case "count"        -> System.out.println("Word count: " + trie.countWords());
                case "quit"         -> { System.out.println("Bye."); sc.close(); return; }
                default             -> System.out.println("Unknown command: " + cmd);
            }
        }
        sc.close();
    }
}
