import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.apache.commons.io.FilenameUtils;
/**
* Code Loader class is meant to load
* the java file into memory
*/
public final class CodeLoader {
   private ANTLRFileStream inputStream;
 
   /**
    * initializes the the class fields
 * @throws IOException 
    * */
   public CodeLoader(String fileName) throws IOException {
	   
	   // Must make sure the file is a java file.
	   String ext = FilenameUtils.getExtension(fileName);
	   if(!ext.equals("java"))
	   {
		   // Handle error.
		   System.out.println("File must be a .java file.");
	   }
	   else
	   {
		   this.inputStream = new ANTLRFileStream(fileName);
		 
	   }
   }
   /**
    * returns the input stream object
    * @return ANTLRInputStream
    * */
   public ANTLRFileStream getJavaFile(){
       return this.inputStream;
	 
   }
}