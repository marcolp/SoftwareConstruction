import java.util.Iterator;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CFG implements Iterable{
	
	private List<Node> nodes;
	private String name;
	
	public CFG(List<Node> newNodes){
		name = "";
		nodes = newNodes;
		
	}

	public void setName(String newName){
	  this.name = newName;
	}
	
	public String getName(){
	  return name;
	}
	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return new CFGIterator(nodes);
	}
	
}
