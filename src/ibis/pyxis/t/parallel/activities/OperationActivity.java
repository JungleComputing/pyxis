package ibis.pyxis.t.parallel.activities;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.parallel.PyxisTActivity;
import ibis.pyxis.t.system.PyxisTContext;

public abstract class OperationActivity<Type> extends PyxisTActivity<Type> {

    /**
     * 
     */
    private static final long serialVersionUID = -2074986705893661691L;
    
    private final int opcode;

    protected OperationActivity(PyxisTContext context, int opcode, ActivityIdentifier... parents) {
        super(context, parents);
        this.opcode = opcode;
    }
    
    protected final int getOpcode() {
        return opcode;
    }
}
