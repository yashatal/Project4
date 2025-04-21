import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for GraphNode class.
 */
public class GraphNodeTest {
    private GraphNode artist;
    private GraphNode song;

    @Before
    public void setUp() {
        artist = new GraphNode("Artist", true);
        song = new GraphNode("Song", false);
    }


    /**
     * Tests edge addition between different node types.
     */
    @Test
    public void testAddEdgeBetweenTypes() {
        artist.addEdge(song);
        assertTrue("Artist should have song edge", artist.getEdges().contains(
            song));
        assertTrue("Song should have artist edge", song.getEdges().contains(
            artist));
    }


    /**
     * Tests self-edge prevention.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddSelfEdge() {
        artist.addEdge(artist); // Should throw exception
    }


    /**
     * Tests edge removal symmetry.
     */
    @Test
    public void testRemoveEdgeSymmetry() {
        artist.addEdge(song);
        artist.removeEdge(song);

        assertFalse("Artist should not have edge", artist.getEdges().contains(
            song));
        assertFalse("Song should not have edge", song.getEdges().contains(
            artist));
    }
}
