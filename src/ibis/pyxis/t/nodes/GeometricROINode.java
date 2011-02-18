package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.parallel.activities.GeometricROIActivity;
import ibis.pyxis.t.parallel.activities.OperationActivity;
import jorus.pixel.Pixel;

public final class GeometricROINode<Type> extends Node<Type> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3011993692703917113L;
	int newImageWidth;
	int newImageHeight;
	Pixel<Type> background;
	int beginX;
	int beginY;

	@SuppressWarnings("unchecked")
	public GeometricROINode(int opcode, Node<Type> source, int newImageWidth,
			int newImageHeight, Pixel<Type> background, int beginX, int beginY) {
		super(opcode, source);
		this.newImageWidth = newImageWidth;
		this.newImageHeight = newImageHeight;
		this.background = background;
		this.beginX = beginX;
		this.beginY = beginY;
	}

	@Override
	protected OperationActivity<Type> createOperation(int opcode, ActivityIdentifier... parents) {
		switch (opcode) {
		case Opcode.GEOMETRIC_ROI_RESIZE:
			return new GeometricROIActivity<Type>(getContext(), opcode, newImageWidth, newImageHeight, background, beginX, beginY, parents[0]);
		default:
			throw new Error("invalid opcode: " + opcode);
		}
	}
	
	@Override
	protected Node<Type>[] setParents(Node<Type>[] parents) {
		return parents;
	}
}
