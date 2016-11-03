import java.util.HashMap;
import java.util.HashSet;
public class Edge{

    private static final int HASH_INIT = 5381;	//The initial value of the hash from hashCode.
    private static final int HASH_MULTIPLY = 33;	//Number to multiply the hash with.33 is the usual one used with bernsteins hash.
    private Node destinationNode;
    private Node sourceNode;
    private Double Weight = null;

    public Edge() {
        
    }

    /**
     * Initialize a new Edge-Object with an source- and destination-node.
     * @param source The node where this edge comes from.
     * @param destination The node where this edge points to.
     */
    public Edge(final Node source, final Node destination) {
        this();
        sourceNode = source;
        destinationNode = destination;

        try {
            if (source != null) {
                source.addEdge(this);
            }
            if (destination != null) {
                destination.addEdge(this);
            }
        } catch (Exception e) {
            /* TODO: add logging */
        }
    }
    /**
     * Returns the source node.
     * @return The source node
     */
    public Node getSourceNode() {
        return sourceNode;
    }

    /**
     * Returns the destination node.
     * @return the destination node
     */
    public Node getDestinationNode() {
        return destinationNode;
    }
    /**
     * Sets the destination node of the edge.
     *
     * @param destinationNode the destination node.
     */
    private void setDestinationNode(final Node destinationNode) {
        this.destinationNode = destinationNode;
    }

    /**
     * Sets the source node of the edge.
     *
     * @param sourceNode the source node
     */
    private void setSourceNode(final Node sourceNode) {
        this.sourceNode = sourceNode;
    }

    /**
     * Check if this Edge is an outgoing edge of the given node.
     * The comparison is based on the references, not on the content;
     * @param n The node to test
     * @return True if it's an outgoing edge otherwise false
     */
    public boolean isOutgoingEdge(final Node n) {
        return (sourceNode != null && sourceNode == n);
    }

    /**
     * Check if this Edge is an incoming edge of the given node.
     * The comparison is based on the references, not on the content;
     * @param n The node to test
     * @return True if it's an incoming edge otherwise false
     */
    public boolean isIncomingEdge(final Node n) {
        return (destinationNode != null && destinationNode == n);
    }

    /**
     * Two edges are equals if their destination and source edes match.
     * @param o The object to check
     * @return True if equals, otherwise false.
     */
    public boolean equals(final Object o) {
        if (o instanceof Edge) {
            Edge e = (Edge) o;
            return e.hashCode() == hashCode();
        }
        return false;
    }

 
}
