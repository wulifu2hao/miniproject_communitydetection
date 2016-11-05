package GirmanNewmanClustering;

//try to finalize the variable because it is a static graph.
public class Edge{	
    private int destinationNodeId;
    private int sourceNodeId;
    private double betweenness=0.0;
    
    public Edge(final int source, final int destination) {
        this.sourceNodeId = source;
        this.destinationNodeId = destination;
    }
    public int getSourceNodeID() {
        return sourceNodeId;
    }
    public int getDestinationNodeID() {
        return destinationNodeId;
    }
    public double getBetweenness() {
        return betweenness;
    }
    public void increaseBetweennessByOne() {
        betweenness++;
    }
    
}
