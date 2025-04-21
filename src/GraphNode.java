import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in the graph that can be either an artist or a song.
 * Each node maintains a list of edges to connected nodes.
 */
public class GraphNode {
    private final String name;
    private final boolean isArtist;
    private final List<GraphNode> edges;

    /**
     * Constructs a new GraphNode.
     * 
     * @param name
     *            the name of the artist or song
     * @param isArtist
     *            true if the node represents an artist
     * @throws IllegalArgumentException
     *             if name is null or empty
     */
    public GraphNode(String name, boolean isArtist) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
        this.isArtist = isArtist;
        this.edges = new ArrayList<>();
    }


    /**
     * Adds a bidirectional edge to another node.
     * 
     * @param node
     *            the node to connect to
     * @throws IllegalArgumentException
     *             if node is null or trying to create a self-edge
     */
    public void addEdge(GraphNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }
        if (node == this) {
            throw new IllegalArgumentException("Cannot create self-edge");
        }
        if (!edges.contains(node)) {
            edges.add(node);
            node.edges.add(this); // Add reciprocal edge
        }
    }


    /**
     * Removes an edge to another node (bidirectional).
     * 
     * @param node
     *            the node to disconnect from
     */
    public void removeEdge(GraphNode node) {
        edges.remove(node);
        node.edges.remove(this);
    }


    /**
     * Gets the name of this node.
     * 
     * @return the node's name
     */
    public String getName() {
        return name;
    }


    /**
     * Checks if this node represents an artist.
     * 
     * @return true if this is an artist node
     */
    public boolean isArtist() {
        return isArtist;
    }


    /**
     * Gets all edges from this node.
     * 
     * @return list of connected nodes
     */
    public List<GraphNode> getEdges() {
        return new ArrayList<>(edges); // Return a defensive copy
    }
}
