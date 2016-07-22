
public class ForNode extends Node {

	private Node beginLoopNode;
	private Node endLoopNode;
	
	public ForNode(String beginStatement, int beginLineNumber, int unqID) {
		super(beginStatement, beginLineNumber, unqID);
		beginLoopNode = new Node("-1", -1,-1);
		endLoopNode = new Node("-1", -1, -1);
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
