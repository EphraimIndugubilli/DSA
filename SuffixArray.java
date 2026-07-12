// time complexity: O(n log² n) construction, O(n) LCP (Kasai's algorithm)
// space complexity: O(n)
// A Suffix Array stores the sorted order of all suffixes of a string.
// Combined with the LCP (Longest Common Prefix) array it enables:
//   - O(m log n) exact pattern search (m = pattern length)
//   - O(1) LCP queries between any two suffixes (with sparse-table RMQ)
//   - Efficient duplicate / longest-repeated-substring detection
// Construction uses prefix-doubling (DC3/SA-IS runs in O(n) but this is
// simpler to read and fast enough for strings up to ~10^6 characters).
import java.util.*;

class SuffixArray {
    private final int   n;
    private final int[] sa;    // sa[i]  = start index of the i-th smallest suffix
    private final int[] rank;  // rank[i] = position of suffix i in sorted order
    private final int[] lcp;   // lcp[i]  = LCP length between sa[i-1] and sa[i]

    public SuffixArray(String s) {
        n    = s.length();
        sa   = new int[n];
        rank = new int[n];
        lcp  = new int[n];
        build(s);
        buildLCP(s);
    }

    // ── Prefix-doubling construction ──────────────────────────────
    private void build(String s) {
        int[] tmp = new int[n];

        // Initial rank = character code
        for (int i = 0; i < n; i++) { sa[i] = i; rank[i] = s.charAt(i); }

        for (int gap = 1; gap < n; gap <<= 1) {
            final int g = gap;
            final int[] r = rank.clone();

            // Compare two suffixes by (rank[i], rank[i+gap])
            Comparator<Integer> cmp = (a, b) -> {
                if (r[a] != r[b]) return Integer.compare(r[a], r[b]);
                int ra = (a + g < n) ? r[a + g] : -1;
                int rb = (b + g < n) ? r[b + g] : -1;
                return Integer.compare(ra, rb);
            };

            // Sort suffix indices using the current (rank, rank+gap) key
            Integer[] box = new Integer[n];
            for (int i = 0; i < n; i++) box[i] = sa[i];
            Arrays.sort(box, cmp);
            for (int i = 0; i < n; i++) sa[i] = box[i];

            // Recompute dense ranks from sorted order
            tmp[sa[0]] = 0;
            for (int i = 1; i < n; i++)
                tmp[sa[i]] = tmp[sa[i - 1]] + (cmp.compare(sa[i], sa[i - 1]) != 0 ? 1 : 0);
            System.arraycopy(tmp, 0, rank, 0, n);

            if (rank[sa[n - 1]] == n - 1) break; // all ranks unique — done early
        }
    }

    // ── Kasai's O(n) LCP array construction ──────────────────────
    private void buildLCP(String s) {
        int[] inv = new int[n]; // inverse SA: inv[sa[i]] = i
        for (int i = 0; i < n; i++) inv[sa[i]] = i;

        int h = 0;
        for (int i = 0; i < n; i++) {
            if (inv[i] > 0) {
                int j = sa[inv[i] - 1];
                while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) h++;
                lcp[inv[i]] = h;
                if (h > 0) h--;
            }
        }
    }

    // ── Accessors ─────────────────────────────────────────────────
    public int[]   getSA()   { return sa.clone(); }
    public int[]   getLCP()  { return lcp.clone(); }
    public int     size()    { return n; }

    // ── Pattern search: first occurrence in O(m log n) ───────────
    public int search(String text, String pattern) {
        int lo = 0, hi = n - 1, m = pattern.length();
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            String suffix = text.substring(sa[mid], Math.min(sa[mid] + m, n));
            int cmp = suffix.compareTo(pattern);
            if      (cmp < 0) lo = mid + 1;
            else if (cmp > 0) hi = mid - 1;
            else              return sa[mid]; // first match found (not necessarily leftmost)
        }
        return -1; // not found
    }

    // ── Count pattern occurrences in O(m log n) ──────────────────
    public int count(String text, String pattern) {
        int m = pattern.length();

        // Lower bound
        int lo = 0, hi = n - 1, left = n;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            String suffix = text.substring(sa[mid], Math.min(sa[mid] + m, n));
            if (suffix.compareTo(pattern) < 0) lo = mid + 1;
            else { left = mid; hi = mid - 1; }
        }

        // Upper bound
        lo = 0; hi = n - 1;
        int right = -1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            String suffix = text.substring(sa[mid], Math.min(sa[mid] + m, n));
            if (suffix.compareTo(pattern) <= 0) { right = mid; lo = mid + 1; }
            else hi = mid - 1;
        }

        return (right >= left) ? right - left + 1 : 0;
    }

    // ── Longest repeated substring via max LCP value ─────────────
    public String longestRepeatedSubstring(String text) {
        int maxLen = 0, idx = 0;
        for (int i = 1; i < n; i++) {
            if (lcp[i] > maxLen) { maxLen = lcp[i]; idx = sa[i]; }
        }
        return maxLen == 0 ? "" : text.substring(idx, idx + maxLen);
    }

    // ── Driver ────────────────────────────────────────────────────
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter string: ");
        String text = sc.next();

        SuffixArray sufArr = new SuffixArray(text);
        int[] sa  = sufArr.getSA();
        int[] lcp = sufArr.getLCP();

        System.out.println("\nSuffix Array:");
        for (int i = 0; i < sa.length; i++)
            System.out.printf("  [%2d] rank=%2d  lcp=%2d  \"%s\"%n",
                sa[i], i, lcp[i], text.substring(sa[i]));

        String lrs = sufArr.longestRepeatedSubstring(text);
        System.out.println("\nLongest Repeated Substring: \"" + lrs + "\"");

        System.out.print("\nEnter pattern to search: ");
        String pat = sc.next();
        int pos   = sufArr.search(text, pat);
        int count = sufArr.count(text, pat);
        if (pos >= 0)
            System.out.printf("Pattern \"%s\" found %d time(s); one occurrence at index %d.%n", pat, count, pos);
        else
            System.out.printf("Pattern \"%s\" not found.%n", pat);

        sc.close();
    }
}
