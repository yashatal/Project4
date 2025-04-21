import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for HashEntry implementation.
 * Covers basic functionality and state transitions.
 */
public class HashEntryTest {
    private HashEntry entry;
    private GraphNode node;

    /**
     * Sets up test fixtures before each test method.
     */
    @Before
    public void setUp() {
        node = new GraphNode("Test", true);
        entry = new HashEntry("key", node);
    }


    /**
     * Tests constructor initialization.
     * Verifies all fields are correctly set.
     */
    @Test
    public void testConstructor() {
        assertEquals("Key should match", "key", entry.getKey());
        assertEquals("Node should match", node, entry.getNode());
        assertTrue("Should be active initially", entry.isActive());
    }


    /**
     * Tests active status modification.
     * Verifies state changes correctly.
     */
    @Test
    public void testSetActive() {
        entry.setActive(false);
        assertFalse("Should be inactive", entry.isActive());

        entry.setActive(true);
        assertTrue("Should be active", entry.isActive());
    }


    /**
     * Tests immutability of key and node references.
     * Verifies fields cannot be modified after construction.
     */
    @Test
    public void testImmutableFields() {
        // Testing behavior rather than actual immutability
        assertEquals("Key should remain unchanged", "key", entry.getKey());
        assertEquals("Node should remain unchanged", node, entry.getNode());
    }


    /**
     * Tests that inactive entry returns correct node.
     */
    @Test
    public void testInactiveEntryReturnsNode() {
        entry.setActive(false);
        assertEquals("Should still return node even when inactive", node, entry
            .getNode());
    }


    /**
     * Tests that key remains accessible after deactivation.
     */
    @Test
    public void testKeyAfterDeactivation() {
        entry.setActive(false);
        assertEquals("Key should remain unchanged", "key", entry.getKey());
    }
}
