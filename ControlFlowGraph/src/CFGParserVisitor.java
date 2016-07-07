
public class CFGParserVisitor extends JavaBaseVisitor<String>{
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public String visitIfElseStmt(JavaParser.IfElseStmtContext ctx) { 
		System.out.println(ctx.getText());
		System.out.println("-------------");
		return visitChildren(ctx); 
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public String visitLocalVariableDeclarationStatement(JavaParser.LocalVariableDeclarationStatementContext ctx) { 
		System.out.println(ctx.getText());
		System.out.println("-------------");
		return visitChildren(ctx); 
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public String visitStatementExpression(JavaParser.StatementExpressionContext ctx) { 
		System.out.println(ctx.getText());
		System.out.println("-------------");
		return visitChildren(ctx); 
	}
}
