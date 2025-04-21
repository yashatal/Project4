import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test class for ExtensibleHashTable implementation.
 * Covers all public methods, edge cases, and internal behaviors including
 * resizing, collision handling, and removal scenarios.
 * 
 * @author Your Name
 * @version 2.0
 */
public class ExtensibleHashTableTest {
    private ExtensibleHashTable table;
    private GraphNode node1, node2, node3;

    /**
     * Sets up test fixtures before each test method execution.
     * Initializes a new hash table with capacity 10 and test nodes.
     */
    @Before
    public void setUp() {
        table = new ExtensibleHashTable(10);
        node1 = new GraphNode("Artist1", true);
        node2 = new GraphNode("Artist2", true);
        node3 = new GraphNode("Artist3", true);
    }


    /**
     * Tests basic insertion and retrieval of key-value pairs.
     * Verifies that inserted values can be retrieved and that
     * inserting new keys returns null.
     */
    @Test
    public void testInsertAndSearch() {
        assertNull("Should return null for new key", table.insert("key1",
            node1));
        assertEquals("Should find inserted node", node1, table.search("key1"));
    }


    /**
     * Tests that inserting a duplicate key returns the previous value
     * and replaces it in the table.
     */
    @Test
    public void testInsertDuplicateKey() {
        table.insert("key1", node1);
        assertEquals("Should return old node", node1, table.insert("key1",
            node2));
        assertEquals("Should store new node", node2, table.search("key1"));
    }


    /**
     * Tests that attempting to insert a null key throws
     * IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullKey() {
        table.insert(null, node1);
    }


    /**
     * Tests that attempting to insert a null value throws
     * IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullValue() {
        table.insert("key1", null);
    }


    /**
     * Tests that searching for a non-existent key returns null.
     */
    @Test
    public void testSearchNonExistentKey() {
        assertNull("Should return null for non-existent key", table.search(
            "nonexistent"));
    }


    /**
     * Tests that searching for a null key returns null.
     */
    @Test
    public void testSearchNullKey() {
        assertNull("Should return null for null key", table.search(null));
    }


    /**
     * Tests collision handling with quadratic probing.
     * Uses known colliding keys to verify probing works correctly.
     */
    @Test
    public void testSearchAfterCollision() {
        String key1 = "a";
        String key2 = "k";
        assertEquals("Keys should collide", Hash.h(key1, 10), Hash.h(key2, 10));

        table.insert(key1, node1);
        table.insert(key2, node2);

        assertEquals("Should find first node", node1, table.search(key1));
        assertEquals("Should find second node", node2, table.search(key2));
    }


    /**
     * Tests removal of existing entries.
     * Verifies that removed entries are no longer findable.
     */
    @Test
    public void testRemoveExistingKey() {
        table.insert("key1", node1);
        assertEquals("Should return removed node", node1, table.remove("key1"));
        assertNull("Should not find removed key", table.search("key1"));
    }


    /**
     * Tests that removing a non-existent key returns null.
     */
    @Test
    public void testRemoveNonExistentKey() {
        assertNull("Should return null for non-existent key", table.remove(
            "nonexistent"));
    }


    /**
     * Tests that removing a null key returns null.
     */
    @Test
    public void testRemoveNullKey() {
        assertNull("Should return null for null key", table.remove(null));
    }


    /**
     * Tests that the table properly resizes when load factor exceeds threshold.
     */
    @Test
    public void testAutoResize() {
        ExtensibleHashTable smallTable = new ExtensibleHashTable(4);
        assertEquals("Initial capacity should be prime", 5, smallTable
            .capacity());

        // Insert 3 items (60% load factor)
        smallTable.insert("a", node1);
        smallTable.insert("b", node2);
        smallTable.insert("c", node3); // Should trigger resize

        assertTrue("Table should have resized", smallTable.capacity() > 5);
        assertEquals("Should maintain all entries after resize", node1,
            smallTable.search("a"));
    }


    /**
     * Tests that size is properly maintained after insertions and removals.
     */
    @Test
    public void testSizeMaintenance() {
        assertEquals("Initial size should be 0", 0, table.size());
        table.insert("key1", node1);
        assertEquals("Size should increment after insert", 1, table.size());
        table.remove("key1");
        assertEquals("Size should decrement after remove", 0, table.size());
    }


