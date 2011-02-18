package ibis.pyxis.t.nodes;

import ibis.pyxis.t.parallel.operations.Operation;
import ibis.pyxis.t.parallel.operations.UpoOperation;
import jorus.pixel.Pixel;


public final class UpoNode extends Node {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7645403880059955722L;
	private Pixel<float[]> value;
	
	public UpoNode(int opcode, Pixel<float[]> value, Node source) {
		super(opcode, source);
		this.value = value;
	}

	@Override
	public Operation createOperation(boolean internal, int outputs, Node... input) {
		return new UpoOperation(identifier(), internal, outputs,  getOpcode(), value, input[0]);
	}
	
	@Override
	protected Node[] initInput(Node[] parents) {
		return parents;
	}
}
