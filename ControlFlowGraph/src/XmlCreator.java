import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class XmlCreator {

  private CFG cfg;
  
  private StringBuilder xmlString;
  
  private static final char SPACE = ' ';
  private static final char QUOTE = '"';
  private static final char RETURN = '\n';
  private static final char TAB = '\t';
  
  private static final String START = "<";
  private static final String CLOSE = "/";
  private static final String END = ">";
  
  private static final String GRAPH_TAG = "graph";
  private static final String NODE_TAG = "node";
  private static final String EDGE_TAG = "edge";
  
  /** XmlCreator Constructor.
   *  will take a cfg.
   */
  public XmlCreator(CFG newCfg) {
    
    cfg = newCfg;
    xmlString = new StringBuilder();
    toXml();
  }
  
  
  
  
  private void toXml() {
    addHeader();
    
    @SuppressWarnings("rawtypes")
    Iterator iterator = cfg.iterator();
    while (iterator.hasNext()) {
      Node currentNode = (Node)iterator.next();
      
      String code = currentNode.getLineNumber() + ", " + currentNode.getLineString();
      
      code = code.replaceAll(QUOTE + "", "&quot;");
      code = code.replaceAll("<", "&lt;");
      code = code.replaceAll(">", "&gt;");
      int id = currentNode.getID();
      addNode(id, code);
      
      List<Node> nextNodes = currentNode.getConnectedTo();
      if (!nextNodes.isEmpty()) {
        for (int nodeNumber = 0; nodeNumber < nextNodes.size(); nodeNumber++) {
          Node neighbor = nextNodes.get(nodeNumber);
          if (nextNodes.size() == 2) {
            String edgeType = (nodeNumber == 0) ? "TRUE" : "FALSE";
            addEdge(id, neighbor.getID(), edgeType);
          } else {
            addEdge(id, neighbor.getID(),"");
          }
          
        }
      }
    }  
    
    String endTag = START + CLOSE + GRAPH_TAG + END;
    xmlString.append(endTag);
    xmlString.append(RETURN);
    
  }
  
  
  
  private void addHeader() {
    
    String header = START + GRAPH_TAG + SPACE;
    header = header + "id=" + QUOTE + "DTMC" + QUOTE + SPACE 
        + "edgedefault=" + QUOTE + "directed" + QUOTE + END;
    
    
    
    xmlString.append(header);
    xmlString.append(RETURN);
    
  }
  
  
  
  private void addNode(int id, String value) {

    String nodeString = START + NODE_TAG + SPACE 
        + "id=" + QUOTE + id + QUOTE + SPACE + "value=" 
        + QUOTE + value + QUOTE + CLOSE + END;
    
    xmlString.append(TAB);
    xmlString.append(nodeString);
    xmlString.append(RETURN);
  }
  
  
  
  private void addEdge(int source, int target, String value) {
    
    String edgeString = START + EDGE_TAG + SPACE 
        + "source=" + QUOTE + source + QUOTE 
        + SPACE + "target=" + QUOTE + target + QUOTE + SPACE 
        + "value=" + QUOTE + value + QUOTE + CLOSE + END;
    
    xmlString.append(TAB);
    xmlString.append(edgeString);
    xmlString.append(RETURN);
  }
  
  
  
  public String getXmlString() {
    
    return xmlString.toString();
  }
  
  
  /**
   * This method will save an xml representation of our cfg.
   * @return true if save was successful.
   */
  public boolean save(String path) throws IOException {
    
    boolean isSaved = false;
    XmlWriter xmlSaver = new XmlWriter(path);
    
    isSaved = xmlSaver.writeXmlString(xmlString.toString());
    
    return isSaved;
  }
}
