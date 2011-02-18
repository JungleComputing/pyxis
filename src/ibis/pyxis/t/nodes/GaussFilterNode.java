package ibis.pyxis.t.nodes;

import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.parallel.operations.GaussFilterOperation;
import ibis.pyxis.t.parallel.operations.Operation;

public final class GaussFilterNode extends Node {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8203823263646732583L;

    private final double truncationX, truncationY;
    private final double sigmaX, sigmaY;
    private final int orderDerivX, orderDerivY;

    private final double phiRad;

    public GaussFilterNode(int opcode, Node source, double sigmaX,
            int orderDerivX, double truncationX, double sigmaY,
            int orderDerivY, double truncationY) {
        super(opcode, source);
        this.orderDerivX = orderDerivX;
        this.orderDerivY = orderDerivY;
        this.sigmaX = sigmaX;
        this.sigmaY = sigmaY;
        this.truncationX = truncationX;
        this.truncationY = truncationY;
        phiRad = 0;
    }

    public GaussFilterNode(int opcode, Node source, double sigmaX,
            int orderDerivX, double truncationX, double sigmaY,
            int orderDerivY, double truncationY, double phiRad) {
        super(opcode, source);
        this.orderDerivX = orderDerivX;
        this.orderDerivY = orderDerivY;
        this.sigmaX = sigmaX;
        this.sigmaY = sigmaY;
        this.truncationX = truncationX;
        this.truncationY = truncationY;
        this.phiRad = phiRad;
    }

    public GaussFilterNode(int opcode, Node source, double sigmaR,
            double sigmaT, double phiDegrees, int derivativeT, double n) {
        super(opcode, source);
        this.sigmaX = sigmaR;
        this.sigmaY = sigmaT;
        phiRad = phiDegrees;
        this.orderDerivY = derivativeT;
        truncationY = n;

        orderDerivX = 0;
        truncationX = 0;
    }

    @Override
    public Operation createOperation(boolean internal, int outputs, Node... input) {
    	int opcode = getOpcode();
        switch (opcode) {
        case Opcode.CONV_GAUSS:
            return new GaussFilterOperation(identifier(), internal, outputs, opcode, sigmaX, orderDerivX,
                    truncationX, sigmaY, orderDerivY, truncationY, input[0]);
        case Opcode.CONV_GAUSS_ANISOTROPIC:
            return new GaussFilterOperation(identifier(), internal, outputs, opcode, sigmaX, orderDerivX,
                    truncationX, sigmaY, orderDerivY, truncationY, phiRad, input[0]);
        case Opcode.CONV_GAUSS_1X2D:
            return new GaussFilterOperation(identifier(), internal, outputs, opcode, sigmaX, sigmaY, phiRad,
                    orderDerivY, truncationY, input[0]);
        default:
            throw new Error("invalid opcode: " + opcode);
        }
    }
    
    @Override
	protected Node[] initInput(Node[] parents) {
		return parents;
	}
    
}
