import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class CFGParserVisitor extends JavaBaseVisitor<Node> {

  private static CommonTokenStream tokens;
  private static List<Node> allNodes = new ArrayList<Node>();
  private static int ID = 0;

  public CFGParserVisitor(JavaParser parser) {
    CFGParserVisitor.tokens = (CommonTokenStream) parser.getTokenStream();
  }

  public static void printNodes() {
    for (Node currentNode : allNodes) {
      System.out.println(currentNode.toString());
    }
  }

  public static void linkNodes() {

    //Sort all the nodes by their unique ID, which identifies in which order they 
    //are created.
    Collections.sort(allNodes, new Comparator<Node>(){
      @Override
      public int compare(Node node2, Node node1){
        if(node2.getUniqueID() < node1.getUniqueID()) return -1;
        
        else if(node2.getUniqueID() == node1.getUniqueID()) return 0;
        
        else return 1;
      }
    });
    
    // Traverse all the current nodes
    for (int i = 0; i < allNodes.size(); i++) {

      // Make a temporary node to keep track as we traverse
      Node currentNode = new Node();
      currentNode = allNodes.get(i);

      // If the current node is an IF node
      if (currentNode instanceof IfElseNode) { //this check is very slow

        if (currentNode.getConnectedTo().size() == 3) {

          // Traverse all the inner children (the first child) of each node
          // in order to reach the last node inside the if
          Node temp = new Node();
          temp = currentNode.getLastInnerChild();
          
          //connect that last child to the outside of the if
          Node exitNode = currentNode.getConnectedTo().get(2);
          exitNode.setExitNode(true);
//          temp.addConnected(currentNode.getConnectedTo().get(2));
          temp.addConnected(exitNode);
          
          //Do the same with the inner of the ELSE_IF node
          if (currentNode.getConnectedTo().get(1).getType() == Node.nodeType.ELSE_IF) {
            Node innerTemp = new Node();
            innerTemp = currentNode.getConnectedTo().get(1).getLastInnerChild();
            innerTemp.addConnected(exitNode);
          }

          currentNode.removeConnected();
          currentNode.getExitNode();
        }

      }
      // If the current node is an ELSE_IF instance
      else if (currentNode.getType() == Node.nodeType.ELSE_IF) {
        
        //Find the last OUTER child, what's outside of the else if (another else if, an else, etc)
        // and link exit node of the current Node to it.
        Node exitNode = currentNode.getExitNode();
        Node lastOuterChild = currentNode.getLastOuterChild();
        lastOuterChild.addConnected(exitNode);
      }


      // If the current node is a LOOP node
      else if (currentNode instanceof LoopNode) {

        /*
         * Keep track of the current child. The first child will be the LOOP's 'true' child (the
         * inside of the for), which is the first child in the list (index 0)
         */
        Node childNode = new Node();
        childNode = currentNode.getConnectedTo().get(0);

        // The idea is to traverse all the children until we reach the last one and link it to the
        // original loop node
        while (childNode != null) {

          /*
           * If the current child has no connections then it means it is the last child
           * 
           * @TODO What happens when the last child is an IF or a LOOP?
           */
          if (childNode.getConnectedTo().size() == 0) {
            childNode.addConnected(currentNode);
            break;
          }

          /*
           * If the current child has more than 1 child, visit the second one because this means
           * that it is a LOOP or IF statement. In which case the first child is the inside of the
           * block which we don't want.
           */
          if (childNode.getConnectedTo().size() > 1)
            childNode = childNode.getConnectedTo().get(1);

          // Otherwise get the first child.
          else
            childNode = childNode.getConnectedTo().get(0);

        }

        // Otherwise our node is a NORMAL node
      } else {
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
      if (lineString != null && lineString.length() > 0 && lineString.charAt(lineString.length() - 1) == ' ')
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
    // 'if' is using '{}') then we stop looking for tokens at the opening {
    if (ctx.statement(0).children.get(0).getClass().getName()
        .equalsIgnoreCase("JavaParser$BlockContext"))
      lineString += getLineParam(tok, "{");

    // Otherwise, stop looking at a parenthesis
    else
      lineString += getLineParam(tok, ")");


    int id = ID;
    ID++;
    
    int lineNumber = ctx.start.getLine();
     

 
    Node currentNode;
    Node trueBranch = visit(ctx.statement(0));
    if (ctx.statement().size() > 1) {
      Node falseBranch = visit(ctx.statement(1));
      currentNode = new IfElseNode(lineString, lineNumber, id, trueBranch, falseBranch);    
    }
    
    else{
      currentNode = new IfElseNode(lineString, lineNumber, id, trueBranch);    
    }

    if (isElseIf){
      ((IfElseNode) currentNode).setElseIf(true);
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

    int id = ID;
    ID++;
    
    int lineNumber = ctx.start.getLine();
    
    Token tok = ctx.getStart();
    String lineString = getLineParam(tok, "{");
    Node beginLoop = visit(ctx.statement());
    
    LoopNode currentNode = new LoopNode(lineString, lineNumber, id);
    currentNode.setBeginLoopNode(beginLoop);

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

    int id = ID;
    ID++;
    
    int lineNumber = ctx.start.getLine();
    
    Token tok = ctx.getStart();
    String lineString = getLineParam(tok, "{");
    Node beginLoop = visit(ctx.statement());

    LoopNode currentNode = new LoopNode(lineString, lineNumber, id);
    currentNode.setBeginLoopNode(beginLoop);

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
  public Node visitLocalVariableDeclarationStatement(JavaParser.LocalVariableDeclarationStatementContext ctx) {
  
    int id = ID;
    ID++;
    
    int lineNumber = ctx.start.getLine();
    
    Token tok = ctx.getStart();
    String lineString = getLineParam(tok, ";");
  
    Node currentNode = new SingleStatementNode(lineString, lineNumber, id);
  
    // currentNode.printNode();
    
    visitChildren(ctx); //probably remove this.
  
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

    int id = ID;
    ID++;
    
    int lineNumber = ctx.start.getLine();
    
    Token tok = ctx.getStart();
    String lineString = getLineParam(tok, ";");

    Node currentNode = new SingleStatementNode(lineString, lineNumber, id);

    // currentNode.printNode();

    visitChildren(ctx);  // Probably remove this

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

    int id = ID;
    ID++;
    
    int lineNumber = ctx.start.getLine();
    
    Token tok = ctx.getStart();
    String lineString = getLineParam(tok, ";");

    Node currentNode = new SingleStatementNode(lineString, lineNumber, id);

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

    int id = ID;
    ID++;
    
    int lineNumber = ctx.start.getLine();
    String lineText = ctx.getText();
    
    Node currentNode = new SingleStatementNode(lineText, lineNumber, id);

    visitChildren(ctx);// Probably remove this

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
      
      if(i != 1) currentNode.setExitNode(true); //Sibling nodes are possible exit nodes
      
      currentNode = visit(ctx.getChild(i));
      childNodes.add(currentNode);
    }

    for (int k = 0; k < childNodes.size() - 1; k++) {
      childNodes.get(k).setNextNode((childNodes.get(k + 1)));
    }

    return childNodes.get(0);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public Node visitExpression(JavaParser.ExpressionContext ctx) {
    return visitChildren(ctx);
  }

}
