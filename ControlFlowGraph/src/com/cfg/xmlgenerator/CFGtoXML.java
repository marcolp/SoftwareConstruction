package com.cfg.xmlgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;



public class CFGtoXML {
	
	private List<Node> allNodes;
	
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
	
	public CFGtoXML(List<Node> allNodes){
		this.allNodes = allNodes;
		xmlString = new StringBuilder();
	}
	
	public void toXML(){
		addHeader();
		for(int i = 0; i < allNodes.size(); i++){
			Node currentNode = allNodes.get(i);
			//int id = currentNode.getUniqueID();
			int id = currentNode.getID();
			String code = currentNode.getLineNumber() + ", " + currentNode.getLineString();
			//code = StringEscapeUtils.escapeJava(code);
			code = code.replaceAll(QUOTE + "", "'");
			code = code.replaceAll("<", "lessThan");
			code = code.replaceAll(">", "greaterThan");
			addNode(id, code);
			/*
			if(currentNode instanceof IfElseNode){
				IfElseNode ifElseNode = (IfElseNode)currentNode;
				Node trueNode = ifElseNode.getTrueNode();
				Node falseNode = ifElseNode.getFalseNode();
				
				addEdge(id, trueNode.getUniqueID());
				addEdge(id, falseNode.getUniqueID());
				
			}
			else if(currentNode instanceof LoopNode){
				LoopNode loopNode = (LoopNode)currentNode;
				Node beginLoopNode = loopNode.getBeginLoopNode();
				Node endLoopNode = loopNode.getEndLoopNode();
				
				addEdge(id, beginLoopNode.getUniqueID());
				addEdge(id, endLoopNode.getUniqueID());
			}
			else{
				Node nextNode = currentNode.getNextNode();
				if(nextNode != null){
					addEdge(id, nextNode.getUniqueID());
				}
			}
			*/
			List<Node> nextNodes = currentNode.getConnectedTo();
			if(!nextNodes.isEmpty()){
				for(int nodeNumber = 0; nodeNumber < nextNodes.size(); nodeNumber++){
					Node neighbor = nextNodes.get(nodeNumber);
					addEdge(id, neighbor.getID());
				}
			}
			 
		}
		
		String endTag = START + CLOSE + GRAPH_TAG + END;
		xmlString.append(endTag);
		
	}
	
	private void addHeader(){
		
		String header = START + GRAPH_TAG + SPACE;
		header = header + "id=" + QUOTE + "DTMC" + QUOTE + SPACE +
				"edgedefault=" + QUOTE + "directed" + QUOTE + END;
		
		
		
		xmlString.append(header);
		xmlString.append(RETURN);
		
	}
	
	private void addNode(int id, String value){

		String nodeString = START + NODE_TAG + SPACE +
				"id=" + QUOTE + id + QUOTE + SPACE + "value=" +
				QUOTE + value + QUOTE + CLOSE + END;
		
		xmlString.append(TAB);
		xmlString.append(nodeString);
		xmlString.append(RETURN);
	}
	
	private void addEdge(int source, int target){
		
		String edgeString = START + EDGE_TAG + SPACE +
				"source=" + QUOTE + source + QUOTE +
				SPACE + "target=" + QUOTE + target + QUOTE + CLOSE + END;
		
		xmlString.append(TAB);
		xmlString.append(edgeString);
		xmlString.append(RETURN);
	}
	
	
	
	public String getXMLString(){
		
		return xmlString.toString();
	}
	
	public boolean saveXMLTo(String path) throws IOException{
		File file = new File(path);
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			return false;
		}
		
		try {
			writer.append(xmlString.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		writer.close();
		return true;
	}
	
	
}
