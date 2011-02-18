package ibis.pyxis.t.parallel.activities;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.system.PyxisTContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ImportActivity<Type> extends OperationActivity<Type> {

    private static final Logger logger = LoggerFactory
            .getLogger(ImportActivity.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = 7734945087892690527L;

    private ImageData<Type> image;

    public ImportActivity(PyxisTContext context, ImageData<Type> image) {
        super(context, Opcode.LOAD, (ActivityIdentifier[]) null);
        this.image = image;
        if (logger.isDebugEnabled()) {
            logger.debug("ImportDescriptor created, state = "
                    + image.getArray().stateString());
        }
    }

    @Override
    protected ImageData<Type> calculate(ImageData<?>[] parents) {
        return image;
    }

}
