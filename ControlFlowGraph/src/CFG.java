import java.util.Iterator;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CFG implements Iterable{
	
	private List<Node> nodes;
	
	
	public CFG(List<Node> newNodes){
		
		nodes = newNodes;
		
	}

	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return new CFGIterator(nodes);
	}
	
}
