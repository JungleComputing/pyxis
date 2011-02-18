package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.parallel.activities.OperationActivity;
import ibis.pyxis.t.parallel.activities.ReduceActivity;


public class ReduceNode<Type> extends Node<Type> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4912070279145139181L;
	@SuppressWarnings("unchecked")
	public ReduceNode(int opcode, Node<Type> source) {
		super(opcode, source);
	}
	
	@Override
	protected OperationActivity<Type> createOperation(int opcode, ActivityIdentifier... parents) {
		return new ReduceActivity<Type>(getContext(), opcode, parents[0]);
	}

	
	@Override
	protected Node<Type>[] setParents(Node<Type>[] parents) {
		return parents;
	}
}
