package ibis.pyxis.t.taskgraph.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.system.PyxisTContext;
import ibis.pyxis.t.taskgraph.Node;

public abstract class OperationDescriptor<Type> extends Node<ImageData<Type>> {

    /**
     * 
     */
    private static final long serialVersionUID = -2074986705893661691L;
    
    private final int opcode;

    protected OperationDescriptor(PyxisTContext context, int opcode, ActivityIdentifier... parents) {
        super(context, parents);
        this.opcode = opcode;
    }
    
    protected final int getOpcode() {
        return opcode;
    }
}
