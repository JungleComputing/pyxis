package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.parallel.activities.OperationActivity;
import ibis.pyxis.t.parallel.activities.UpoActivity;
import jorus.pixel.Pixel;


public final class UpoNode<Type> extends Node<Type> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7645403880059955722L;
	private Pixel<Type> value;
	
	@SuppressWarnings("unchecked")
	public UpoNode(int opcode, Pixel<Type> value, Node<Type> source) {
		super(opcode, source);
		this.value = value;
	}

	@Override
	protected OperationActivity<Type> createOperation(int opcode, ActivityIdentifier... parents) {
		return new UpoActivity<Type>(getContext(), opcode, value, parents[0]);
	}
	
	@Override
	protected Node<Type>[] setParents(Node<Type>[] parents) {
		return parents;
	}

}
