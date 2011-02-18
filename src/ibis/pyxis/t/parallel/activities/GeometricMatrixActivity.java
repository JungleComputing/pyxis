package ibis.pyxis.t.parallel.activities;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.system.PyxisTContext;
import jorus.pixel.Pixel;

public class GeometricMatrixActivity<Type> extends OperationActivity<Type> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6450567526285348839L;
	double alpha;
	boolean linearInterpolation;
	boolean resize;
	Pixel<Type> background;

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
	public GeometricMatrixActivity(PyxisTContext context, int opcode,
			double alpha, boolean linearInterpolation, boolean resize,
			Pixel<Type> background, ActivityIdentifier parent) {
		super(context, opcode, parent);
		this.alpha = alpha;
		this.linearInterpolation = linearInterpolation;
		this.resize = resize;
		this.background = background;
	}

	static int count = 0;

	@Override
	protected ImageData<Type> calculate(ImageData<?>[] parents) {
		ImageData<Type> image = (ImageData<Type>) parents[0];
		ImageData<Type> result = null;

		switch (getOpcode()) {
		case Opcode.GEOMETRIC_ROTATE:
			result = new ImageData<Type>(image.getArray().rotate(alpha,
					linearInterpolation, resize, background));
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
