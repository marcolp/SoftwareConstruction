import com.mxgraph.io.mxGraphMlCodec;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.JFrame;

public class Visualizer  extends JFrame { 
  
  // Must include with JFrame in order to keep Frame unique.
  private static final long serialVersionUID = -2764911804288120883L;
  private String xml = "";
  private Document doc;
  private mxGraph graph = new mxGraph();
  private final mxGraphComponent graphComponent; 
  
  /** Visualizer Constructor.
   *  will take a valid xml string.
   */
  public Visualizer(String vizXml, String cfgName) {
    // JPanel title
    super(cfgName +" method CFG visualization");
    
    // Set xml string.
    xml = vizXml;  
    
    //Load up our graph with the data
    loadGraphData();    
        
    // Our Layout
    mxIGraphLayout layout = new mxHierarchicalLayout(graph);

    // Adding the layout to the graph.
    layout.execute(graph.getDefaultParent());
      
    // Now displaying our graph.
    graphComponent = new mxGraphComponent(graph);
      
    // Disable editing the graph.
    graphComponent.setEnabled(false);
        
    // Add graph to our JFrame.
    getContentPane().add(graphComponent);
  }
  
  private void loadGraphData() {

    // Now will create Document from xml string
    doc = mxXmlUtils.parseXml(xml);
    
    // Adding data from Document into our graph.
    mxGraphMlCodec.decode(doc, graph);
      
    // Now adding all of our labels to each node.
    NodeList nodeList = doc.getElementsByTagName("node");
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node currentNode = nodeList.item(i);
      mxCell cell = (mxCell) graph.getChildVertices(graph.getDefaultParent())[i];
      cell.setValue(currentNode.getAttributes().getNamedItem("value").getNodeValue());
    }

    // Now adding all of our labels to each edge if there are any.
    NodeList edgeList = doc.getElementsByTagName("edge");
    for (int i = 0; i < edgeList.getLength(); i++) {
      Node currentNode = edgeList.item(i);
      mxCell cell = (mxCell) graph.getChildEdges(graph.getDefaultParent())[i];
      if (currentNode.getAttributes().getNamedItem("value") != null) {
        cell.setValue(currentNode.getAttributes().getNamedItem("value").getNodeValue());
      }
    }
  }
  
  public String getXml() {
    return xml;
  }
  
  public void setXml(String vizXml) {
    xml = vizXml;
  }
}
