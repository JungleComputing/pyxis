package ibis.pyxis.t.nodes;

import ibis.pyxis.t.parallel.operations.BpoOperation;
import ibis.pyxis.t.parallel.operations.Operation;
import jorus.pixel.Pixel;

public class BpoNode extends Node {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1430546735985671384L;

    protected Pixel<float[]> pixel;

    public BpoNode(int opcode, Pixel<float[]> pixel, Node source) {
        super(opcode, source);
        this.pixel = pixel;
    }

    public BpoNode(int opcode, Node source1, Node source2) {
        super(opcode, source1, source2);
        pixel = null;
    }

    @Override
    public Operation createOperation(boolean internal, int outputs, Node... parents) {
        if (pixel == null) {
            return new BpoOperation(identifier(), internal, outputs, getOpcode(), parents);
        } else {
            Operation desc = new BpoOperation(identifier(), internal, outputs, getOpcode(), pixel, parents[0]);
            pixel = null;
            return desc;
        }
    }

	@Override
	protected Node[] initInput(Node[] parents) {
		return parents;
	}
}
