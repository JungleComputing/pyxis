package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.taskgraph.nodes.ImportDescriptor;
import ibis.pyxis.t.taskgraph.nodes.OperationDescriptor;

public final class ImportNode<Type> extends Node<Type> {
    /**
	 * 
	 */
    private static final long serialVersionUID = 7734945087892690527L;
    

    //TODO Fix!
    public ImportNode(ImageData<Type> data) {
        super(Opcode.LOAD, data);
    }

    @Override
    protected OperationDescriptor<Type> createOperation(int opcode,
            ActivityIdentifier... parents) {
        try {
            return new ImportDescriptor<Type>(getContext(), getImage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        return null;

    }
}
