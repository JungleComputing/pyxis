package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.taskgraph.nodes.OperationDescriptor;
import ibis.pyxis.t.taskgraph.nodes.SvoDescriptor;
import jorus.pixel.Pixel;

public class SvoNode<Type> extends Node<Type> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3839843358570171514L;
	private Pixel<Type> value;
	private int x, y;

	@SuppressWarnings("unchecked")
	public SvoNode(int opcode, Pixel<Type> value, Node<Type> source, int x, int y) {
		super(opcode, source);
		this.value = value;
		this.x = x;
		this.y = y;
	}

	@Override
	protected OperationDescriptor<Type> createOperation(int opcode, ActivityIdentifier... parents) {
		return new SvoDescriptor<Type>(getContext(), opcode, value, x, y, parents[0]);
	}
}
