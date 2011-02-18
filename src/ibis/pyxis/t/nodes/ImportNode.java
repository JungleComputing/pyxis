package ibis.pyxis.t.nodes;

import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.parallel.operations.ImportOperation;
import ibis.pyxis.t.parallel.operations.Operation;
import jorus.array.Array2dFloat;

public final class ImportNode extends Node {
    /**
	 * 
	 */
    private static final long serialVersionUID = 7734945087892690527L;
    

    public ImportNode(Array2dFloat<?> data) {
        super(Opcode.LOAD, data);
    }

    @Override
    public Operation createOperation(boolean internal, int outputs, Node... input) {
        try {
            return new ImportOperation(identifier(), internal, outputs, getImage(null));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        return null;

    }
    
//    @Override
//	public boolean restrictToLocal() {
//		return true;
//	}
    
    @Override
	protected Node[] initInput(Node[] parents) {
		return parents;
	}
}
