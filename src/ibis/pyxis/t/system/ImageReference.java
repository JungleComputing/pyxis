package ibis.pyxis.t.system;

import ibis.pyxis.t.ImageT;
import ibis.pyxis.t.Node;

import java.lang.ref.PhantomReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * When an {@link ImageImpl} is not longer used by the application and is
 * garbage collected, this reference is used to notify the corresponding
 * {@link Node} that its data is not used anymore outside the scope of the
 * runtime system.
 * 
 * @author Timo van Kessel
 */
public class ImageReference extends PhantomReference<ImageT<?, ?>> {

    private static final Logger logger = LoggerFactory
            .getLogger(ImageReference.class);

    private static final GraphController graphController = GraphController
            .getController();
    private final Node<?> node;

    /**
     * @param referent
     *            the {@link ImageImpl} for which this reference will be created
     * @param node
     *            the {@link Node} corresponding to the image
     */
    public ImageReference(final ImageT<?, ?> referent, final Node<?> node) {
        super(referent, graphController.queue);
        this.node = node;
        graphController.addReference(this);
    }

    protected void destroy() {
        node.referenceLost(graphController.getConstellation());
        graphController.removeReference(this);
        clear();
        if (logger.isDebugEnabled()) {
            logger.debug("Image destroyed");
        }
    }
}
