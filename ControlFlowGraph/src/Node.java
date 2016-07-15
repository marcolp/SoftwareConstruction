import java.util.ArrayList;

public class Node {
	
	public enum nodeType {
		IF, NORMAL, LOOP;
	}
	
	private int ID;
	private int lineNumber;
	private String lineString;
	private ArrayList<Node> connectedTo;
	private nodeType type;
	private int antlrDepth;

	
	public Node(){
		ID = -1;
		lineNumber = -1;
		lineString = null;
		connectedTo = new ArrayList<Node>();
		type = nodeType.NORMAL;
		antlrDepth = -1;
	}
	
	public int getID(){
		return ID;
	}
	
	public void setID(int newNumber){
		ID = newNumber;
	}
	
	public int getLineNumber(){
		return lineNumber;
	}
	
	public void setLineNumber(int newNumber){
		lineNumber = newNumber;
	}
	
	public String getLineString(){
		return lineString;
	}
	
	public void setLineString(String newString){
		lineString = newString;
	}
	
	public ArrayList<Node> getConnectedTo(){
		return connectedTo;
	}
	
	public void setConnectedTo(ArrayList<Node> newList){
		connectedTo = newList;
	}
	
	public nodeType getType(){
		return type;
	}
	
	public void setType(nodeType newType){
		type = newType;
	}
	
	public int getDepth(){
		return antlrDepth;
	}
	
	public void setDepth(int depth){
		antlrDepth = depth;
	}
	
	public void addConnected(Node newNode){
		connectedTo.add(newNode);
	}
	
	//Removes and returns the last
	//Child added (the last index connected)
	public Node removeConnected(){
		return connectedTo.remove(connectedTo.size()-1);
	}
	
	public void printNode(){
//		System.out.println("---------------------------------------------------");
		System.out.println("This node's ID is: "+ID);
		System.out.println("This node's line number is " + lineNumber);
		System.out.println("This node's line Stirng is \"" + lineString+"\"");
//		System.out.println("This node's type is " + type);
//		System.out.println("This node's antlr depth is: "+ antlrDepth);
		if(!connectedTo.isEmpty()){
			for(Node currentNode : connectedTo){
				if(currentNode != null)
				System.out.println("This node is connected to Node with ID: "+currentNode.ID);
			}
		}
		System.out.println("---------------------------------------------------");
	}
}
