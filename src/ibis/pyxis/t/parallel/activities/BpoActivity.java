package ibis.pyxis.t.parallel.activities;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.system.PyxisTContext;
import jorus.array.Array2d;
import jorus.pixel.Pixel;

public class BpoActivity<Type> extends OperationActivity<Type> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3766885466895359329L;
    private Pixel<Type> pixel;

    public BpoActivity(PyxisTContext context, int opcode, Pixel<Type> pixel,
            ActivityIdentifier parent) {
        super(context, opcode, parent);
        this.pixel = pixel;
    }

    public BpoActivity(PyxisTContext context, int opcode, ActivityIdentifier... parents) {
        super(context, opcode, parents);
        pixel = null;
    }

    @Override
    protected ImageData<Type> calculate(ImageData<?>[] parents) {
        ImageData<Type> resultImage = null;

        int opcode = getOpcode();
        if (pixel == null) {
            ImageData<Type> parent0 = (ImageData<Type>) parents[0];
            ImageData<Type> parent1 = (ImageData<Type>) parents[1];
            Array2d<Type, ?> result;
            switch (opcode) {
            case Opcode.BPO_ABSDIV:
                result = parent0.getArray().absDiv(parent1.getArray(),
                        parent0.inplace());
                break;
            case Opcode.BPO_ADD:
                result = parent0.getArray().add(parent1.getArray(),
                        parent0.inplace());
                break;
            case Opcode.BPO_DIV:
                result = parent0.getArray().div(parent1.getArray(),
                        parent0.inplace());
                break;
            case Opcode.BPO_MAX:
                result = parent0.getArray().max(parent1.getArray(),
                        parent0.inplace());
                break;
            case Opcode.BPO_MIN:
                result = parent0.getArray().min(parent1.getArray(),
                        parent0.inplace());
                break;
            case Opcode.BPO_MUL:
                result = parent0.getArray().mul(parent1.getArray(),
                        parent0.inplace());
                break;
            case Opcode.BPO_NEGDIV:
                result = parent0.getArray().negDiv(parent1.getArray(),
                        parent0.inplace());
                break;
            case Opcode.BPO_POSDIV:
                result = parent0.getArray().posDiv(parent1.getArray(),
                        parent0.inplace());
                break;
            case Opcode.BPO_SUB:
                result = parent0.getArray().sub(parent1.getArray(),
                        parent0.inplace());
                break;
            default:
                throw new Error("no resultImage");
            }
            resultImage = new ImageData<Type>(result);
        } else {
            ImageData<Type> parent0 = (ImageData<Type>) parents[0];
            Array2d<Type, ?> result;
            switch (opcode) {
            case Opcode.BPO_ABSDIV:
                result = parent0.getArray().absDivVal(pixel, parent0.inplace());
                break;
            case Opcode.BPO_ADD:
                result = parent0.getArray().addVal(pixel, parent0.inplace());
                break;
            case Opcode.BPO_DIV:
                result = parent0.getArray().divVal(pixel, parent0.inplace());
                break;
            case Opcode.BPO_MAX:
                result = parent0.getArray().maxVal(pixel, parent0.inplace());
                break;
            case Opcode.BPO_MIN:
                result = parent0.getArray().minVal(pixel, parent0.inplace());
                break;
            case Opcode.BPO_MUL:
                result = parent0.getArray().mulVal(pixel, parent0.inplace());
                break;
            case Opcode.BPO_NEGDIV:
                result = parent0.getArray().negDivVal(pixel, parent0.inplace());
                break;
            case Opcode.BPO_POSDIV:
                result = parent0.getArray().posDivVal(pixel, parent0.inplace());
                break;
            case Opcode.BPO_SUB:
                result = parent0.getArray().subVal(pixel, parent0.inplace());
                break;
            default:
                throw new Error("no resultImage");
            }

            resultImage = new ImageData<Type>(result);
        }
        return resultImage;
    }

}
