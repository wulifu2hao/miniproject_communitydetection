package GirmanNewmanClustering;

//try to finalize the variable because it is a static graph.
public class Edge{	
    private int destinationNodeId;
    private int sourceNodeId;
    
    public Edge(final int source, final int destination) {
        sourceNodeId = source;
        destinationNodeId = destination;
    }
    public int getSourceNodeID() {
        return sourceNodeId;
    }
    public int getDestinationNodeID() {
        return destinationNodeId;
    }
    /*
     * Check if this Edge is an outgoing edge of the given node.
     * @param n The node to test
     * @return True if it's an outgoing edge otherwise false
     */
    public boolean isOutgoingEdge(final Node node) {
        return (sourceNodeId == node.getId());
    }
    /*
     * Check if this Edge is an incoming edge of the given node.
     * @param n The node to test
     * @return True if it's an incoming edge otherwise false
     */
    public boolean isIncomingEdge(final Node node) {
        return (destinationNodeId == node.getId());
    }


 
}
