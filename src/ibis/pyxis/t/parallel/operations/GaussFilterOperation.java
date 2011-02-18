package ibis.pyxis.t.parallel.operations;

import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.nodes.OperationIdentifier;
import jorus.array.Array2d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class GaussFilterOperation extends
        Operation {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4092205568657992741L;

    private static final Logger logger = LoggerFactory
            .getLogger(GaussFilterOperation.class);
    
    private final double truncationX, truncationY;
    private final double sigmaX, sigmaY;
    private final int orderDerivX, orderDerivY;

    private final double phiRad;

    public GaussFilterOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode, double sigmaX, int orderDerivX,
            double truncationX, double sigmaY, int orderDerivY,
            double truncationY, Node input) {
        super(nodeID, internal, outputs, opcode, input);
        this.orderDerivX = orderDerivX;
        this.orderDerivY = orderDerivY;
        this.sigmaX = sigmaX;
        this.sigmaY = sigmaY;
        this.truncationX = truncationX;
        this.truncationY = truncationY;
        phiRad = 0;
    }

    public GaussFilterOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode, double sigmaX, int orderDerivX,
            double truncationX, double sigmaY, int orderDerivY,
            double truncationY, double phiRad, Node input) {
        super(nodeID, internal, outputs, opcode, input);
        this.orderDerivX = orderDerivX;
        this.orderDerivY = orderDerivY;
        this.sigmaX = sigmaX;
        this.sigmaY = sigmaY;
        this.truncationX = truncationX;
        this.truncationY = truncationY;
        this.phiRad = phiRad;
    }

    public GaussFilterOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode, double sigmaR, double sigmaT,
            double phiDegrees, int derivativeT, double n, Node input) {
        super(nodeID, internal, outputs, opcode, input);
        this.sigmaX = sigmaR;
        this.sigmaY = sigmaT;
        phiRad = phiDegrees;
        this.orderDerivY = derivativeT;
        truncationY = n;

        orderDerivX = 0;
        truncationX = 0;
    }

    @Override
	protected void processImageEvent(int index, ImageData image) {
		//do nothing
	}
    
    @Override
    protected ImageData execute(ImageData[] parents) {
        ImageData image = parents[0];
        ImageData result = null;

        Array2d<float[], ?> source = image.getArray();
        if (logger.isDebugEnabled()) {
            logger.debug("parents calculated, state = " + source.stateString());
        }

        switch (getOpcode()) {
        case Opcode.CONV_GAUSS:
            result = new ImageData(source.convGauss2d(sigmaX, orderDerivX,
                    truncationX, sigmaY, orderDerivY, truncationY, false), identifier());
            break;
        case Opcode.CONV_GAUSS_ANISOTROPIC:
            result = new ImageData(source.convGaussAnisotropic2d(sigmaX,
                    orderDerivX, truncationX, sigmaY, orderDerivY, truncationY,
                    phiRad, false), identifier());
            break;
        case Opcode.CONV_GAUSS_1X2D:
            result = new ImageData(source.convGauss1x2d(sigmaX, sigmaY,
                    phiRad, orderDerivY, truncationY), identifier());
            break;
        default:
            throw new Error("invalid opcode: " + getOpcode());
        }
        return result;
    }
}
