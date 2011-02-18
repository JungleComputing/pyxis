package ibis.pyxis.t.parallel.operations;

import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.nodes.OperationIdentifier;
import jorus.array.Array2d;

public class NpoOperation extends Operation {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7327921696258168338L;

    private Array2d<float[], ?> tempResult;
    
    public NpoOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode, Node... input) {
        super(nodeID, internal, outputs, opcode, input);
    }

    @Override
    protected void processImageEvent(int index, ImageData payload) {
        if(tempResult == null) {
        	if(payload.inplace()) {
        		tempResult = payload.getArray();
        	} else {
        		tempResult = payload.getArray().clone();
        	}
            return;
        }
        int opcode = getOpcode();
        
        switch (opcode) {
        case Opcode.NPO_MAX:
            tempResult = tempResult.max(payload.getArray(), true);
            break;
        case Opcode.NPO_MIN:
            tempResult = tempResult.max(payload.getArray(), true);
            break;
        case Opcode.NPO_ADD:
            tempResult = tempResult.add(payload.getArray(), true);
            break;
        case Opcode.NPO_MUL:
            tempResult = tempResult.mul(payload.getArray(), true);
            break;
        default:
            throw new Error("no valid image");
        }
    }

    @Override
    protected ImageData execute(ImageData[] parents) {
        ImageData result = new ImageData(tempResult, identifier());
        tempResult = null;
        return result;
    }
}
