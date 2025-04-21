import java.util.*;

/**
 * Represents a graph linking artists and songs using adjacency lists.
 */
public class Graph {
    private final List<GraphNode> nodes;

    /**
     * Constructs an empty graph.
     */
    public Graph() {
        this.nodes = new ArrayList<>();
    }


    /**
     * Adds a new node to the graph.
     * 
     * @param name
     *            the name of the artist/song
     * @param isArtist
     *            true if the node is an artist
     * @return the created GraphNode
     */
    public GraphNode addNode(String name, boolean isArtist) {
        GraphNode node = new GraphNode(name, isArtist);
        nodes.add(node);
        return node;
    }


    /**
     * Adds an undirected edge between two nodes.
     * 
     * @param node1
     *            the first node
     * @param node2
     *            the second node
     */
    public void addEdge(GraphNode node1, GraphNode node2) {
        node1.addEdge(node2);
        node2.addEdge(node1);
    }


    /**
     * Prints basic graph statistics (node and edge counts).
     */
    public void printGraph() {
        int edgeCount = nodes.stream().mapToInt(n -> n.getEdges().size()).sum()
            / 2;
        System.out.println("Total nodes: " + nodes.size());
        System.out.println("Total edges: " + edgeCount);
    }


    /**
     * Analyzes and prints connected components information.
     */
    public void analyzeComponents() {
        Set<GraphNode> visited = new HashSet<>();
        Map<Integer, Integer> componentSizes = new HashMap<>();
        int componentId = 0;

        for (GraphNode node : nodes) {
            if (!visited.contains(node)) {
                int size = dfsComponentSize(node, visited, new HashSet<>());
                componentSizes.put(componentId++, size);
            }
        }

        int numComponents = componentSizes.size();
        int largestComponent = componentSizes.values().stream().max(
            Integer::compare).orElse(0);

        System.out.println("Number of connected components: " + numComponents);
        System.out.println("Largest component size: " + largestComponent);
    }


    /**
     * Helper method for DFS traversal to calculate component size.
     * 
     * @param node
     *            the current node being visited
     * @param visited
     *            set of all visited nodes
     * @param currentComponent
     *            nodes in the current component
     * @return size of the current component
     */
    private int dfsComponentSize(
        GraphNode node,
        Set<GraphNode> visited,
        Set<GraphNode> currentComponent) {
        visited.add(node);
        currentComponent.add(node);

        for (GraphNode neighbor : node.getEdges()) {
            if (!visited.contains(neighbor)) {
                dfsComponentSize(neighbor, visited, currentComponent);
            }
        }

        return currentComponent.size();
    }


    /**
     * Removes a node and all its associated edges from the graph.
     * 
     * @param node
     *            the node to remove
     */
    public void removeNode(GraphNode node) {
        if (node == null || !nodes.contains(node)) {
            return;
        }

        for (GraphNode neighbor : new ArrayList<>(node.getEdges())) {
            neighbor.removeEdge(node);
        }

        node.getEdges().clear();
        nodes.remove(node);
    }


    /**
     * Returns a copy of all nodes in the graph.
     * 
     * @return list of all GraphNodes
     */
    public List<GraphNode> getNodes() {
        return new ArrayList<>(nodes);
    }
}
