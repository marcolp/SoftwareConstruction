import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class CFGParserVisitor extends JavaBaseVisitor<Node> {

  private static CommonTokenStream tokens;
  private List<Node> allNodes = new ArrayList<Node>();
  private static int ID = 0;

  public CFGParserVisitor(JavaParser parser) {
    CFGParserVisitor.tokens = (CommonTokenStream) parser.getTokenStream();
  }

  public void printNodes() {
    for (Node currentNode : this.allNodes) {
      currentNode.printNode();
    }
  }

  public Node findBreakContinue(Node currentNode, Node loopNode){

    //Inside of the block
    Node tempNode = currentNode;


    //Traverse the inside of the block and search for break/continue nodes
    while(!tempNode.isBlockEnd()){
      //      if(tempNode.getType() == Node.nodeType.CONTINUE) 
      //        tempNode.getConnectedTo().get(1);0
    }

    return tempNode;

  }
  public void linkLoopNode(Node loopNode){

    if(loopNode.getConnectedTo().isEmpty()) return;

    /*
     * Keep track of the current child. The first child will be the LOOP's 'true' child (the
     * inside of the for), which is the first child in the list (index 0)
     */
    Node childNode = loopNode.getConnectedTo().get(0);

    // The idea is to traverse all the children until we reach the last one 
    // in the block and link it to the original loop node
    while (!childNode.isBlockEnd()) {

      /*
       * If the child node is a continue node then remove its previous
       * connection because it would be connected to the next node in the
       * block and link it back to the original node
       */
      if(childNode.getType() == Node.nodeType.CONTINUE){
        childNode.removeConnected();
        childNode.addConnected(loopNode);
      }

      /*
       * If the child node is a break node then remove its previous
       * connection because it would be connected to the next node in the
       * block and link it to the outside of the loop node
       */
      else if(childNode.getType() == Node.nodeType.BREAK){
        childNode.removeConnected();
        if(loopNode.getConnectedTo().size() > 1) childNode.addConnected(loopNode.getConnectedTo().get(1));
      }

      if (childNode.getConnectedTo().size() > 1){
        findBreakContinue(childNode, loopNode);
        childNode = childNode.getConnectedTo().get(1);
      }
      else
        childNode = childNode.getConnectedTo().get(0);
    }

    //Link the last block node to the loop node 
    childNode.addConnected(loopNode);
  }

  public void linkElseIfNode(Node elseNode){

    // Find the last OUTER child, what's outside of the else if (another else if, an else, etc)
    // and link exit node of the current Node to it.
    Node exitNode = elseNode.getExitNode();
    Node lastOuterChild = elseNode.getLastOuterChild();

    // If the outer child is 
    if(lastOuterChild == null) {
      elseNode.addConnected(exitNode);
      return;
    }

    lastOuterChild.addConnected(exitNode);
  }

  /**
   * Due to the way the visitors are made, IF nodes will end up being connected to 3 nodes if
   * it has an ELSE/ELSE_IF as part of the statement. This has to do with the implementation
   * of 'visitBlock' and 'visitIfElseStmt' methods.
   * 
   * The 'if' node will be linked to the true statement (the inside of the if), the ELSE/ELSE_IF
   * node, and whatever is after the ENTIRE IF statement (if, else if's, else) in that
   * particular order.
   * 
   * This simply removes the last entry in the connection list in order to remove the wrong
   * link.
   *
   * @param ifNode
   */
  public void linkIfNode(Node ifNode){

    // Traverse all the inner children (the first child) of each node
    // in order to reach the last node inside the IF
    Node lastInnerChild = ifNode.getLastInnerChild();

    //connect that last child to the outside of the if
    int lastLinkIndex = ifNode.getConnectedTo().size()-1; //The index of the last child (the exit node index)
    Node exitNode = ifNode.getConnectedTo().get(lastLinkIndex);
    exitNode.setExitNode(true);
    lastInnerChild.addConnected(exitNode);


    if (ifNode.getConnectedTo().size() == 3) {

      // If the child is an ELSE_IF node then add the exit node to the last child in its block
      //      if (ifNode.getConnectedTo().get(1).getType() == Node.nodeType.ELSE_IF) {
      Node innerTemp = ifNode.getConnectedTo().get(1).getLastInnerChild();
      innerTemp.addConnected(exitNode);
      //      }

      //If it is not an ELSE_IF node then it is for sure an ELSE node
      //      else{
      //        Node innerTemp = ifNode.getConnectedTo().get(1).getLastInnerChild();
      //        innerTemp.addConnected(exitNode);
      //
      //      }
      ifNode.removeConnected();
    }

  }

  public void sortNodes(){
    //Sort all the nodes by their unique ID, which identifies in which order they 
    //are created.
    Collections.sort(this.allNodes, new Comparator<Node>(){
      @Override
      public int compare(Node node2, Node node1){
        if(node2.getID() < node1.getID()) return -1;

        else if(node2.getID() == node1.getID()) return 0;

        else return 1;
      }
    });
  }

  public void linkNodes() {
    sortNodes();

    // Traverse all the current nodes
    for (int i = 0; i < this.allNodes.size(); i++) {

      // Make a temporary node to keep track as we traverse
      Node currentNode = this.allNodes.get(i);

      // If the current node is an IF node
      if (currentNode.getType() == Node.nodeType.IF) {
        linkIfNode(currentNode);
      }

      // If the current node is an ELSE_IF instance
      else if (currentNode.getType() == Node.nodeType.ELSE_IF) {
        linkElseIfNode(currentNode);
      }

      // If the current node is a LOOP node
      else if (currentNode.getType() == Node.nodeType.LOOP) {
        linkLoopNode(currentNode);
      }

      // Otherwise our node is a NORMAL node
      else {
        /*
         * @TODO ??????????????
         */
      }
    }
  }

  public static void printAllTokens() {
    Token tok = CFGParserVisitor.tokens.get(0);
    int idx = tok.getTokenIndex();
    while (tok.getType() != -1) {
      System.out.println("\nThis is the token index: " + idx);
      System.out.println("The total token start index: " + tok.getStartIndex());
      System.out.println("The total token end index: " + tok.getStopIndex());
      System.out.println("This is the token string: " + tok.getText());
      System.out.println("This is the token type: " + tok.getType());
      System.out.println("This is the line number: " + tok.getLine());
      System.out
      .println("This is the token start position in line: " + tok.getCharPositionInLine());
      System.out.println(tok);
      idx++;
      tok = tokens.get(idx);
    }
  }






  /**
   * This method takes in the starting token from a given context and traverses through the tokens
   * until the given String is detected in order to stop looking for tokens. It adds them to a final
   * string variable separating them with a whitespace ' '.
   * 
   * @param tok - The starting token of the context
   * @param endChar - The indication of which token to stop at.
   * @return lineString - The tokens separated by whitespace
   */
  public String getLineParam(Token tok, String endChar) {

    int index = tok.getTokenIndex();
    String lineString = "";

    while (!tok.getText().equals(endChar)) {
      lineString += tok.getText();
      lineString += " ";
      index++;
      tok = tokens.get(index);
    }

    // Remove the last whitespace in the line string.
    // If the last character is not a ')'
    if (endChar != ")") {
      if (lineString != null && lineString.length() > 0
          && lineString.charAt(lineString.length() - 1) == ' ')
        lineString = lineString.substring(0, lineString.length() - 1);
    }
    lineString += endChar;

    return lineString;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public Node visitIfElseStmt(JavaParser.IfElseStmtContext ctx) {

    Boolean isElseIf = false;

    Token tok = ctx.getStart();
    String lineString = "";

    // If the parent context was an 'IfElseStmtContext' then this is an else instance
    if (ctx.parent.getClass().getName() == "JavaParser$IfElseStmtContext") {

      // If the parent context had 5 children and the last child was equal to the current
      // context ('IfElseStmtContext' then this is an 'else' instance. So we add an 'else' to the
      // string
      if (ctx.parent.getChildCount() == 5
          && ctx.parent.getChild(4).getClass().getName().equals(ctx.getClass().getName()))
        lineString = "else ";
      isElseIf = true;
    }

    // If the statement's first children is a block statement (meaning the
    // 'if' is using '{}') then we stop looking for tokens at the opening '{'
    String childNodeClass = ctx.statement(0).children.get(0).getClass().getName();
    if (childNodeClass.equalsIgnoreCase("JavaParser$BlockContext")) 
      lineString += getLineParam(tok, "{");

    // Otherwise, stop looking at a parenthesis
    else
      lineString += getLineParam(tok, ")");


    Node currentNode = new Node();
    currentNode.setLineNumber(ctx.getStart().getLine());
    currentNode.setID(ID);
    ID++;
    currentNode.setLineString(lineString);

    if (isElseIf)
      currentNode.setType(Node.nodeType.ELSE_IF);

    else
      currentNode.setType(Node.nodeType.IF);

    currentNode.setDepth(ctx.depth());
    currentNode.addConnected(visit(ctx.statement(0)));
    if (ctx.statement().size() > 1) {
      Node temp = visit(ctx.statement(1));
      currentNode.addConnected(temp);
    }


    // visitChildren(ctx);

    // currentNode.printNode();

    allNodes.add(currentNode);

    return currentNode;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public Node visitForStmt(JavaParser.ForStmtContext ctx) {

    Node currentNode = new Node();
    currentNode.setLineNumber(ctx.getStart().getLine());

    Token tok = ctx.getStart();
    String lineString = getLineParam(tok, "{");

    currentNode.setID(ID);
    ID++;
    currentNode.setLineString(lineString);
    currentNode.setType(Node.nodeType.LOOP);
    currentNode.setDepth(ctx.depth());
    currentNode.addConnected(visit(ctx.statement()));

    allNodes.add(currentNode);

    return currentNode;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override 
  public Node visitDoStmt(JavaParser.DoStmtContext ctx) {  
    Node currentNode = new Node();
    currentNode.setLineNumber(ctx.getStart().getLine());

    Token tok = ctx.getStart();
    String lineString = getLineParam(tok, "{");
    lineString += "} while " + ctx.parExpression().getText();


    System.out.println(ctx.parExpression().getText());

    tok.getStopIndex();
    currentNode.setLineString(lineString);
    currentNode.setType(Node.nodeType.LOOP);
    currentNode.setDepth(ctx.depth());
    currentNode.addConnected(visit(ctx.statement()));
    currentNode.setID(ID);
    ID++;
    allNodes.add(currentNode);

    return currentNode;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override 
  public Node visitSwitchStmt(JavaParser.SwitchStmtContext ctx) {
    return visitChildren(ctx); 
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public Node visitLocalVariableDeclarationStatement(JavaParser.LocalVariableDeclarationStatementContext ctx) {
    Token tok = ctx.getStart();
    String lineString = getLineParam(tok, ";");

    Node currentNode = new Node();

    currentNode.setID(ID);
    ID++;
    currentNode.setLineNumber(ctx.start.getLine());
    currentNode.setLineString(lineString);
    currentNode.setType(Node.nodeType.NORMAL);
    currentNode.setDepth(ctx.depth());

    // currentNode.printNode();

    allNodes.add(currentNode);

    return currentNode;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public Node visitWhileStmt(JavaParser.WhileStmtContext ctx) {

    Node currentNode = new Node();
    currentNode.setLineNumber(ctx.getStart().getLine());

    Token tok = ctx.getStart();
    String lineString = getLineParam(tok, "{");

    currentNode.setID(ID);
    ID++;
    currentNode.setLineString(lineString);
    currentNode.setType(Node.nodeType.LOOP);
    currentNode.setDepth(ctx.depth());
    currentNode.addConnected(visit(ctx.statement()));

    allNodes.add(currentNode);

    return currentNode;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public Node visitAssertStmt(JavaParser.AssertStmtContext ctx) {

    Token tok = ctx.getStart();
    String lineString = getLineParam(tok, "{");

    Node currentNode = new Node();
    currentNode.setID(ID);
    ID++;
    currentNode.setLineNumber(ctx.start.getLine());
    currentNode.setLineString(lineString);
    currentNode.setType(Node.nodeType.NORMAL);
    currentNode.setDepth(ctx.depth());

    // currentNode.printNode();

    visitChildren(ctx);// Probably remove this

    allNodes.add(currentNode);

    return currentNode;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.</p>
   */
  @Override public Node visitSemiStmt(JavaParser.SemiStmtContext ctx) {  

    Node currentNode = new Node();
    currentNode.setID(ID);
    ID++;
    currentNode.setLineNumber(ctx.start.getLine());
    currentNode.setLineString(ctx.getText());
    currentNode.setType(Node.nodeType.NORMAL);
    currentNode.setDepth(ctx.depth());

    // currentNode.printNode();

    visitChildren(ctx);// Probably remove this

    allNodes.add(currentNode);

    return currentNode;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public Node visitReturnStmt(JavaParser.ReturnStmtContext ctx) {

    // We do this in order to add spaces to the line strings
    // Otherwise all the tokens would be together
    Token tok = ctx.getStart();
    String lineString = getLineParam(tok, ";");

    Node currentNode = new Node();
    currentNode.setID(ID);
    ID++;
    currentNode.setLineNumber(ctx.start.getLine());
    currentNode.setLineString(lineString);
    currentNode.setType(Node.nodeType.NORMAL);
    currentNode.setDepth(ctx.depth());

    // currentNode.printNode();

    visitChildren(ctx);// Probably remove this

    allNodes.add(currentNode);

    return currentNode;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public Node visitStmtExprStmt(JavaParser.StmtExprStmtContext ctx) {
    // System.out.println(ctx.getText());
    // System.out.println("------------------");

    Node currentNode = new Node();
    currentNode.setID(ID);
    ID++;
    currentNode.setLineNumber(ctx.start.getLine());
    currentNode.setLineString(ctx.getText());
    currentNode.setType(Node.nodeType.NORMAL);
    currentNode.setDepth(ctx.depth());

    // currentNode.printNode();

    //    visitChildren(ctx);// Probably remove this

    allNodes.add(currentNode);

    return currentNode;
  }


  /**
   * This method is a helper method to avoid using returnChildren() in order to return a Node.
   */
  @Override
  public Node visitBlockStatement(JavaParser.BlockStatementContext ctx) {
    if (ctx.localVariableDeclarationStatement() != null) {
      Node node = visit(ctx.localVariableDeclarationStatement());
      return node;
    }

    else if (ctx.statement() != null) {
      Node node = visit(ctx.statement());
      return node;
    }

    else {
      Node node = visit(ctx.typeDeclaration());
      return node;
    }
    // node.printNode();
  }

  /**
   * This method is what links nodes in the same block to each other. It traverses the block
   * children linking one to the next child.
   */
  @Override
  public Node visitBlock(JavaParser.BlockContext ctx) {

    ArrayList<Node> childNodes = new ArrayList<Node>();

    int limit = ctx.getChildCount();

    // Traverse the children ignoring the {} children
    // and add the node they return to a list.
    for (int i = 1; i < limit - 1; i++) {
      Node currentNode = new Node();
      currentNode = visit(ctx.getChild(i));
      childNodes.add(currentNode);
    }

    for (int k = 0; k < childNodes.size() - 1; k++) {
      childNodes.get(k).addConnected(childNodes.get(k + 1));
    }

    childNodes.get(childNodes.size()-1).setBlockEnd(true); //The last child is at the end of the {} block

    return childNodes.get(0);
  }
}
