import java.io.IOException;

import javax.swing.JFrame;

public class CFGDriver {

  public static void main(String[] args) throws IOException{
    CFGListener listener = new CFGListener("Test Files/BreakDemo.java");
    //		CodeHandler cl = new CodeHandler("Test Files/TestAll.java");
    //		cl.linkNodes();
    //		cl.printNodes();
    //		CFG cfg = cl.createCFG();

    

    for(int i = 0; i < listener.cfgList.size(); i++){
      XmlCreator xc = new XmlCreator(listener.cfgList.get(i));
      
      CFG cfg = listener.cfgList.get(i);
      xc = new XmlCreator(cfg);
      xc.save("C:\\Users\\LopezMarcoA\\Desktop\\test"+i+".xml");
      Visualizer viz = new Visualizer(xc.getXmlString(),cfg.getName());
      viz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      viz.setSize(400, 320);
      viz.setVisible(true);
    }
//    xc.save("C:\\Users\\LopezMarcoA\\Desktop\\test.xml");
//    Visualizer viz = new Visualizer(xc.getXmlString());
//    viz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    viz.setSize(400, 320);
//    viz.setVisible(true);
  }

}
