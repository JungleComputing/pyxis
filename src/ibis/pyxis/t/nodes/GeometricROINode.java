package ibis.pyxis.t.nodes;

import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.parallel.operations.GeometricROIOperation;
import ibis.pyxis.t.parallel.operations.Operation;
import jorus.pixel.Pixel;

public final class GeometricROINode extends Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3011993692703917113L;
	int newImageWidth;
	int newImageHeight;
	Pixel<float[]> background;
	int beginX;
	int beginY;

	public GeometricROINode(int opcode, Node source, int newImageWidth,
			int newImageHeight, Pixel<float[]> background, int beginX, int beginY) {
		super(opcode, source);
		this.newImageWidth = newImageWidth;
		this.newImageHeight = newImageHeight;
		this.background = background;
		this.beginX = beginX;
		this.beginY = beginY;
	}

	@Override
	public Operation createOperation(boolean internal, int outputs, Node... input) {
		int opcode = getOpcode();
		switch (opcode) {
		case Opcode.GEOMETRIC_ROI_RESIZE:
			return new GeometricROIOperation(identifier(), internal, outputs, opcode, newImageWidth, newImageHeight, background, beginX, beginY, input[0]);
		default:
			throw new Error("invalid opcode: " + opcode);
		}
	}
	
	@Override
	protected Node[] initInput(Node[] parents) {
		return parents;
	}
}
