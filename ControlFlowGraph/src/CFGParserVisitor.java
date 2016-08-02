/*
 * @TODO Missing try-catch statement nodes
 * @TODO Missing labeled continues/breaks
 * @TODO Missing multiple CFG Functionality
 * @TODO Redo removeConnected to specific removal
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class CFGParserVisitor extends JavaBaseVisitor<Node> {

	private Node breakableNode; // Nodes that can contain a break/continue node
								// (e.g. loops, switches)
	private static CommonTokenStream tokens;
	private List<Node> allNodes = new ArrayList<Node>();
	private List<Node> labels = new ArrayList<Node>();
	private int ID = 0;

	public CFGParserVisitor(JavaParser parser) {
		CFGParserVisitor.tokens = (CommonTokenStream) parser.getTokenStream();
	}

	public void printNodes() {
		for (Node currentNode : this.allNodes) {
			currentNode.printNode();
		}
	}

	public void linkBreakNode(Node breakNode) {
		breakNode.getConnectedTo().clear(); // Previous linkage is wrong (unsure
											// where it is being done)

		// If the BREAK node's length is greater than 6 which is
		// the size of "break:" then it is a labeled break
		String breakString = breakNode.getLineString();
		if (breakString.length() > 6) {
			String label = breakString.substring(6, breakString.length() - 1);

			// Look for a label that has the same string
			for (int i = 0; i < labels.size(); i++) {
				Node currentLabel = labels.get(i);

				// Link the current break node to whatever this label is linked
				// to
				if (currentLabel.getLineString().equals(label + ":")) {
					breakNode.addConnected(currentLabel.getConnectedTo().get(0));
				}
			}
		}

		else {
			if (breakableNode.getConnectedTo().size() > 1)
				breakNode.addConnected(breakableNode.getConnectedTo().get(1));
		}
	}

	public void linkContinueNode(Node continueNode) {
		continueNode.getConnectedTo().clear(); // Previous linkage is wrong
												// (unsure where it is being
												// done)

		// If the CONTINUE node's length is greater than 9 which is
		// the size of "continue:" then it is a labeled continue
		String breakString = continueNode.getLineString();
		if (breakString.length() > 9) {
			String label = breakString.substring(9, breakString.length() - 1);

			// Look for a label that has the same string
			for (int i = 0; i < labels.size(); i++) {
				Node currentLabel = labels.get(i);

				// Link the current break node to whatever this label is linked
				// to
				if (currentLabel.getLineString().equals(label + ":")) {
					continueNode.addConnected(currentLabel.getConnectedTo().get(0));
				}
			}
		}

		else

			continueNode.addConnected(breakableNode);
	}

	public void linkLoopNode(Node loopNode) {

		if (loopNode.getConnectedTo().isEmpty())
			return;

		/*
		 * Keep track of the current child. The first child will be the LOOP's
		 * 'true' child (the inside of the for), which is the first child in the
		 * list (index 0)
		 */
		Node childNode = loopNode.getConnectedTo().get(0);

		// The idea is to traverse all the children until we reach the last one
		// in the block and link it to the original loop node
		childNode = loopNode.getLastInnerChild();

		// Link the last block node to the loop node
		childNode.addConnected(loopNode);
	}

	public void linkElseIfNode(Node elseNode) {

		// Find the last OUTER child, what's outside of the else if (another
		// else if, an else, etc)
		// and link exit node of the current Node to it.
		Node exitNode = elseNode.getExitNode();
		Node lastOuterChild = elseNode.getLastOuterChild();

		// If the outer child is
		if (lastOuterChild == null) {
			elseNode.addConnected(exitNode);
			return;
		}

		lastOuterChild.addConnected(exitNode);
	}

	/**
	 * Due to the way the visitors are made, IF nodes will end up being
	 * connected to 3 nodes if it has an ELSE/ELSE_IF as part of the statement.
	 * This has to do with the implementation of 'visitBlock' and
	 * 'visitIfElseStmt' methods.
	 * 
	 * The 'if' node will be linked to the true statement (the inside of the
	 * if), the ELSE/ELSE_IF node, and whatever is after the ENTIRE IF statement
	 * (if, else if's, else) in that particular order.
	 * 
	 * This simply removes the last entry in the connection list in order to
	 * remove the wrong link.
	 *
	 * @param ifNode
	 */
	public void linkIfNode(Node ifNode) {

		// Traverse all the inner children (the first child) of each node
		// in order to reach the last node inside the IF
		Node lastInnerChild = ifNode.getLastInnerChild();

		// connect that last child to the outside of the if
		int lastLinkIndex = ifNode.getConnectedTo().size() - 1; // The index of
																// the last
																// child (the
																// exit node
																// index)
		Node exitNode = ifNode.getConnectedTo().get(lastLinkIndex);
		exitNode.setExitNode(true);
		lastInnerChild.addConnected(exitNode);

		if (ifNode.getConnectedTo().size() == 3) {

			// If the child is an ELSE_IF node then add the exit node to the
			// last child in its block
			// if (ifNode.getConnectedTo().get(1).getType() ==
			// Node.nodeType.ELSE_IF) {
			Node innerTemp = ifNode.getConnectedTo().get(1).getLastInnerChild();
			innerTemp.addConnected(exitNode);
			// }

			// If it is not an ELSE_IF node then it is for sure an ELSE node
			// else{
			// Node innerTemp =
			// ifNode.getConnectedTo().get(1).getLastInnerChild();
			// innerTemp.addConnected(exitNode);
			//
			// }
			ifNode.removeConnected();
		}

	}

	public void linkSwitchNodes(Node switchNode) {
		// connect that last child to the outside of the if
		int lastLinkIndex = switchNode.getConnectedTo().size() - 1; // The index
																	// of the
																	// last
																	// child
																	// (the exit
																	// node
																	// index)
		Node exitNode = switchNode.getConnectedTo().get(lastLinkIndex);
		exitNode.setExitNode(true);
	}

	public void sortNodes() {
		// Sort all the nodes by their unique ID, which identifies in which
		// order they
		// are created.
		Collections.sort(this.allNodes, new Comparator<Node>() {
			@Override
			public int compare(Node node2, Node node1) {
				if (node2.getID() < node1.getID())
					return -1;

				else if (node2.getID() == node1.getID())
					return 0;

				else
					return 1;
			}
		});
	}

	public void linkNormalNode(Node normalNode){
		if(normalNode.getConnectedTo().isEmpty()) return;
		
		Node nextNode = normalNode.getConnectedTo().get(0);
		
		//If the next node is a loop node and it's a do while, then change the link to the inside of the loop.
		if(nextNode.getType() == Node.nodeType.LOOP && nextNode.getLineString().charAt(0) == 'd'){
			normalNode.removeConnected();
			normalNode.addConnected(nextNode.getConnectedTo().get(0));
		}
	}
	
	public CFG createCFG(){
		CFG graph = new CFG(allNodes);
		return graph;
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

			// If the current node is an BREAK instance
			else if (currentNode.getType() == Node.nodeType.BREAK) {
				linkBreakNode(currentNode);
			}

			// If the current node is an CONTINUE instance
			else if (currentNode.getType() == Node.nodeType.CONTINUE) {
				linkContinueNode(currentNode);
			}

			// If the current node is a LOOP node
			else if (currentNode.getType() == Node.nodeType.LOOP) {
				linkLoopNode(currentNode);
				breakableNode = currentNode; //////////////////////////
			}

			// if the current node is a SWITCH node
			else if (currentNode.getType() == Node.nodeType.SWITCH) {
				linkSwitchNodes(currentNode);
				breakableNode = currentNode; /////////////////////////////
				currentNode.removeConnected();
			}

			// Otherwise our node is a NORMAL node
			else {
				linkNormalNode(currentNode);
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
			System.out.println("This is the token start position in line: " + tok.getCharPositionInLine());
			System.out.println(tok);
			idx++;
			tok = tokens.get(idx);
		}
	}

	/**
	 * This method takes in the starting token from a given context and
	 * traverses through the tokens until the given String is detected in order
	 * to stop looking for tokens. It adds them to a final string variable
	 * separating them with a whitespace ' '.
	 * 
	 * @param tok
	 *            - The starting token of the context
	 * @param endChar
	 *            - The indication of which token to stop at.
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
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitIfElseStmt(JavaParser.IfElseStmtContext ctx) {

		Boolean isElseIf = false;

		// Token tok = ctx.getStart();
		String lineString = "";

		// If the parent context was an 'IfElseStmtContext' then this is an else
		// instance
		if (ctx.parent.getClass().getName() == "JavaParser$IfElseStmtContext") {

			// If the parent context had 5 children and the last child was equal
			// to the current
			// context ('IfElseStmtContext' then this is an 'else' instance. So
			// we add an 'else' to the
			// string
			if (ctx.parent.getChildCount() == 5
					&& ctx.parent.getChild(4).getClass().getName().equals(ctx.getClass().getName()))
				lineString = "else ";
			isElseIf = true;
		}

		// // If the statement's first children is a block statement (meaning
		// the
		// // 'if' is using '{}') then we stop looking for tokens at the opening
		// '{'
		// String childNodeClass =
		// ctx.statement(0).children.get(0).getClass().getName();
		// if (childNodeClass.equalsIgnoreCase("JavaParser$BlockContext"))
		// lineString += getLineParam(tok, "{");
		//
		// // Otherwise, stop looking at a parenthesis. --- THIS DOESN'T WORK
		// DUE TO MULTIPLE INNER ()
		// else
		// lineString += getLineParam(tok, ")");
		lineString += "if " + ctx.parExpression().getText();// getLineParam(tok,
															// ")");

		Node currentNode = new Node();
		currentNode.setLineNumber(ctx.getStart().getLine());
		currentNode.setID(ID);
		ID++;
		currentNode.setLineString(lineString);

		if (isElseIf)
			currentNode.setType(Node.nodeType.ELSE_IF);

		else
			currentNode.setType(Node.nodeType.IF);

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
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitForStmt(JavaParser.ForStmtContext ctx) {

		Node currentNode = new Node();
		currentNode.setLineNumber(ctx.getStart().getLine());

		Token tok = ctx.getStart();
		String lineString = getLineParam(tok, "{");// This might not work
													// correctly if the
													// condition has an opening
													// parenthesis in it. Is
													// that possible?
		currentNode.setID(ID);
		ID++;
		currentNode.setLineString(lineString);
		currentNode.setType(Node.nodeType.LOOP);
		currentNode.addConnected(visit(ctx.statement()));
		allNodes.add(currentNode);

		return currentNode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitDoStmt(JavaParser.DoStmtContext ctx) {
		Node currentNode = new Node();
		currentNode.setLineNumber(ctx.getStart().getLine());

		Token tok = ctx.getStart();
		String lineString = getLineParam(tok, "{");// This might not work
													// correctly if the
													// condition has an opening
													// parenthesis in it. Is
													// that possible?
		lineString += "} while " + ctx.parExpression().getText();

		tok.getStopIndex();
		currentNode.setLineString(lineString);
		currentNode.setType(Node.nodeType.LOOP);
		currentNode.addConnected(visit(ctx.statement()));
		currentNode.setID(ID);
		ID++;
		allNodes.add(currentNode);

		return currentNode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitSwitchStmt(JavaParser.SwitchStmtContext ctx) {

		Token tok = ctx.getStart();
		String lineString = "";

		lineString += getLineParam(tok, "{");

		Node currentNode = new Node();
		currentNode.setLineNumber(ctx.getStart().getLine());
		currentNode.setID(ID);
		ID++;
		currentNode.setLineString(lineString);
		currentNode.setType(Node.nodeType.SWITCH);

		ArrayList<Node> groupChildren = new ArrayList<Node>();
		int groupChildrenNum = ctx.switchBlockStatementGroup().size();
		int groupIndex = 0;

		// Traverse the SwitchBlockStatementGroup children and add them to a
		// list
		while (groupIndex < groupChildrenNum) {
			Node blockChild = visit(ctx.switchBlockStatementGroup(groupIndex));

			groupChildren.add(blockChild);

			groupIndex++;
		}

		if (!groupChildren.isEmpty())
			currentNode.addConnected(groupChildren.get(0));

		// Link the children to each other
		for (int k = 0; k < groupChildren.size() - 1; k++) {
			groupChildren.get(k).addConnected(groupChildren.get(k + 1));
		}
		allNodes.add(currentNode);
		return currentNode;

	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitWhileStmt(JavaParser.WhileStmtContext ctx) {

		Node currentNode = new Node();
		currentNode.setLineNumber(ctx.getStart().getLine());

		Token tok = ctx.getStart();
		String lineString = getLineParam(tok, "{"); // This might not work
													// correctly if the
													// condition has an opening
													// parenthesis in it. Is
													// that possible?
		currentNode.setID(ID);
		ID++;
		currentNode.setLineString(lineString);
		currentNode.setType(Node.nodeType.LOOP);
		currentNode.addConnected(visit(ctx.statement()));
		allNodes.add(currentNode);

		return currentNode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitSwitchBlockStatementGroup(JavaParser.SwitchBlockStatementGroupContext ctx) {

		String caseString = "";

		int labelNum = ctx.switchLabel().size(); // amount of label children
		int labelIndex = 0;

		while (labelIndex < labelNum) {
			caseString += ctx.switchLabel(labelIndex).getText();
			if (labelIndex != labelNum - 1)
				caseString += "\n";
			labelIndex++;
		}

		Node caseNode = new Node();
		caseNode.setID(ID);
		ID++;
		caseNode.setLineNumber(ctx.start.getLine());
		caseNode.setLineString(caseString);
		caseNode.setType(Node.nodeType.NORMAL);

		ArrayList<Node> blockChildren = new ArrayList<Node>();
		int blockStatementNum = ctx.blockStatement().size(); // amount of block
																// children
		int blockIndex = 0;

		// Traverse the blockStatement children and add them to a list
		while (blockIndex < blockStatementNum) {
			Node blockChild = visit(ctx.blockStatement(blockIndex));

			if (blockIndex == 0)
				caseNode.addConnected(blockChild);

			blockChildren.add(blockChild);

			blockIndex++;
		}

		// Link the children to each other
		for (int k = 0; k < blockChildren.size() - 1; k++) {
			blockChildren.get(k).addConnected(blockChildren.get(k + 1));
		}

		allNodes.add(caseNode);

		return caseNode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitBreakStmt(JavaParser.BreakStmtContext ctx) {
		Token tok = ctx.getStart();
		String lineString = getLineParam(tok, ";");

		Node currentNode = new Node();

		currentNode.setID(ID);
		ID++;
		currentNode.setLineNumber(ctx.start.getLine());
		currentNode.setLineString(lineString);
		currentNode.setType(Node.nodeType.BREAK);

		// currentNode.printNode();

		allNodes.add(currentNode);

		return currentNode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitContStmt(JavaParser.ContStmtContext ctx) {
		Token tok = ctx.getStart();
		String lineString = getLineParam(tok, ";");

		Node currentNode = new Node();

		currentNode.setID(ID);
		ID++;
		currentNode.setLineNumber(ctx.start.getLine());
		currentNode.setLineString(lineString);
		currentNode.setType(Node.nodeType.CONTINUE);

		// currentNode.printNode();

		allNodes.add(currentNode);

		return currentNode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
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

		// currentNode.printNode();

		allNodes.add(currentNode);

		return currentNode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
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

		// currentNode.printNode();

		visitChildren(ctx);// Probably remove this

		allNodes.add(currentNode);

		return currentNode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitSemiStmt(JavaParser.SemiStmtContext ctx) {

		Node currentNode = new Node();
		currentNode.setID(ID);
		ID++;
		currentNode.setLineNumber(ctx.start.getLine());
		currentNode.setLineString(ctx.getText());
		currentNode.setType(Node.nodeType.NORMAL);

		// currentNode.printNode();

		visitChildren(ctx);// Probably remove this

		allNodes.add(currentNode);

		return currentNode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
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

		// currentNode.printNode();

		visitChildren(ctx);// Probably remove this

		allNodes.add(currentNode);

		return currentNode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
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

		// currentNode.printNode();

		// visitChildren(ctx);// Probably remove this

		allNodes.add(currentNode);

		return currentNode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitIdStmt(JavaParser.IdStmtContext ctx) {

		// This context is for tags

		Node currentNode = new Node();
		currentNode.setID(-ID);

		currentNode.setLineNumber(ctx.start.getLine());
		currentNode.setLineString(ctx.Identifier().getText() + ":");
		currentNode.setType(Node.nodeType.TAG);

		// currentNode.printNode();

		currentNode.addConnected(visit(ctx.statement()));

		labels.add(currentNode);

		return currentNode;
	}

	/**
	 * This method is a helper method to avoid using returnChildren() in order
	 * to return a Node.
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
	 * This method is what links nodes in the same block to each other. It
	 * traverses the block children linking one to the next child.
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

			Node currentNode = childNodes.get(k);
			Node nextNode = childNodes.get(k + 1);

			if (nextNode.getType() == Node.nodeType.TAG) {
				currentNode.addConnected(nextNode.getConnectedTo().get(0));
				continue;
			}

			// Dont link BREAKS or CONTINUE nodes to whatever is next
			if (currentNode.getType() == Node.nodeType.BREAK || currentNode.getType() == Node.nodeType.CONTINUE) {
				continue;
			}

			currentNode.addConnected(nextNode);
		}

		childNodes.get(childNodes.size() - 1).setBlockEnd(true); // The last
																	// child is
																	// at the
																	// end of
																	// the {}
																	// block

		return childNodes.get(0);
	}
}