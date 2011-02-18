package ibis.pyxis.t.parallel.operations;

import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.nodes.OperationIdentifier;
import jorus.pixel.Pixel;

public class GeometricMatrixOperation extends Operation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6450567526285348839L;
	double alpha;
	boolean linearInterpolation;
	boolean resize;
	Pixel<float[]> background;

	/**
	 * Rotates the image around the z-axis. The center of rotation is the center
	 * of the image
	 * 
	 * @param alpha
	 *            The rotation angle in degrees (positive alpha leads to
	 *            rotation in counterclockwise direction)
	 * @param linearInterpolation
	 *            true: use linear interpolation false: use nearest neighbour
	 *            fit
	 * @param resize
	 *            when false, corners may be chopped off the image
	 * @param background
	 *            the value of the background pixels
	 */
	public GeometricMatrixOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode,
			double alpha, boolean linearInterpolation, boolean resize,
			Pixel<float[]> background, Node input) {
		super(nodeID, internal, outputs, opcode, input);
		this.alpha = alpha;
		this.linearInterpolation = linearInterpolation;
		this.resize = resize;
		this.background = background;
	}

	static int count = 0;

    @Override
	protected void processImageEvent(int index, ImageData image) {
		//do nothing
	}
    
	@Override
    protected ImageData execute(ImageData[] parents) {
		ImageData image = parents[0];
		ImageData result = null;

		switch (getOpcode()) {
		case Opcode.GEOMETRIC_ROTATE:
			result = new ImageData(image.getArray().rotate(alpha,
					linearInterpolation, resize, background), identifier());
			break;
		// case Opcode.GEOMETRIC_RESIZE:
		// result = new JorusImage<T>(image.getArray().resize());
		// break;
		default:
			throw new Error("invalid opcode: " + getOpcode());
		}
		return result;
	}

}
