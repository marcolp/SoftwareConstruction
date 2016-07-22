public class IfElseNode extends Node {

    private Node trueNode;
    private Node falseNode;
    private boolean isElseIf; 
    
   
    public IfElseNode(String beginStatement, int beginLineNumber, int unqID, Node beginTrueNode, Node beginFalseNode) {
        super(beginStatement, beginLineNumber, unqID);
        
        this.trueNode = beginTrueNode;
        this.falseNode = beginFalseNode;
        this.isElseIf = false;
    }
    
    public IfElseNode(String beginStatement, int beginLineNumber, int unqID, Node beginTrueNode) {
      super(beginStatement, beginLineNumber, unqID);
      
      this.trueNode = beginTrueNode;
      this.falseNode = super.nextNode;
      this.isElseIf = false;
  }
    
    public void setTrueNode(Node inTrueNode)
    {
        this.trueNode = inTrueNode;
    }
    
    public void setFalseNode(Node inFalseNode)
    {
        this.falseNode = inFalseNode;
    }
    
    public Node getTrueNode() {
        return trueNode;
    }
    
    public Node getFalseNode() {
        return falseNode;
    }
    
    public boolean isElseIf() {
      return isElseIf;
    }

    public void setElseIf(boolean isElseIf) {
      this.isElseIf = isElseIf;
    }

    public String toString()
    {
        return "IFELSE Statement: " + this.statmt + "  Line Number: " + Integer.toString(this.lineNumber) + "  Unique ID: " + Integer.toString(this.uniqueID) + "  True Node: " + this.trueNode + "  False Node: " + this.falseNode + "\n\n";
    }

}