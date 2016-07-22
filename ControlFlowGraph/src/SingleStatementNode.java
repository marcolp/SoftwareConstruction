
public class SingleStatementNode extends Node {

	private Node nextNode;
	
	public SingleStatementNode(String beginStatement, int beginLineNumber, int unqID) {
		super(beginStatement, beginLineNumber, unqID);
		
		nextNode = new Node("-1", -1, -1);
	}
	
	public void setNextNode(Node nxtNode) {
		nextNode = nxtNode;
	}
	
	public Node getNextNode() {
		return nextNode;
	}
	
	public String toString()
	{
		return "\n SINGSTMT Statement: " + statmt + "  Line Number: " + Integer.toString(lineNumber) + "  Unique ID: " + Integer.toString(uniqueID) + "  Next Node: " + nextNode + "\n\n";
	}

}
