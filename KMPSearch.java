// time complexity: O(n + m) where n = text length, m = pattern length
// space complexity: O(m) for the failure function array
// Knuth-Morris-Pratt (KMP) finds all occurrences of a pattern in a text
// without backtracking. It pre-computes a failure function (lps array) that
// tells the automaton how far to slide the pattern on a mismatch, so each
// character is examined at most twice — once in the build pass and once in
// the search pass.
import java.util.*;

class KMPSearch {

    // Build the Longest Proper Prefix which is also Suffix (lps) array.
    // lps[i] = length of the longest proper prefix of pattern[0..i]
    // that is also a suffix of pattern[0..i].
    private static int[] buildLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0; // length of the previous longest prefix-suffix
        int i = 1;
        lps[0] = 0;  // base case: empty string

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                lps[i++] = ++len;
            } else if (len != 0) {
                // Fall back — do NOT increment i here
                len = lps[len - 1];
            } else {
                lps[i++] = 0;
            }
        }
        return lps;
    }

    // Search for all occurrences of pattern in text.
    // Returns a list of 0-indexed starting positions.
    public static List<Integer> search(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m == 0) return result;

        int[] lps = buildLPS(pattern);
        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }
            if (j == m) {
                result.add(i - j); // match found at index (i - j)
                j = lps[j - 1];   // slide pattern using failure function
            } else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1]; // use failure function to avoid re-checking
                } else {
                    i++;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter text: ");
        String text = sc.nextLine();

        System.out.print("Enter pattern: ");
        String pattern = sc.nextLine();

        List<Integer> matches = search(text, pattern);

        if (matches.isEmpty()) {
            System.out.println("Pattern not found in text.");
        } else {
            System.out.println("Pattern found at index/indices: " + matches);
            System.out.println("Total occurrences: " + matches.size());
        }

        sc.close();
    }
}