    /**
     * Tests rehashing behavior after removal.
     * Verifies other entries remain accessible after removal.
     */
    @Test
    public void testRemoveWithRehashing() {
        table.insert("a", node1);
        table.insert("k", node2); // Collision with "a"
        table.insert("b", node3);

        assertEquals("Should remove first node", node1, table.remove("a"));
        assertEquals("Should still find collided node", node2, table.search(
            "k"));
        assertEquals("Should still find third node", node3, table.search("b"));
    }


    /**
     * Tests that tables maintain prime number capacities.
     */
    @Test
    public void testPrimeSizedTables() {
        ExtensibleHashTable table = new ExtensibleHashTable(7);
        assertTrue("Initial capacity should be prime", isPrime(table
            .capacity()));

        // Trigger resize
        for (int i = 0; i < 4; i++) {
            table.insert("key" + i, new GraphNode("Artist" + i, true));
        }

        assertTrue("Resized capacity should be prime", isPrime(table
            .capacity()));
    }


    /**
     * Tests insertion at exact load factor boundary.
     */
    @Test
    public void testInsertAtLoadFactorBoundary() {
        ExtensibleHashTable smallTable = new ExtensibleHashTable(5);
        // Insert 2 items (40% load factor)
        smallTable.insert("a", node1);
        smallTable.insert("b", node2);
        // Next insert (60%) should trigger resize
        smallTable.insert("c", node3);
        assertTrue("Table should have resized", smallTable.capacity() > 5);
    }


    /**
     * Tests behavior after multiple insertions and removals.
     */
    @Test
    public void testMultipleInsertRemoveOperations() {
        table.insert("a", node1);
        table.insert("b", node2);
        table.remove("a");
        table.insert("c", node3);
        table.remove("b");

        assertNull("Removed node should not be found", table.search("a"));
        assertEquals("Existing node should be found", node3, table.search("c"));
        assertEquals("Size should reflect active entries", 1, table.size());
    }


    /**
     * Helper method to check if a number is prime.
     * 
     * @param n
     *            number to check
     * @return true if prime, false otherwise
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


    @Test
    public void testResizeWithTombstones() {
        table.insert("a", node1);
        table.remove("a"); // Leaves tombstone
        // Fill table to trigger resize with tombstone
        for (int i = 0; i < 10; i++) {
            table.insert("key" + i, new GraphNode("Node" + i, true));
        }
        assertNull("Tombstone shouldn't survive resize", table.search("a"));
    }


    @Test
    public void testQuadraticProbingWraparound() {
        // Force collision and probe sequence that wraps around table
        String key1 = "a";
        String key2 = "k"; // Collides in small table
        ExtensibleHashTable smallTable = new ExtensibleHashTable(5);
        smallTable.insert(key1, node1);
        smallTable.insert(key2, node2); // Should probe
        assertEquals(node2, smallTable.search(key2));
    }


    @Test
    public void testShouldRehashEdgeCases() {
        // Setup: Force a scenario where rehashing should occur
        ExtensibleHashTable table = new ExtensibleHashTable(5);
        table.insert("a", node1); // home slot = h("a",5)
        table.insert("k", node2); // collides with "a", probes to next slot
        table.remove("a"); // Creates a tombstone at home slot

        // Verify rehashing logic
        assertTrue("Should rehash due to tombstone in probe path", table
            .shouldRehash(Hash.h("k", 5), Hash.h("a", 5),
                /* currentIndex= */ (Hash.h("k", 5) + 1) % 5));
    }


    @Test
    public void testProbingWraparound() {
        ExtensibleHashTable table = new ExtensibleHashTable(5);
        // Force collisions until probing wraps around the table
        table.insert("a", node1); // home = h("a",5)
        table.insert("k", node2); // same home, probes to (home + 1) % 5
        table.insert("x", node3); // same home, probes to (home + 4) % 5 (wraps
                                  // around)

        assertEquals(node3, table.search("x"));
    }
}
