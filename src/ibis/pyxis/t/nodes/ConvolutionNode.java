package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.taskgraph.nodes.ConvolutionDescriptor;
import ibis.pyxis.t.taskgraph.nodes.OperationDescriptor;

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
    protected OperationDescriptor<Type> createOperation(int opcode, ActivityIdentifier... parents) {
        switch (opcode) {
        case Opcode.CONVOLUTION:
            return new ConvolutionDescriptor<Type>(getContext(), opcode, parents);
        case Opcode.CONVOLUTION_1D:
            return new ConvolutionDescriptor<Type>(getContext(), opcode, dimension, parents);
        case Opcode.CONVOLUTION_ROTATED_1D:
            return new ConvolutionDescriptor<Type>(getContext(), opcode, phirad, parents);
        default:
            throw new Error("invalid opcode: " + opcode);
        }
    }
}
