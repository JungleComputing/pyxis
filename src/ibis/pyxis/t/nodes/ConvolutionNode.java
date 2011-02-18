package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.parallel.activities.ConvolutionActivity;
import ibis.pyxis.t.parallel.activities.OperationActivity;

public class ConvolutionNode<Type> extends Node<Type> {
    /**
	 * 
	 */
    private static final long serialVersionUID = 2609950829867863834L;
    int dimension;
    double phirad;

    @SuppressWarnings("unchecked")
    public ConvolutionNode(int opcode, Node<Type> source, Node<Type> kernel) {
        super(opcode, source, kernel);
    }

    public ConvolutionNode(int opcode, Node<Type> source, Node<Type> kernel,
            int dimension) {
        this(opcode, source, kernel);
        this.dimension = dimension;
    }

    public ConvolutionNode(int opcode, Node<Type> source, Node<Type> kernel,
            double phirad) {
        this(opcode, source, kernel);
        this.phirad = phirad;
    }

    @Override
    protected OperationActivity<Type> createOperation(int opcode, ActivityIdentifier... parents) {
        switch (opcode) {
        case Opcode.CONVOLUTION:
            return new ConvolutionActivity<Type>(getContext(), opcode, parents);
        case Opcode.CONVOLUTION_1D:
            return new ConvolutionActivity<Type>(getContext(), opcode, dimension, parents);
        case Opcode.CONVOLUTION_ROTATED_1D:
            return new ConvolutionActivity<Type>(getContext(), opcode, phirad, parents);
        default:
            throw new Error("invalid opcode: " + opcode);
        }
    }
    
	@Override
	protected Node<Type>[] setParents(Node<Type>[] parents) {
		return parents;
	}
}
