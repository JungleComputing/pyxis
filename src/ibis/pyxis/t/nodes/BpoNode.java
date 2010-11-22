package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.taskgraph.nodes.BpoDescriptor;
import ibis.pyxis.t.taskgraph.nodes.OperationDescriptor;
import jorus.pixel.Pixel;

public class BpoNode<Type> extends Node<Type> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1430546735985671384L;

    protected Pixel<Type> pixel;

    @SuppressWarnings("unchecked")
    public BpoNode(int opcode, Pixel<Type> pixel, Node<Type> source) {
        super(opcode, source);
        this.pixel = pixel;
    }

    @SuppressWarnings("unchecked")
    public BpoNode(int opcode, Node<Type> source1, Node<Type> source2) {
        super(opcode, source1, source2);
        pixel = null;
    }

    @Override
    protected OperationDescriptor<Type> createOperation(int opcode, ActivityIdentifier... parents) {
        if (pixel == null) {
            return new BpoDescriptor<Type>(getContext(), opcode, parents);
        } else {
            OperationDescriptor<Type> desc = new BpoDescriptor<Type>(getContext(), opcode, pixel, parents[0]);
            pixel = null;
            return desc;
        }
    }
}
