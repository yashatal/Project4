/**
 * Represents an entry in the hash table, storing a key and its associated
 * GraphNode.
 **/
public class HashEntry {
    private final String key;
    private final GraphNode node;
    private boolean isActive;

    /**
     * Constructs a new HashEntry.
     * 
     * @param key
     *            the artist/song name
     * @param node
     *            the associated GraphNode
     */
    public HashEntry(String key, GraphNode node) {
        this.key = key;
        this.node = node;
        this.isActive = true;
    }


    /**
     * Gets the key of this entry.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }


    /**
     * Gets the GraphNode of this entry.
     * 
     * @return the associated GraphNode
     */
    public GraphNode getNode() {
        return node;
    }


    /**
     * Checks if this entry is active.
     * 
     * @return true if active, false if marked inactive
     */
    public boolean isActive() {
        return isActive;
    }


    /**
     * Sets the active status of this entry.
     * 
     * @param active
     *            the new active status
     */
    public void setActive(boolean active) {
        isActive = active;
    }
}
