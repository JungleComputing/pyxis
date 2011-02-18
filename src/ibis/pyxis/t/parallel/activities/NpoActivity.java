package ibis.pyxis.t.parallel.activities;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.system.PyxisTContext;
import jorus.array.Array2d;

public class NpoActivity<Type> extends OperationActivity<Type> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7327921696258168338L;

    private Array2d<Type, ?> tempResult;
    
    public NpoActivity(PyxisTContext context, int opcode, ActivityIdentifier... parents) {
        super(context, opcode, parents);
    }

    /* (non-Javadoc)
     * @see pyxis.t.taskgraph.Node#processEvent(ibis.constellation.ActivityIdentifier, int, java.lang.Object)
     */
    @Override
    protected void processEvent(ActivityIdentifier source, int index,
            ImageData<Type> payload) {
        if(tempResult == null) {
            tempResult = payload.getArray();
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
    protected ImageData<Type> calculate(ImageData<?>[] parentData) {
        ImageData<Type> result = new ImageData<Type>(tempResult);
        tempResult = null;
        return result;
    }
}
