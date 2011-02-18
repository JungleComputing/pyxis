package ibis.pyxis.t.parallel.operations;

import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.nodes.OperationIdentifier;
import jorus.array.Array2d;

public class ReduceOperation extends Operation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4912070279145139181L;

	public ReduceOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode, Node input) {
		super(nodeID, internal, outputs, opcode, input);
	}

    @Override
	protected void processImageEvent(int index, ImageData image) {
		//do nothing
	}
    
	@Override
    protected ImageData execute(ImageData[] parents) {
		Array2d<float[],?> array;
		Array2d<float[],?> source = parents[0].getArray();
		switch (getOpcode()) {
		case Opcode.REDUCE_SUM:
			array = source.pixSum();
			break;
		case Opcode.REDUCE_PRODUCT:
			array = source.pixProduct();
			break;
		case Opcode.REDUCE_MIN:
			array = source.pixMin();
			break;
		case Opcode.REDUCE_MAX:
			array = source.pixMax();
			break;
		case Opcode.REDUCE_SUP:
			array = source.pixSup();
			break;
		case Opcode.REDUCE_INF:
			array = source.pixInf();
			break;
		default:
			throw new Error("invalid opcode: " + getOpcode());
		}
		return new ImageData(array, identifier());
	}

}
