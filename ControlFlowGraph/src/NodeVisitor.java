import java.util.ArrayList;

public class NodeVisitor extends JavaBaseVisitor<Node> {
	
	private ArrayList<Node> cfgNodes = new ArrayList<Node>();
	
	public NodeVisitor(ArrayList<Node> nodeList)
	{
		cfgNodes = nodeList;
	}
	
	public ArrayList<Node> getCFGNodes()
	{
		return cfgNodes;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Node visitStmtExprStmt(JavaParser.StmtExprStmtContext ctx) {
		SingleStatementNode stmtNode = new SingleStatementNode(ctx.getText(), ctx.start.getLine(), ctx.start.getTokenIndex());
		cfgNodes.add(stmtNode);
		return stmtNode;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Node visitLocalVariableDeclarationStatement(JavaParser.LocalVariableDeclarationStatementContext ctx) {
		SingleStatementNode stmtNode = new SingleStatementNode(ctx.getText(), ctx.start.getLine(), ctx.start.getTokenIndex());
		cfgNodes.add(stmtNode);
		return stmtNode;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Node visitIfElseStmt(JavaParser.IfElseStmtContext ctx) { 
		IfElseNode ifElseNode = new IfElseNode(ctx.getText(), ctx.start.getLine(), ctx.start.getTokenIndex(), new Node("NotANode", -1, -1), new Node("NotANode", -1, -1));
		cfgNodes.add(ifElseNode);
		visitChildren(ctx);
		return ifElseNode;
	}
}
