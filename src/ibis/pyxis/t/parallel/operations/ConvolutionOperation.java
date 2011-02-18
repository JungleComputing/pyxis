package ibis.pyxis.t.parallel.operations;

import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.nodes.OperationIdentifier;

public class ConvolutionOperation extends Operation {
    /**
	 * 
	 */
    private static final long serialVersionUID = 2609950829867863834L;
    int dimension;
    double phirad;

    public ConvolutionOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode, Node... input) {
        super(nodeID, internal, outputs,  opcode, input);
    }

    public ConvolutionOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode, int dimension, Node... input) {
        this(nodeID, internal, outputs, opcode, input);
        this.dimension = dimension;
    }

    public ConvolutionOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode, double phirad, Node... input) {
        this(nodeID, internal, outputs, opcode, input);
        this.phirad = phirad;
    }

    @Override
	protected void processImageEvent(int index, ImageData image) {
		//do nothing
	}
    
    @Override
    protected ImageData execute(ImageData[] parents) {
        ImageData image = parents[0];
        ImageData kernel = parents[1];
        ImageData result = null;
        int opcode = getOpcode();
        switch (opcode) {
        case Opcode.CONVOLUTION:
            result = new ImageData(image.getArray().convolution(
                    kernel.getArray()), identifier());
            break;
        case Opcode.CONVOLUTION_1D:
            result = new ImageData(image.getArray().convolution1d(
                    kernel.getArray(), dimension), identifier());
            break;
        case Opcode.CONVOLUTION_ROTATED_1D:
            result = new ImageData(image.getArray().convolutionRotated1d(
                    kernel.getArray(), phirad), identifier());
            break;
        default:
            throw new Error("invalid opcode: " + getOpcode());
        }
        return result;
    }
}
