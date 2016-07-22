
public class LoopNode extends Node {

    private Node beginLoopNode;
    private Node endLoopNode;
    
    public LoopNode(String beginStatement, int beginLineNumber, int unqID) {
        super(beginStatement, beginLineNumber, unqID);
        beginLoopNode = new Node();
        endLoopNode = super.nextNode;
    }
    
    public void setBeginLoopNode(Node bgnLoopNode) {
        beginLoopNode = bgnLoopNode;
    }
    
    public void setEndLoopNode(Node enLoopNode) {
        endLoopNode = enLoopNode;
    }
    
    public Node getBeginLoopNode() {
        return beginLoopNode;
    }
    
    public Node getEndLoopNode() {
        return endLoopNode;
    }

}