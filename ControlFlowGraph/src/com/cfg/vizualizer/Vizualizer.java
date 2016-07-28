package com.cfg.vizualizer;

import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mxgraph.examples.swing.XMLtoViz;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;

import att.grappa.Graph;
import att.grappa.GrappaAdapter;
import att.grappa.GrappaPanel;

public class Vizualizer extends JFrame{
	
	public Vizualizer() throws IOException, InterruptedException{
		
		// readXMLfile -> parseXML -> store in doc
		String filename = "xml\\Nodes.xml";
		Document doc = mxXmlUtils.parseXml(mxUtils.readFile(filename));
					
					
					
		Element cfg = (Element) doc.getDocumentElement();
		
		NodeList graphList = cfg.getElementsByTagName("node");
		StringBuilder yeah = new StringBuilder();
		
		//System.out.println("digraph { ");
		yeah.append("digraph { \n");
		for(int i = 0; i < graphList.getLength(); i++){
			Element currentNode = (Element)graphList.item(i);
			NodeList id = currentNode.getElementsByTagName("id");
			NodeList next =currentNode.getElementsByTagName("nextNode");
			
			for(int j = 0; j < next.getLength(); j++){
				String src = id.item(0).getTextContent();
				String dest = next.item(j).getTextContent();
				String connection = src + " -> " + dest + ";";
				//System.out.println(src + "->" + dest +";");
				yeah.append(connection + "\n");
			}
			
		}
		yeah.append("}");
		//System.out.println("}");
		System.out.println(yeah.toString());
		
		File file = new File("dotFile\\test.dot");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		//writer.write(yeah.toString());
		writer.append(yeah.toString());
		writer.close();
		
		Runtime rt = Runtime.getRuntime();
		String[] args = {"DOT\\dot.exe", "-Tpng", "-O", "dotFile\\test.dot"};
        Process p = rt.exec(args);
        p.waitFor();
        add(new JLabel(new ImageIcon("dotFile\\test.dot.png")));
       
	}

	
	public static void main(String[] args) throws IOException, InterruptedException{
		
		Vizualizer frame = new Vizualizer();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 800);
		frame.setVisible(true);
		
		
	}
	
	
	
}
