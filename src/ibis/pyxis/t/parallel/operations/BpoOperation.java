package ibis.pyxis.t.parallel.operations;

import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.nodes.OperationIdentifier;
import jorus.array.Array2d;
import jorus.pixel.Pixel;

public class BpoOperation extends Operation {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3766885466895359329L;
    private Pixel<float[]> pixel;

    public BpoOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode, Pixel<float[]> pixel,
    		Node input) {
        super(nodeID, internal, outputs, opcode, input);
        this.pixel = pixel;
    }

    public BpoOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode, Node... input) {
        super(nodeID, internal, outputs, opcode, input);
        pixel = null;
    }

    @Override
	protected void processImageEvent(int index, ImageData image) {
		//do nothing
	}
    
    @Override
    protected ImageData execute(ImageData[] parents) {
        ImageData resultImage = null;

        int opcode = getOpcode();
        if (pixel == null) {
            ImageData parent0 = parents[0];
            ImageData parent1 = parents[1];
            Array2d<float[], ?> result;
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
            resultImage = new ImageData(result, identifier());
        } else {
            ImageData parent0 = parents[0];
            Array2d<float[], ?> result;
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

            resultImage = new ImageData(result, identifier());
        }
        return resultImage;
    }
}
