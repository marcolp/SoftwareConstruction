
public class SingleStatementNode extends Node {

    private Node nextNode;
    
    public SingleStatementNode(String beginStatement, int beginLineNumber, int unqID) {
        super(beginStatement, beginLineNumber, unqID);
        
        nextNode = super.nextNode;
    }
    
    public void setNextNode(Node nxtNode) {
        this.nextNode = nxtNode;
    }
    
    public Node getNextNode() {
        return this.nextNode;
    }
    
    public String toString()
    {
        return "\n SINGSTMT Statement: " + this.statmt + "  Line Number: " + Integer.toString(this.lineNumber) + "  Unique ID: " + Integer.toString(this.uniqueID) + "  Next Node: " + this.nextNode + "\n\n";
    }

}