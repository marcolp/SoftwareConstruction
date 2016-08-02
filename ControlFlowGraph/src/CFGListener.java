import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CFGListener extends JavaBaseListener{
  List<CFG> cfgList= new ArrayList<CFG>();
  JavaLexer lexer;

  public CFGListener(String pathToJavaFile) throws IOException {
    CodeLoader cl = new CodeLoader(pathToJavaFile);

    this.lexer = new JavaLexer(cl.getJavaFile());

    CommonTokenStream tokens = new CommonTokenStream(lexer);

    JavaParser parser = new JavaParser(tokens);
    CodeHandler.setTokens(tokens);

    ParseTree tree = parser.compilationUnit();

    ParseTreeWalker.DEFAULT.walk(this, tree);

  }

  /**
   * {@inheritDoc}
   *
   * <p>The default implementation does nothing.</p>
   * @throws IOException 
   */
  @Override 
  public void enterMethodDeclaration(@NotNull JavaParser.MethodDeclarationContext ctx)  {
    String methodName = "";
    
    if(ctx.type() != null)methodName += ctx.type() + " ";
    else methodName += "void ";
    
    methodName += ctx.Identifier().getText() + " ";
    methodName += ctx.formalParameters().getText();
    
    System.out.println("\n-----------LISTENING "+methodName+" METHOD-----------\n");

    String method = ctx.getText();
    
    CodeHandler visitor = new CodeHandler(method);
//    System.out.println(method);
    visitor.visit(ctx);
    visitor.linkNodes();
    visitor.printNodes();
    CFG temp = visitor.createCFG();
    temp.setName(methodName);
    cfgList.add(temp);

  }

}
