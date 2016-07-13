import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class CFGParserVisitor extends JavaBaseVisitor<Node> {

	static CommonTokenStream tokens;

	public CFGParserVisitor(JavaParser parser) {
		CFGParserVisitor.tokens = (CommonTokenStream) parser.getTokenStream();
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
		// System.out.println(ctx.getChildCount());
		// System.out.println("First Child: " + ctx.statement(0).getText());
		// if
		// (ctx.statement(1).getRuleContext().getClass().toString().equals("class
		// JavaParser$BlockStmtContext")) {
		// System.out.println("Second Child: " + ctx.statement(1).getText());
		// }
		// System.out.println(ctx.statement(1).getRuleContext().getClass().toString());
		// System.out.println("Depth: " + ctx.depth());
		// System.out.println("Number of Children: " + ctx.statement().size());
		// System.out.println("------------------");
		// System.out.println("THIS IS A CURLY BRACE "+ctx.getToken(59,23));

		Node currentNode = new Node();
		currentNode.setLineNumber(ctx.getStart().getLine());

		Token tok = ctx.getStart();
		int index = tok.getTokenIndex();
		String lineString = "";

		while (!tok.getText().equals("{")) {
			lineString += tok.getText();
			lineString += " ";
			index++;
			tok = tokens.get(index);
		}
		lineString += "{";

		currentNode.setLineString(lineString);
		currentNode.setType(2);
		currentNode.printNode();

		visitChildren(ctx);

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
		int index = tok.getTokenIndex();
		String lineString = "";

		while (!tok.getText().equals("{")) {
			lineString += tok.getText();
			lineString += " ";
			index++;
			tok = tokens.get(index);
		}
		lineString += "{";

		currentNode.setLineString(lineString);
		currentNode.setType(2);
		currentNode.printNode();

		visitChildren(ctx);

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
		int index = tok.getTokenIndex();
		String lineString = "";

		while (!tok.getText().equals("{")) {
			lineString += tok.getText();
			lineString += " ";
			index++;
			tok = tokens.get(index);
		}
		lineString += "{";

		currentNode.setLineString(lineString);
		currentNode.setType(2);
		currentNode.printNode();

		visitChildren(ctx);

		return currentNode;	}

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

		currentNode.setLineNumber(ctx.start.getLine());
		currentNode.setLineString(lineString);
		currentNode.setType(1);

		currentNode.printNode();
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
		currentNode.setLineNumber(ctx.start.getLine());
		currentNode.setLineString(lineString);
		currentNode.printNode();

		visitChildren(ctx);// Probably remove this

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
	public Node visitStatementExpression(JavaParser.StatementExpressionContext ctx) {
		// System.out.println(ctx.getText());
		// System.out.println("------------------");

		Node currentNode = new Node();
		currentNode.setLineNumber(ctx.start.getLine());
		currentNode.setLineString(ctx.getText() + ";");
		currentNode.printNode();

		visitChildren(ctx);// Probably remove this

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
	public Node visitBlockStmt(JavaParser.BlockStmtContext ctx) {
		// Node currentNode = new Node();
		// currentNode.setLineNumber(10000);
		// currentNode.setLineString("THIS IS A BLOCK_STATEMENT NODE. TESTING
		// PURPOSES");
		// currentNode.setType(5);
		// visitChildren(ctx);
		// visit(ctx.block());
		// return currentNode;
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

		/*
		 * Visit all the children INDIVIDUALLY and link them one to the other
		 * UNLESS the child node can have two child nodes. E.g. an 'if' in
		 * addition to an 'else if'/'else'. The true child will be within the
		 * 'if' statement and the false will be within the 'else'. If there is
		 * no 'else' the false node will be within the same depth of the 'if'
		 * i.e. a sibling in the antlr parse tree.
		 */

		// Traverse all the children
		// for(Node currentNode = ctx.; i < ctx.blockStatement().size(); i++){
		//
		// }

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
	public Node visitExpression(JavaParser.ExpressionContext ctx) {
		Node currentNode = new Node();

		currentNode.setLineNumber(10000);
		currentNode.setLineString("THIS IS AN EXPRESSION NODE. TESTING PURPOSES");
		currentNode.setType(5);

		visitChildren(ctx);// Probably remove this

		return currentNode;
	}

}