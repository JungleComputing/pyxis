package ibis.pyxis.t.nodes;

import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.parallel.operations.ConvolutionOperation;
import ibis.pyxis.t.parallel.operations.Operation;

public class ConvolutionNode extends Node {
    /**
	 * 
	 */
    private static final long serialVersionUID = 2609950829867863834L;
    int dimension;
    double phirad;

    public ConvolutionNode(int opcode, Node source, Node kernel) {
        super(opcode, source, kernel);
    }

    public ConvolutionNode(int opcode, Node source, Node kernel,
            int dimension) {
        this(opcode, source, kernel);
        this.dimension = dimension;
    }

    public ConvolutionNode(int opcode, Node source, Node kernel,
            double phirad) {
        this(opcode, source, kernel);
        this.phirad = phirad;
    }
    
    @Override
	protected Node[] initInput(Node[] parents) {
		return parents;
	}

    @Override
    public Operation createOperation(boolean internal, int outputs, Node... input) {
    	int opcode = getOpcode();
        switch (opcode) {
        case Opcode.CONVOLUTION:
            return new ConvolutionOperation(identifier(), internal, outputs, opcode, input);
        case Opcode.CONVOLUTION_1D:
            return new ConvolutionOperation(identifier(), internal, outputs, opcode, dimension, input);
        case Opcode.CONVOLUTION_ROTATED_1D:
            return new ConvolutionOperation(identifier(), internal, outputs, opcode, phirad, input);
        default:
            throw new Error("invalid opcode: " + opcode);
        }
    }
}
