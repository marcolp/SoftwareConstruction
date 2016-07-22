
public class Node {
	protected String statmt;
	protected int lineNumber;
	protected int uniqueID;
	
	public Node(String lineOfCode, int lineNo, int unqID) {
		statmt = lineOfCode;
		lineNumber = lineNo;
		uniqueID = unqID;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public String getStatement() {
		return statmt;
	}
	public int getUniqueID() {
		return uniqueID;
	}
	
	public String toString() {
		return "\n Statement: " + statmt + "  Line Number: " + Integer.toString(lineNumber) + "  Unique ID: " + Integer.toString(uniqueID) + "\n\n";
	}
}
