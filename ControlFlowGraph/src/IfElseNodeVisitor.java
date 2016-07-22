import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;

public class IfElseNodeVisitor extends JavaBaseVisitor<String> {

	private ArrayList<Node> cfgNodes = new ArrayList<Node>();
	
	public IfElseNodeVisitor(ArrayList<Node> nodeList)
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
	@Override public String visitIfElseStmt(JavaParser.IfElseStmtContext ctx) { 
		
		/* Get current If node */
		IfElseNode currentNode = new IfElseNode("Uninitialized", -1, -1, new Node("Uninitialized", -1, -1), new Node("Uninitialized", -1, -1));
		for (Node node : cfgNodes) {
			if (node.getUniqueID() == ctx.start.getTokenIndex())
			{
				currentNode = (IfElseNode)node;
				break;
			}				
		}
		
		/* If statement has else part */
		if(ctx.getChildCount() == 5)
		{
			ParseTree trueBranch = ctx.getChild(2).getChild(0).getChild(1).getChild(0);
			ParseTree falseBranch;
			
			if (ctx.getChild(4).getChild(0).getClass().toString().equals("class JavaParser$BlockContext"))
			{
				falseBranch = ctx.getChild(4).getChild(0).getChild(1).getChild(0);
			}
			else
			{
				falseBranch = ctx.getChild(4);
			}
			
			String trueID = visit(trueBranch);
			String falseID = visit(falseBranch);
			
			/* Now add edges for true and false branches */
			for (Node node : cfgNodes) {
				if (String.valueOf(node.getUniqueID()).equals(trueID))
				{
					currentNode.setTrueNode(node);
					break;
				}				
			}
			
			for (Node node : cfgNodes) {
				if (String.valueOf(node.getUniqueID()).equals(falseID))
				{
					currentNode.setFalseNode(node);
					break;
				}				
			}
		}
		/* Otherwise statement has no else part */
		else
		{
			ParseTree trueBranch;
			
			if(ctx.getChild(2).getClass().toString().equals("class JavaParser$StmtExprStmtContext") ||
			   ctx.getChild(2).getClass().toString().equals("class JavaParser$LocalVariableDeclarationStatementContext"))
			{
				trueBranch = ctx.getChild(2);			
			}
			else
			{
				trueBranch = ctx.getChild(2).getChild(0).getChild(1).getChild(0);			
			}

			String trueID = visit(trueBranch);
			
			/* Now add edges for true and false branches */
			for (Node node : cfgNodes) {
				if (String.valueOf(node.getUniqueID()).equals(trueID))
				{
					currentNode.setTrueNode(node);
					break;
				}				
			}
		}

		return String.valueOf(ctx.start.getTokenIndex());
	}	
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public String visitStmtExprStmt(JavaParser.StmtExprStmtContext ctx) {
		return String.valueOf(ctx.start.getTokenIndex());
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public String visitLocalVariableDeclarationStatement(JavaParser.LocalVariableDeclarationStatementContext ctx) {
		return String.valueOf(ctx.start.getTokenIndex());
	}
	

}
