package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.parallel.activities.ImportActivity;
import ibis.pyxis.t.parallel.activities.OperationActivity;

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
    protected OperationActivity<Type> createOperation(int opcode,
            ActivityIdentifier... parents) {
        try {
            return new ImportActivity<Type>(getContext(), getImage(null));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        return null;

    }
    
	@Override
	protected Node<Type>[] setParents(Node<Type>[] parents) {
		return parents;
	}
}
