import java.io.IOException;

import javax.swing.JFrame;

public class CFGDriver {

	public static void main(String[] args) throws IOException{
		CodeHandler cl = new CodeHandler("Test Files/TestAll.java");
		cl.linkNodes();
		cl.printNodes();
		CFG cfg = cl.createCFG();
		XmlCreator xc = new XmlCreator(cfg);
		//xc.save("C:\\Users\\Vince\\Desktop\\test.xml");
		Visualizer viz = new Visualizer(xc.getXmlString());
		viz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		viz.setSize(400, 320);
		viz.setVisible(true);
	}

}
