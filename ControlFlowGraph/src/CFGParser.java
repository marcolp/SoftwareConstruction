import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class CFGParser {
	public static void main( String[] args) throws Exception 
    {

        ANTLRInputStream input = new ANTLRFileStream("if.java");

        JavaLexer lexer = new JavaLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        JavaParser parser = new JavaParser(tokens);
        
        ParseTree tree = parser.compilationUnit();
        
        CFGParserVisitor visitor = new CFGParserVisitor(parser);
        
        visitor.visit(tree);
        
        
        CFGParserVisitor.linkNodes();
        
        CFGParserVisitor.printNodes();

//        CFGParserVisitor.printAllTokens();
        
    }
}
