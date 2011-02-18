package ibis.pyxis.t.parallel.operations;

import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.nodes.OperationIdentifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ImportOperation extends Operation {

    private static final Logger logger = LoggerFactory
            .getLogger(ImportOperation.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = 7734945087892690527L;

    private ImageData image;

    public ImportOperation(OperationIdentifier nodeID, boolean internal, int outputs, ImageData image) {
        super(nodeID, internal, outputs, Opcode.LOAD, (Node[]) null);
        this.image = image;
        if (logger.isDebugEnabled()) {
            logger.debug("ImportDescriptor created, state = "
                    + image.getArray().stateString());
        }
    }

    @Override
	protected void processImageEvent(int index, ImageData image) {
		//do nothing
	}
    
    @Override
    protected ImageData execute(ImageData[] parents) {
        return image;
    }

}
