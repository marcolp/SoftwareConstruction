import java.util.ArrayList;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class CFGParser {
	public static void main( String[] args) throws Exception 
    {

        ANTLRInputStream input = new ANTLRFileStream("C:\\Users\\Vince\\Desktop\\TestClass.java");

        JavaLexer lexer = new JavaLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        JavaParser parser = new JavaParser(tokens);
        ParseTree tree = parser.compilationUnit();
        ArrayList<Node> nodeList = new ArrayList<Node>();
        NodeVisitor nodeVisitor = new NodeVisitor(nodeList);
        nodeVisitor.visit(tree);
        IfElseNodeVisitor ifElseVisitor = new IfElseNodeVisitor(nodeList);
        ifElseVisitor.visit(tree);
        System.out.println(nodeList);
    }
}
