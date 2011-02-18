package ibis.pyxis.t.nodes;

import ibis.pyxis.t.parallel.operations.Operation;
import ibis.pyxis.t.parallel.operations.ReduceOperation;


public class ReduceNode extends Node {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4912070279145139181L;

	public ReduceNode(int opcode, Node source) {
		super(opcode, source);
	}
	
	@Override
	public Operation createOperation(boolean internal, int outputs, Node... input) {
		return new ReduceOperation(identifier(), internal, outputs, getOpcode(), input[0]);
	}

	@Override
	protected Node[] initInput(Node[] parents) {
		return parents;
	}
}
