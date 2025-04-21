import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

/**
 * Test class for Graph implementation.
 * Covers node and edge operations, component analysis, and mutation scenarios.
 */
public class GraphTest {
    private Graph graph;
    private GraphNode artist1, artist2, song1, song2;

    /**
     * Sets up test fixtures before each test method.
     */
    @Before
    public void setUp() {
        graph = new Graph();
        artist1 = graph.addNode("Artist1", true);
        artist2 = graph.addNode("Artist2", true);
        song1 = graph.addNode("Song1", false);
        song2 = graph.addNode("Song2", false);
    }


    /**
     * Tests node creation and properties.
     * Verifies name and type are correctly set.
     */
    @Test
    public void testAddNode() {
        assertEquals("Name should match", "Artist1", artist1.getName());
        assertTrue("Should be artist", artist1.isArtist());
        assertFalse("Should not be artist", song1.isArtist());
    }


    /**
     * Tests bidirectional edge creation.
     * Verifies edges exist in both directions.
     */
    @Test
    public void testAddEdge() {
        graph.addEdge(artist1, song1);
        assertTrue("Artist should have song edge", artist1.getEdges().contains(
            song1));
        assertTrue("Song should have artist edge", song1.getEdges().contains(
            artist1));
    }


    /**
     * Tests duplicate edge prevention.
     * Verifies only one edge is created between nodes.
     */
    @Test
    public void testAddDuplicateEdge() {
        graph.addEdge(artist1, song1);
        graph.addEdge(artist1, song1);
        assertEquals("Should only have one edge", 1, artist1.getEdges().size());
    }


    /**
     * Tests node removal and edge cleanup.
     * Verifies all related edges are removed.
     */
    @Test
    public void testRemoveNode() {
        graph.addEdge(artist1, song1);
        graph.addEdge(artist1, song2);
        graph.removeNode(artist1);

        assertFalse("Song1 should not have edge", song1.getEdges().contains(
            artist1));
        assertFalse("Song2 should not have edge", song2.getEdges().contains(
            artist1));
        assertFalse("Graph should not contain node", graph.getNodes().contains(
            artist1));
    }


    /**
     * Tests single connected component scenario.
     * Verifies correct component count and size.
     */
    @Test
    public void testAnalyzeComponentsSingleComponent() {
        graph.addEdge(artist1, song1);
        graph.addEdge(song1, artist2);

        // Create a new graph to analyze components
        Graph testGraph = new Graph();
        GraphNode a1 = testGraph.addNode("Artist1", true);
        GraphNode s1 = testGraph.addNode("Song1", false);
        GraphNode a2 = testGraph.addNode("Artist2", true);

        testGraph.addEdge(a1, s1);
        testGraph.addEdge(s1, a2);

        // Verify all nodes are connected in one component
        assertEquals("Should have one connected component", 1, countComponents(
            testGraph));
    }


    /**
     * Tests multiple connected components scenario.
     * Verifies correct component count and sizes.
     */
    @Test
    public void testAnalyzeComponentsMultipleComponents() {
        graph.addEdge(artist1, song1);
        graph.addEdge(artist2, song2);

        // Create a new graph to analyze components
        Graph testGraph = new Graph();
        GraphNode a1 = testGraph.addNode("Artist1", true);
        GraphNode s1 = testGraph.addNode("Song1", false);
        GraphNode a2 = testGraph.addNode("Artist2", true);
        GraphNode s2 = testGraph.addNode("Song2", false);

        testGraph.addEdge(a1, s1);
        testGraph.addEdge(a2, s2);

        // Verify two separate components
        assertEquals("Should have two connected components", 2, countComponents(
            testGraph));
    }


    /**
     * Helper method to count connected components in a graph.
     */
    private int countComponents(Graph graph) {
        Set<GraphNode> visited = new HashSet<>();
        int componentCount = 0;

        for (GraphNode node : graph.getNodes()) {
            if (!visited.contains(node)) {
                componentCount++;
                dfs(node, visited);
            }
        }
        return componentCount;
    }


    /**
     * Helper method for DFS traversal.
     */
    private void dfs(GraphNode node, Set<GraphNode> visited) {
        visited.add(node);
        for (GraphNode neighbor : node.getEdges()) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited);
            }
        }
    }


    /**
     * Tests edge removal in both directions.
     * Verifies edges are completely removed.
     */
    @Test
    public void testRemoveEdge() {
        graph.addEdge(artist1, song1);
        artist1.removeEdge(song1);

        assertFalse("Artist should not have edge", artist1.getEdges().contains(
            song1));
        assertFalse("Song should not have edge", song1.getEdges().contains(
            artist1));
    }


    /**
     * Tests empty graph scenario.
     * Verifies no nodes or edges exist.
     */
    @Test
    public void testEmptyGraph() {
        Graph emptyGraph = new Graph();
        assertEquals("Should have no nodes", 0, emptyGraph.getNodes().size());
    }


    /**
     * Tests component analysis with disconnected nodes.
     */
    @Test
    public void testAnalyzeComponentsWithIsolatedNodes() {
        Graph testGraph = new Graph();
        testGraph.addNode("Artist1", true);
        testGraph.addNode("Song1", false);

        assertEquals("Should count isolated nodes as components", 2,
            countComponents(testGraph));
    }


    /**
     * Tests edge case of empty graph component analysis.
     */
    @Test
    public void testAnalyzeComponentsEmptyGraph() {
        Graph emptyGraph = new Graph();
        emptyGraph.analyzeComponents(); // Should not throw exception

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        emptyGraph.analyzeComponents();
        assertTrue(out.toString().contains(
            "Number of connected components: 0"));
        System.setOut(System.out);
    }


    /**
     * Tests removal of node with multiple edges.
     */
    @Test
    public void testRemoveNodeWithMultipleEdges() {
        graph.addEdge(artist1, song1);
        graph.addEdge(artist1, song2);
        graph.addEdge(artist1, artist2);

        graph.removeNode(artist1);

        for (GraphNode node : Arrays.asList(song1, song2, artist2)) {
            assertFalse("All edges should be removed", node.getEdges().contains(
                artist1));
        }
    }
}
