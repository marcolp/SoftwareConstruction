import java.util.ArrayList;

public class Node {

  public enum nodeType {
    IF, NORMAL, LOOP, ELSE_IF, ELSE;
  }

  private int ID;
  private int lineNumber;
  private String lineString;
  private ArrayList<Node> connectedTo;
  private nodeType type;
  private int antlrDepth;
  private boolean isExitNode; // This means that this is where an 'if'/'for'/etc ends up in



  public Node() {
    ID = -1;
    lineNumber = -1;
    lineString = null;
    connectedTo = new ArrayList<Node>();
    type = nodeType.NORMAL;
    antlrDepth = -1;
    isExitNode = false;
  }

  public int getID() {
    return this.ID;
  }

  public void setID(int newNumber) {
    this.ID = newNumber;
  }

  public int getLineNumber() {
    return this.lineNumber;
  }

  public void setLineNumber(int newNumber) {
    this.lineNumber = newNumber;
  }

  public String getLineString() {
    return this.lineString;
  }

  public void setLineString(String newString) {
    this.lineString = newString;
  }

  public ArrayList<Node> getConnectedTo() {
    return this.connectedTo;
  }

  public void setConnectedTo(ArrayList<Node> newList) {
    this.connectedTo = newList;
  }

  public nodeType getType() {
    return this.type;
  }

  public void setType(nodeType newType) {
    this.type = newType;
  }

  public int getDepth() {
    return this.antlrDepth;
  }

  public void setDepth(int depth) {
    this.antlrDepth = depth;
  }

  public boolean isExitNode() {
    return this.isExitNode;
  }

  public void setExitNode(boolean condition) {
    this.isExitNode = condition;
  }

  public void addConnected(Node newNode) {
    this.connectedTo.add(newNode);
  }

  // Removes and returns the last
  // Child added (the last index connected)
  public Node removeConnected() {
    return this.connectedTo.remove(this.connectedTo.size() - 1);
  }

  public void printNode() {
    // System.out.println("---------------------------------------------------");
    System.out.println("This node's ID is: " + this.ID);
    // System.out.println("This node's line number is " + this.lineNumber);
    System.out.println("This node's line Stirng is \"" + lineString + "\"");
    System.out.println("This node's type is " + this.type);
    // System.out.println("This node's antlr depth is: "+ this.antlrDepth);
//    System.out.println("This node's isExitNode value: "+this.isExitNode);
    if (!connectedTo.isEmpty()) {
      for (Node currentNode : connectedTo) {
        if (currentNode != null)
          System.out.println("This node is connected to Node with ID: " + currentNode.ID);
      }
    }
    System.out.println("---------------------------------------------------");
  }

  public Node getLastInnerChild() {
    Node currentNode = new Node();
    if (!this.connectedTo.isEmpty())
      currentNode = this.connectedTo.get(0);

    else
      System.out.println("Trying to traverse a node with no children");

    while (!currentNode.getConnectedTo().isEmpty()) {
      currentNode = currentNode.getConnectedTo().get(0);
    }
    return currentNode;
  }
  
  public Node getLastOuterChild() {
    Node currentNode = new Node();
    if (this.connectedTo.size() > 1)
      currentNode = this.connectedTo.get(1);

    else
      System.out.println("Trying to traverse a node with not enough children");

    while (!currentNode.getConnectedTo().isEmpty()) {
      currentNode = currentNode.getConnectedTo().get(0);
    }
    return currentNode;
  }
  
  public Node getExitNode(){
    Node currentNode;
    if(!this.connectedTo.isEmpty())
      currentNode = this.connectedTo.get(0);
    
    else{
      System.out.println("Trying to traverse a node with no children");
      return null;
    }
    
    while(!currentNode.connectedTo.isEmpty()){
      if(currentNode.isExitNode() == true) return currentNode;
      currentNode = currentNode.getConnectedTo().get(0);
    }
    
    return currentNode;
  }
}
