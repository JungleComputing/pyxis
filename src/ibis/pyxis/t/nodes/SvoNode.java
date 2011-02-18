package ibis.pyxis.t.nodes;

import ibis.pyxis.t.parallel.operations.Operation;
import ibis.pyxis.t.parallel.operations.SvoOperation;
import jorus.pixel.Pixel;

public class SvoNode extends Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3839843358570171514L;
	private Pixel<float[]> value;
	private int x, y;

	public SvoNode(int opcode, Pixel<float[]> value, Node source, int x, int y) {
		super(opcode, source);
		this.value = value;
		this.x = x;
		this.y = y;
	}

	@Override
	public Operation createOperation(boolean internal, int outputs, Node... input) {
		return new SvoOperation(identifier(), internal, outputs, getOpcode(), value, x, y, input[0]);
	}

	@Override
	protected Node[] initInput(Node[] parents) {
		return parents;
	}
}
