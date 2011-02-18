package ibis.pyxis.t.parallel.activities;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.system.PyxisTContext;
import jorus.array.Array2d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class GaussFilterActivity<Type> extends
        OperationActivity<Type> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4092205568657992741L;

    private static final Logger logger = LoggerFactory
            .getLogger(GaussFilterActivity.class);
    
    private final double truncationX, truncationY;
    private final double sigmaX, sigmaY;
    private final int orderDerivX, orderDerivY;

    private final double phiRad;

    public GaussFilterActivity(PyxisTContext context, int opcode, double sigmaX, int orderDerivX,
            double truncationX, double sigmaY, int orderDerivY,
            double truncationY, ActivityIdentifier parent) {
        super(context, opcode, parent);
        this.orderDerivX = orderDerivX;
        this.orderDerivY = orderDerivY;
        this.sigmaX = sigmaX;
        this.sigmaY = sigmaY;
        this.truncationX = truncationX;
        this.truncationY = truncationY;
        phiRad = 0;
    }

    public GaussFilterActivity(PyxisTContext context, int opcode, double sigmaX, int orderDerivX,
            double truncationX, double sigmaY, int orderDerivY,
            double truncationY, double phiRad, ActivityIdentifier parent) {
        super(context, opcode, parent);
        this.orderDerivX = orderDerivX;
        this.orderDerivY = orderDerivY;
        this.sigmaX = sigmaX;
        this.sigmaY = sigmaY;
        this.truncationX = truncationX;
        this.truncationY = truncationY;
        this.phiRad = phiRad;
    }

    public GaussFilterActivity(PyxisTContext context, int opcode, double sigmaR, double sigmaT,
            double phiDegrees, int derivativeT, double n, ActivityIdentifier parent) {
        super(context, opcode, parent);
        this.sigmaX = sigmaR;
        this.sigmaY = sigmaT;
        phiRad = phiDegrees;
        this.orderDerivY = derivativeT;
        truncationY = n;

        orderDerivX = 0;
        truncationX = 0;
    }

    @Override
    protected ImageData<Type> calculate(ImageData<?>[] parents) {
        ImageData<Type> image = (ImageData<Type>) parents[0];
        ImageData<Type> result = null;

        Array2d<Type, ?> source = image.getArray();
        if (logger.isDebugEnabled()) {
            logger.debug("parents calculated, state = " + source.stateString());
        }

        switch (getOpcode()) {
        case Opcode.CONV_GAUSS:
            result = new ImageData<Type>(source.convGauss2d(sigmaX, orderDerivX,
                    truncationX, sigmaY, orderDerivY, truncationY, false));
            break;
        case Opcode.CONV_GAUSS_ANISOTROPIC:
            result = new ImageData<Type>(source.convGaussAnisotropic2d(sigmaX,
                    orderDerivX, truncationX, sigmaY, orderDerivY, truncationY,
                    phiRad, false));
            break;
        case Opcode.CONV_GAUSS_1X2D:
            result = new ImageData<Type>(source.convGauss1x2d(sigmaX, sigmaY,
                    phiRad, orderDerivY, truncationY));
            break;
        default:
            throw new Error("invalid opcode: " + getOpcode());
        }
        return result;
    }
}
