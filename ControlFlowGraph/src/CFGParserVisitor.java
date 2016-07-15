import java.util.ArrayList;
import java.util.Stack;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class CFGParserVisitor extends JavaBaseVisitor<Node> {

	private static CommonTokenStream tokens;
	// private Stack<Node> pointBack = new Stack<Node>();
	private static ArrayList<Node> allNodes = new ArrayList<Node>();
	private static int ID = 0;

	// private static int BlockCount = 0;
	// private static int BlockStatement = 0;
	// private static int BlockStmt = 0;
	//
	public CFGParserVisitor(JavaParser parser) {
		CFGParserVisitor.tokens = (CommonTokenStream) parser.getTokenStream();
	}

	public static void printNodes() {
		for (Node currentNode : allNodes) {
			currentNode.printNode();
		}
	}

	public static void linkNodes() {

		// Traverse all the current nodes
		for (int i = 0; i < allNodes.size(); i++) {

			// Make a temporary node to keep track as we traverse
			Node currentNode = new Node();
			currentNode = allNodes.get(i);

			// If the current node is an IF node
			if (currentNode.getType() == Node.nodeType.IF) {

				/*
				 * Due to the way the visitors are made IF nodes will end up
				 * being connected to 3 nodes if it has an ELSE/ELSE_IF as part
				 * of the statement. This has to do with the implementation of
				 * 'visitBlock' and 'visitIfElseStmt' methods.
				 * 
				 * The if node will be linked to the true statement (the inside
				 * of the if), the else/else if node, and whatever is after the
				 * ENTIRE if statement (if, else if's, else) in that particular
				 * order.
				 * 
				 * This simply removes the last entry in the connection list in
				 * order to remove the wrong link.
				 */
				for (int k = currentNode.getConnectedTo().size(); k > 2; k = currentNode.getConnectedTo().size()) {
					currentNode.removeConnected();
				}

				// If the current node is a LOOP node
			} else if (currentNode.getType() == Node.nodeType.LOOP) {

				/*
				 * Keep track of the current child. The first child will be the
				 * LOOP's 'true' child (the inside of the for), which is the
				 * first child in the list (index 0)
				 */

				Node childNode = new Node();
				childNode = currentNode.getConnectedTo().get(0);

				/*
				 * The idea is to traverse all the children until we reach the
				 * last one and link it to the original loop node
				 */
				while (childNode != null) {

					/*
					 * If the current child has no connections then it means it
					 * is the last child
					 * 
					 * @TODO What happens when the last child is an IF or a
					 * LOOP?
					 */
					if (childNode.getConnectedTo().size() == 0) {
						childNode.addConnected(currentNode);
						break;
					}

					/*
					 * If the current child has more than 1 child, visit the
					 * second one because this means that it is a LOOP or IF
					 * statement. In which case the first child is the inside of
					 * the block which we don't want.
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
		Token tok = (Token) CFGParserVisitor.tokens.get(0);
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
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitIfElseStmt(JavaParser.IfElseStmtContext ctx) {

		Node currentNode = new Node();
		currentNode.setLineNumber(ctx.getStart().getLine());

		Token tok = ctx.getStart();
		String lineString = getLineString(tok);

		currentNode.setID(ID);
		ID++;
		currentNode.setLineString(lineString);
		currentNode.setType(Node.nodeType.IF);
		currentNode.setDepth(ctx.depth());
		currentNode.addConnected(visit(ctx.statement(0)));
		if (ctx.statement().size() > 1)
			currentNode.addConnected(visit(ctx.statement(1)));

		// visitChildren(ctx);

		// currentNode.printNode();

		allNodes.add(currentNode);

		return currentNode;
	}

	public String getLineString(Token tok) {
		int index = tok.getTokenIndex();
		String lineString = "";

		while (!tok.getText().equals("{")) {
			lineString += tok.getText();
			lineString += " ";
			index++;
			tok = tokens.get(index);
		}
		lineString += "{";

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
	public Node visitForStmt(JavaParser.ForStmtContext ctx) {

		Node currentNode = new Node();
		currentNode.setLineNumber(ctx.getStart().getLine());

		Token tok = ctx.getStart();
		String lineString = getLineString(tok);

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
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitWhileStmt(JavaParser.WhileStmtContext ctx) {

		Node currentNode = new Node();
		currentNode.setLineNumber(ctx.getStart().getLine());

		Token tok = ctx.getStart();
		String lineString = getLineString(tok);

		currentNode.setID(ID);
		ID++;
		currentNode.setLineString(lineString);
		currentNode.setType(Node.nodeType.LOOP);
		currentNode.setDepth(ctx.depth());

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
		// System.out.println(ctx.getText());
		// System.out.println("------------------");
		// System.out.println("This is the visit children return:
		// "+visitChildren(ctx).getLineNumber());
		Token tok = ctx.getStart();
		int index = tok.getTokenIndex();
		String lineString = "";

		while (index < ctx.getStop().getTokenIndex()) {
			lineString += tok.getText();
			if (index != ctx.getStop().getTokenIndex() - 1)
				lineString += " ";
			index++;
			tok = tokens.get(index);
		}
		lineString += ";";

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
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitReturnStmt(JavaParser.ReturnStmtContext ctx) {

		// We do this in order to add spaces to the line strings
		// Otherwise all the tokens would be together
		Token tok = ctx.getStart();
		int index = tok.getTokenIndex();
		String lineString = "";

		while (index < ctx.getStop().getTokenIndex()) {
			lineString += tok.getText();
			if (index != ctx.getStop().getTokenIndex() - 1)
				lineString += " ";
			index++;
			tok = tokens.get(index);
		}
		lineString += ";";

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
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitBlockStatement(JavaParser.BlockStatementContext ctx) {

		// System.out.println("Block Statement visit number: "+BlockStatement);
		// System.out.println("============================================");
		// BlockStatement++;

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
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Node visitBlockStmt(JavaParser.BlockStmtContext ctx) {

		// System.out.println("BlockStmt visit number: "+BlockStmt);
		// System.out.println("============================================");
		// BlockStmt++;

		return visitChildren(ctx);
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
	public Node visitBlock(JavaParser.BlockContext ctx) {

		ArrayList<Node> childNodes = new ArrayList<Node>();

		Node returnNode = new Node();
		int limit = ctx.getChildCount();

		// Traverse the children.
		// Ignoring the {} children
		for (int i = 1; i < limit - 1; i++) {
			Node currentNode = new Node();
			currentNode = visit(ctx.getChild(i));
			childNodes.add(currentNode);
		}

		for (int k = 0; k < childNodes.size(); k++) {
			if (k != childNodes.size() - 1)
				childNodes.get(k).addConnected(childNodes.get(k + 1));
		}

		return childNodes.get(0);
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
	public Node visitExpression(JavaParser.ExpressionContext ctx) {
		return visitChildren(ctx);
	}

}