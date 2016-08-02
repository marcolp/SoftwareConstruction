import java.util.Iterator;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CFGIterator implements Iterator {
	
	private List<Node> nodes;
	private int currentPosition;
	
	public CFGIterator(List<Node> nodes){
		
		this.nodes = nodes;
		currentPosition = 0;
		
	}
	@Override
	public boolean hasNext() {
		if(currentPosition == nodes.size()){
			return false;
		}
		
		return true;
	}

	@Override
	public Object next() {
		Node currentNode = nodes.get(currentPosition);
		currentPosition++;
		return currentNode;
	}

}
