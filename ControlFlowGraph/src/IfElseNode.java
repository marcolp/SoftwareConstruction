
public class IfElseNode extends Node {

	private Node trueNode;
	private Node falseNode;
	
	public IfElseNode(String beginStatement, int beginLineNumber, int unqID, Node beginTrueNode, Node beginFalseNode) {
		super(beginStatement, beginLineNumber, unqID);
		
		trueNode = beginTrueNode;
		falseNode = beginFalseNode;
	}
	
	public void setTrueNode(Node inTrueNode)
	{
		trueNode = inTrueNode;
	}
	
	public void setFalseNode(Node inFalseNode)
	{
		falseNode = inFalseNode;
	}
	
	public Node getTrueNode() {
		return trueNode;
	}
	
	public Node getFalseNode() {
		return falseNode;
	}
	
	public String toString()
	{
		return "\n IFELSE Statement: " + statmt + "  Line Number: " + Integer.toString(lineNumber) + "  Unique ID: " + Integer.toString(uniqueID) + "  True Node: " + trueNode + "  False Node: " + falseNode + "\n\n";
	}

}
