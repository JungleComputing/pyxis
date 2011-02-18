package ibis.pyxis.t.parallel.activities;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.system.PyxisTContext;

public class ConvolutionActivity<Type> extends OperationActivity<Type> {
    /**
	 * 
	 */
    private static final long serialVersionUID = 2609950829867863834L;
    int dimension;
    double phirad;

    public ConvolutionActivity(PyxisTContext context, int opcode, ActivityIdentifier... parents) {
        super(context, opcode, parents);
    }

    public ConvolutionActivity(PyxisTContext context, int opcode, int dimension, ActivityIdentifier... parents) {
        this(context, opcode, parents);
        this.dimension = dimension;
    }

    public ConvolutionActivity(PyxisTContext context, int opcode, double phirad, ActivityIdentifier... parents) {
        this(context, opcode, parents);
        this.phirad = phirad;
    }

    @Override
    protected ImageData<Type> calculate(ImageData<?>[] parents) {
        ImageData<Type> image = (ImageData<Type>) parents[0];
        ImageData<Type> kernel = (ImageData<Type>) parents[1];
        ImageData<Type> result = null;
        int opcode = getOpcode();
        switch (opcode) {
        case Opcode.CONVOLUTION:
            result = new ImageData<Type>(image.getArray().convolution(
                    kernel.getArray()));
            break;
        case Opcode.CONVOLUTION_1D:
            result = new ImageData<Type>(image.getArray().convolution1d(
                    kernel.getArray(), dimension));
            break;
        case Opcode.CONVOLUTION_ROTATED_1D:
            result = new ImageData<Type>(image.getArray().convolutionRotated1d(
                    kernel.getArray(), phirad));
            break;
        default:
            throw new Error("invalid opcode: " + getOpcode());
        }
        return result;
    }
}
