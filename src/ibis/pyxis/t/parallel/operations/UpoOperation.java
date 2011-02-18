package ibis.pyxis.t.parallel.operations;

import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.nodes.OperationIdentifier;
import jorus.pixel.Pixel;

public final class UpoOperation extends Operation {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7645403880059955722L;
    private Pixel<float[]> value;

    public UpoOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode, Pixel<float[]> value, Node input) {
        super(nodeID, internal, outputs, opcode, input);
        this.value = value;
    }
    
    @Override
	protected void processImageEvent(int index, ImageData image) {
		//do nothing
	}
    
    @Override
    protected ImageData execute(ImageData[] parents) {
        ImageData image = parents[0];
        ImageData result;

        switch (getOpcode()) {
        case Opcode.UPO_SET_PIXEL:
            result = new ImageData(image.getArray().setVal(value,
                    image.inplace()), identifier());
            break;
        default:
            throw new Error("invalid opcode: " + getOpcode());
        }
        return result;
    }

}
