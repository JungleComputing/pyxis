package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.taskgraph.nodes.OperationDescriptor;
import ibis.pyxis.t.taskgraph.nodes.ReduceDescriptor;


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
	protected OperationDescriptor<Type> createOperation(int opcode, ActivityIdentifier... parents) {
		return new ReduceDescriptor<Type>(getContext(), opcode, parents[0]);
	}

}
