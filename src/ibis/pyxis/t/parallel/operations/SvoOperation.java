package ibis.pyxis.t.parallel.operations;

import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.nodes.OperationIdentifier;
import jorus.pixel.Pixel;

public class SvoOperation extends Operation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3839843358570171514L;
	private Pixel<float[]> value;
	private int x, y;

	public SvoOperation(OperationIdentifier nodeID, boolean internal, int opcode, int outputs, Pixel<float[]> value, int x, int y, Node input) {
		super(nodeID, internal, opcode, outputs, input);
		this.value = value;
		this.x = x;
		this.y = y;
	}
    
    @Override
	protected void processImageEvent(int index, ImageData image) {
		//do nothing
	}
    
	@Override
    protected ImageData execute(ImageData[] parents) {
	    ImageData result;
	    ImageData source =  parents[0];
		switch (getOpcode()) {
		case Opcode.SVO_SET:
			result = new ImageData(source.getArray().setSingleValue(
					value, x, y, source.inplace()), identifier());
			break;
		case Opcode.SVO_ADD:
			result = new ImageData(source.getArray().addSingleValue(
					value, x, y, source.inplace()), identifier());
			break;
		default:
			throw new Error("invalid opcode: " + getOpcode());
		}
		return result;
	}
}
