import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XmlWriter {

  private File xmlFile;
  
  /** XmlWriter Constructor.
   *  will take a valid path where we want to write our file.
   */
  public XmlWriter(String path) {
    
    xmlFile = new File(path);
    
  }
  
  /** XmlWriter Constructor.
   *  will take a valid xml string.
   */
  public boolean writeXmlString(String xmlString) throws IOException {
    
    BufferedWriter writer;
    
    try {
      writer = new BufferedWriter(new FileWriter(xmlFile));
    } catch (IOException ex) {
      return false;
    }
    
    
    try {
      writer.append(xmlString.toString());
    } catch (IOException ex) {
      // TODO Auto-generated catch block
      writer.close();
      return false;
    }
    
    writer.close();
    return true;
  }
}
