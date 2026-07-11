// time complexity: O(n) — each character is a potential palindrome center, expanded at most once
// space complexity: O(n) for the transformed string and radius array
// Manacher's Algorithm finds the longest palindromic substring in linear time.
// Key insight: transform "abc" → "#a#b#c#" so every palindrome has an odd length,
// then exploit previously computed radii to skip redundant expansions using a
// right-boundary mirror trick.
import java.util.Scanner;

class Manacher {

    // Returns the longest palindromic substring of s.
    public static String longestPalindrome(String s) {
        if (s == null || s.isEmpty()) return "";

        // Transform s into t: "abc" → "^#a#b#c#$"
        // Sentinel characters ^ and $ prevent out-of-bounds checks.
        StringBuilder sb = new StringBuilder("^#");
        for (char c : s.toCharArray()) { sb.append(c); sb.append('#'); }
        sb.append('$');
        String t = sb.toString();
        int n = t.length();

        // p[i] = radius of the longest palindrome centred at t[i]
        int[] p = new int[n];
        int c = 0;  // centre of the palindrome with the current rightmost boundary
        int r = 0;  // right boundary of that palindrome (exclusive)

        for (int i = 1; i < n - 1; i++) {
            int mirror = 2 * c - i;  // mirror of i around c

            if (i < r) {
                // i is within the current right boundary — use mirror's radius
                // as a lower bound to avoid unnecessary expansions.
                p[i] = Math.min(r - i, p[mirror]);
            }

            // Attempt to expand the palindrome centred at i.
            while (t.charAt(i + p[i] + 1) == t.charAt(i - p[i] - 1)) {
                p[i]++;
            }

            // If this palindrome expands past r, update the centre and boundary.
            if (i + p[i] > r) {
                c = i;
                r = i + p[i];
            }
        }

        // Find the maximum radius and its centre in the transformed string.
        int maxLen = 0, centreIdx = 0;
        for (int i = 1; i < n - 1; i++) {
            if (p[i] > maxLen) { maxLen = p[i]; centreIdx = i; }
        }

        // Map back to the original string: start = (centreIdx - maxLen - 1) / 2
        int start = (centreIdx - maxLen - 1) / 2;
        return s.substring(start, start + maxLen);
    }

    // Returns the length of the longest palindromic substring (O(n) query).
    public static int longestPalindromeLength(String s) {
        return longestPalindrome(s).length();
    }

    // Returns all palindromic substrings of length >= minLen in O(n).
    public static void printAllPalindromes(String s, int minLen) {
        if (s == null || s.isEmpty()) return;
        StringBuilder sb = new StringBuilder("^#");
        for (char c : s.toCharArray()) { sb.append(c); sb.append('#'); }
        sb.append('$');
        String t = sb.toString();
        int n = t.length();
        int[] p = new int[n];
        int c = 0, r = 0;
        for (int i = 1; i < n - 1; i++) {
            int mirror = 2 * c - i;
            if (i < r) p[i] = Math.min(r - i, p[mirror]);
            while (t.charAt(i + p[i] + 1) == t.charAt(i - p[i] - 1)) p[i]++;
            if (i + p[i] > r) { c = i; r = i + p[i]; }
            // p[i] is the radius in the transformed string; actual length = p[i]
            if (p[i] >= minLen) {
                int start = (i - p[i] - 1) / 2;
                System.out.println("  \"" + s.substring(start, start + p[i]) + "\" (len=" + p[i] + ", start=" + start + ")");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter string: ");
        String s = sc.nextLine().trim();

        String lp = longestPalindrome(s);
        System.out.println("Longest palindromic substring : \"" + lp + "\" (length " + lp.length() + ")");

        System.out.println("All palindromic substrings of length >= 2:");
        printAllPalindromes(s, 2);

        sc.close();
    }
}
