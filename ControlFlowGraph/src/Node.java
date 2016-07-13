import java.util.ArrayList;

public class Node {
	private int lineNumber;
	private String lineString;
	private ArrayList<Node> connectedTo;
	private int type;
	/*
	 * Types are:
	 * 0 Starting node
	 * 1 No transfer of control node
	 * 2 Transfer of control node
	 * 3 End node
	 */
	
	
	public Node(){
		lineNumber = -1;
		lineString = null;
		connectedTo = new ArrayList<Node>();
		type = -1;
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
	
	public int getType(){
		return type;
	}
	
	public void setType(int newType){
		type = newType;
	}
	
	
	public void addConnected(Node newNode){
		connectedTo.add(newNode);
	}
	
	public void printNode(){
//		System.out.println("---------------------------------------------------");
		System.out.println("This node's line number is " + lineNumber);
		System.out.println("This node's line Stirng is \"" + lineString+"\"");
		System.out.println("This node's type is " + type);
		System.out.println("---------------------------------------------------");
	}
}
