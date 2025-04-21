import java.util.Arrays;

/**
 * An extensible hash table implementation that uses quadratic probing for
 * collision resolution.
 * The table automatically resizes when the load factor exceeds 50% and uses
 * prime-sized
 * tables for better key distribution. Supports insertion, search, and removal
 * operations
 * with average-case O(1) time complexity.
 *
 * @author Yash Atal
 * @version 2.0
 */
public class ExtensibleHashTable {
    /** The underlying array of hash entries */
    private HashEntry[] table;

    /** The number of active entries in the table */
    private int size;

    /** The initial capacity specified during construction */
    private final int initialCapacity;

    /** The load factor threshold that triggers resizing (0.5 = 50%) */
    private static final double LOAD_FACTOR_THRESHOLD = 0.5;

    /** Maximum number of probing attempts before giving up */
    private static final int MAX_PROBE_ATTEMPTS = 1000;

    /**
     * Constructs a new, empty hash table with the specified initial capacity.
     *
     * @param capacity
     *            the initial capacity of the hash table
     * @throws IllegalArgumentException
     *             if the initial capacity is not positive
     */
    public ExtensibleHashTable(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "Initial capacity must be positive");
        }
        this.table = new HashEntry[nextPrime(capacity)];
        this.initialCapacity = capacity;
        this.size = 0;
    }


    /**
     * Inserts the specified key-value pair into the hash table. If the key
     * already exists,
     * replaces the existing value and returns the previous value.
     *
     * @param key
     *            the key to be inserted
     * @param node
     *            the value to be associated with the key
     * @return the previous value associated with the key, or null if the key
     *         was not present
     * @throws IllegalArgumentException
     *             if either key or node is null
     */
    public GraphNode insert(String key, GraphNode node) {
        if (key == null || node == null) {
            throw new IllegalArgumentException(
                "Neither key nor value can be null");
        }

        // Check if we need to resize BEFORE inserting
        if ((double)(size + 1) / table.length >= LOAD_FACTOR_THRESHOLD) {
            resize();
        }

        int index = findSlotForInsert(key);
        if (table[index] == null || !table[index].isActive()) {
            table[index] = new HashEntry(key, node);
            size++;
            return null;
        }
        else {
            GraphNode oldNode = table[index].getNode();
            table[index] = new HashEntry(key, node);
            return oldNode;
        }
    }


    /**
     * Returns the value associated with the specified key.
     *
     * @param key
     *            the key whose associated value is to be returned
     * @return the value associated with the key, or null if the key is not
     *         present
     */
    public GraphNode search(String key) {
        if (key == null) {
            return null;
        }

        int index = findSlotForSearch(key);
        if (table[index] == null || !table[index].isActive()) {
            return null;
        }
        return table[index].getNode();
    }


    /**
     * Removes the key-value pair associated with the specified key.
     *
     * @param key
     *            the key whose mapping is to be removed
     * @return the value previously associated with the key, or null if the key
     *         was not present
     */
    public GraphNode remove(String key) {
        if (key == null) {
            return null;
        }

        int index = findSlotForSearch(key);
        if (table[index] == null || !table[index].isActive()) {
            return null;
        }

        table[index].setActive(false);
        size--;
        GraphNode removedNode = table[index].getNode();

        rehashAfterRemoval(index);
        return removedNode;
    }


    /**
     * Returns the number of active entries in the hash table.
     *
     * @return the number of active entries
     */
    public int size() {
        return size;
    }


    /**
     * Returns the current capacity of the hash table.
     *
     * @return the current table capacity
     */
    public int capacity() {
        return table.length;
    }


    /**
     * Prints the contents of the hash table for debugging purposes.
     *
     * @param type
     *            a label describing the type of contents being printed
     */
    public void printContents(String type) {
        System.out.println("Total " + type + " nodes: " + size);
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && table[i].isActive()) {
                System.out.println(i + ": " + table[i].getKey());
            }
        }
    }


    /**
     * Returns a copy of the internal table for testing purposes.
     *
     * @return a defensive copy of the internal hash table array
     */
    HashEntry[] getTable() {
        return Arrays.copyOf(table, table.length);
    }


    /**
     * Determines if the table should be resized based on current load factor.
     *
     * @return true if the table should be resized, false otherwise
     */
    private boolean shouldResize() {
        return (double)size / table.length >= LOAD_FACTOR_THRESHOLD;
    }


    /**
     * Finds the appropriate slot for insertion using quadratic probing.
     *
     * @param key
     *            the key to locate a slot for
     * @return the index of the slot for the key
     * @throws IllegalStateException
     *             if no empty slot is found after maximum attempts
     */
    private int findSlotForInsert(String key) {
        int home = Hash.h(key, table.length);
        int index = home;

        for (int i = 1; i < MAX_PROBE_ATTEMPTS; i++) {
            if (table[index] == null || !table[index].isActive()) {
                return index;
            }
            if (table[index].getKey().equals(key)) {
                return index;
            }
            index = (home + i * i) % table.length;
            if (index < 0)
                index += table.length;
        }
        throw new IllegalStateException("Failed to find slot after "
            + MAX_PROBE_ATTEMPTS + " attempts");
    }


    /**
     * Finds the appropriate slot for search using quadratic probing.
     *
     * @param key
     *            the key to locate a slot for
     * @return the index of the slot for the key
     */
    private int findSlotForSearch(String key) {
        int home = Hash.h(key, table.length);
        int index = home;

        for (int i = 1; i < MAX_PROBE_ATTEMPTS; i++) {
            if (table[index] == null) {
                return index;
            }
            if (table[index].getKey().equals(key) && table[index].isActive()) {
                return index;
            }
            index = (home + i * i) % table.length;
            if (index < 0)
                index += table.length;
        }
        return home; // Fallback to home position
    }


    /**
     * Doubles the size of the hash table (to next prime) and rehashes all
     * active entries.
     */
    private void resize() {
        HashEntry[] oldTable = table;
        table = new HashEntry[nextPrime(oldTable.length * 2)];
        size = 0;

        for (HashEntry entry : oldTable) {
            if (entry != null && entry.isActive()) {
                int index = findSlotForInsert(entry.getKey());
                table[index] = entry;
                size++;
            }
        }
    }


    /**
     * Rehashes entries after removal to maintain probe sequence integrity.
     *
     * @param removedIndex
     *            the index where removal occurred
     */
    private void rehashAfterRemoval(int removedIndex) {
        int current = (removedIndex + 1) % table.length;
        while (table[current] != null) {
            HashEntry entry = table[current];
            int home = Hash.h(entry.getKey(), table.length);

            if (shouldRehash(home, removedIndex, current)) {
                table[current] = null;
                size--;
                int newIndex = findSlotForInsert(entry.getKey());
                table[newIndex] = entry;
                size++;
            }
            current = (current + 1) % table.length;
        }
    }


    /**
     * Determines if an entry should be rehashed after removal.
     *
     * @param home
     *            the home position of the entry
     * @param removedIndex
     *            the index where removal occurred
     * @param currentIndex
     *            the current index being examined
     * @return true if the entry should be rehashed, false otherwise
     */
    boolean shouldRehash(int home, int removedIndex, int currentIndex) {
        return (currentIndex > home && (removedIndex >= home
            && removedIndex < currentIndex)) || (currentIndex < home
                && (removedIndex >= home || removedIndex < currentIndex));
    }


    /**
     * Returns the smallest prime number greater than or equal to n.
     *
     * @param n
     *            the starting number
     * @return the next prime number
     */
    private int nextPrime(int n) {
        if (n <= 1)
            return 2;
        while (!isPrime(n)) {
            n++;
        }
        return n;
    }


    /**
     * Checks if a number is prime.
     *
     * @param n
     *            the number to check
     * @return true if the number is prime, false otherwise
     */
    private boolean isPrime(int n) {
        if (n <= 1)
            return false;
        if (n <= 3)
            return true;
        if (n % 2 == 0 || n % 3 == 0)
            return false;
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
        return true;
    }
}
