import java.util.ArrayList;

public class Node {

  public enum nodeType {
    IF, NORMAL, LOOP, ELSE_IF, SWITCH, BREAK, CONTINUE, TAG;
  }

  private int ID;
  private int lineNumber;
  private String lineString;
  private ArrayList<Node> connectedTo; //Node at index 0 should be the inside of a block, index 1 should be outside the block
  private nodeType type;
  private boolean isExitNode; // This means that this is where an 'if'/'for'/etc ends up in
  private boolean isBlockEnd; // This means that this node is the last one in a {} block




  public Node() {
    ID = -1;
    lineNumber = -1;
    lineString = null;
    connectedTo = new ArrayList<Node>();
    type = nodeType.NORMAL;
    isExitNode = false;
    isBlockEnd = false;
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

  public boolean isExitNode() {
    return this.isExitNode;
  }

  public void setExitNode(boolean condition) {
    this.isExitNode = condition;
  }

  public boolean isBlockEnd() {
    return isBlockEnd;
  }

  public void setBlockEnd(boolean isBlockEnd) {
    this.isBlockEnd = isBlockEnd;
  }

  public void addConnected(Node newNode) {
    this.connectedTo.add(newNode);
  }

  // Removes and returns the last
  // Child added (the last index connected)
  public Node removeConnected() {
    if(!this.connectedTo.isEmpty())
      return this.connectedTo.remove(this.connectedTo.size() - 1);

    else
      return null;
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

  /**
   * Get the last child in the block 
   * @return
   */
  public Node getLastInnerChild() {

    if (this.connectedTo.isEmpty()){
      //      System.out.println("Trying to traverse a node with no children");
      return this;
    }

    Node currentNode;
    currentNode = this.getConnectedTo().get(0);

    while (!currentNode.isBlockEnd && !currentNode.getConnectedTo().isEmpty()) 
      if(currentNode.connectedTo.size() > 1)
        currentNode = currentNode.getConnectedTo().get(1);

      else
        currentNode = currentNode.getConnectedTo().get(0);

    return currentNode;
  }


  /**
   * If the current node has a child that has more 
   * than 1 child (e.g. a LOOP node, IF node, etc) then find the 
   * last inner child of that node.
   * 
   * @return The 
   */
  public Node getLastOuterChild() {
    Node currentChildNode;
    if (this.connectedTo.size() > 1)
      currentChildNode = this.connectedTo.get(1);

    else{
//      System.out.println("Trying to traverse a node with not enough children");
      return null;
    }

    return currentChildNode.getLastInnerChild();

  }


  public Node getExitNode(){
    Node currentNode;
    if(!this.connectedTo.isEmpty())
      currentNode = this.connectedTo.get(0);

    else{
//      System.out.println("Trying to traverse a node with no children");
      return null;
    }

    while(!currentNode.connectedTo.isEmpty()){
      if(currentNode.isExitNode() == true) return currentNode;
      if(currentNode.connectedTo.size() > 1)
        currentNode = currentNode.getConnectedTo().get(1);

      else
        currentNode = currentNode.getConnectedTo().get(0);
      //      currentNode = currentNode.getConnectedTo().get(0);
    }

    return currentNode;
  }
  
}