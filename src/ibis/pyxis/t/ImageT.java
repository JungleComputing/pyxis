package ibis.pyxis.t;

import java.nio.ByteBuffer;

import ibis.imaging4j.Format;
import ibis.pyxis.Image;
import ibis.pyxis.t.nodes.BpoNode;
import ibis.pyxis.t.nodes.ConvolutionNode;
import ibis.pyxis.t.nodes.GaussFilterNode;
import ibis.pyxis.t.nodes.GeometricMatrixNode;
import ibis.pyxis.t.nodes.GeometricROINode;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.nodes.NpoNode;
import ibis.pyxis.t.nodes.ReduceNode;
import ibis.pyxis.t.nodes.SvoNode;
import ibis.pyxis.t.nodes.UpoNode;
import ibis.pyxis.t.system.ImageReference;
import ibis.pyxis.t.system.PyxisT;
import jorus.array.Array2d;
import jorus.pixel.Pixel;

/**
 * @author Timo van Kessel
 * 
 * @param <Type>
 * @param <U>
 */
public class ImageT extends Image {

	private final Node node;
	protected int width, height;
	protected int extent;
	private final PyxisT pyxis;

	public ImageT(Node node, int width, int height, int extent, PyxisT pyxis) {
		this.node = node;
		this.width = width;
		this.height = height;
		this.extent = extent;
		new ImageReference(this, node, pyxis.getController());
		this.pyxis = pyxis;
	}

	protected ImageT createNewImage(Node node, int width, int height, int extent) {
		return new ImageT(node, width, height, extent, getPyxis());
	}

	protected final PyxisT getPyxis() {
		return pyxis;
	}

	protected final Node getNode() {
		return node;
	}

	@Override
	public float[] getData() throws Exception {
//		Array2d<float[], ?> array = getPyxis().getImage(node).getArray();
		Array2d<float[], ?> array = getNode().getImage(getPyxis()).getArray();

		return array.clone(0, 0).getData();
	}

	@Override
	public ibis.imaging4j.Image toImaging4j() throws Exception {

		// FIXME normalize broken --> now implemented in Imaging4j?
		float[] data = /* normalize(). */getData();
		ByteBuffer buffer = ByteBuffer.allocate(data.length * (Float.SIZE / 8));
		buffer.asFloatBuffer().put(data);
		ibis.imaging4j.Image result;
		switch (extent) {
		case 1:
			result = new ibis.imaging4j.Image(Format.PYXIS_FLOATGREY, width,
					height, buffer);
			break;
		case 4:
			result = new ibis.imaging4j.Image(Format.PYXIS_FLOATARGB, width,
					height, buffer);
			break;
		default:
			result = null;// exception
		}
		return result;
	}

	// @Override
	// public final JorusImage<T> getData() throws Exception {
	// JorusImage<T> result = getNode().getImageData();
	// return result;
	// }

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	public final int getDepth() {
		return 0;
	}

	public final int getExtent() {
		return extent;
	}

	@Override
	public final ImageT set(Pixel<float[]> value, int x, int y) {
		Node node = new SvoNode(Opcode.SVO_SET, value, getNode(), x, y);
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	/* UPO */

	@Override
	public final ImageT set(Pixel<float[]> value) {
		Node node = new UpoNode(Opcode.UPO_SET_PIXEL, value, getNode());
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	/* BPO */

	@Override
	public final ImageT add(Pixel<float[]> pixel) {
		Node node = new BpoNode(Opcode.BPO_ADD, pixel, getNode());
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT div(Pixel<float[]> pixel) {
		Node node = new BpoNode(Opcode.BPO_DIV, pixel, getNode());
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT max(Pixel<float[]> pixel) {
		Node node = new BpoNode(Opcode.BPO_MAX, pixel, getNode());
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT min(Pixel<float[]> pixel) {
		Node node = new BpoNode(Opcode.BPO_MIN, pixel, getNode());
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT mul(Pixel<float[]> pixel) {
		Node node = new BpoNode(Opcode.BPO_MUL, pixel, getNode());
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT sub(Pixel<float[]> pixel) {
		Node node = new BpoNode(Opcode.BPO_SUB, pixel, getNode());
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT negdiv(Pixel<float[]> pixel) {
		Node node = new BpoNode(Opcode.BPO_NEGDIV, pixel, getNode());
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT posdiv(Pixel<float[]> pixel) {
		Node node = new BpoNode(Opcode.BPO_POSDIV, pixel, getNode());
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT absdiv(Pixel<float[]> pixel) {
		Node node = new BpoNode(Opcode.BPO_ABSDIV, pixel, getNode());
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT add(Image source2) {
		Node node = new BpoNode(Opcode.BPO_ADD, getNode(),
				getNode(source2));
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT div(Image source2) {
		Node node = new BpoNode(Opcode.BPO_DIV, getNode(),
				getNode(source2));
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT max(Image source2) {
		Node node = new BpoNode(Opcode.BPO_MAX, getNode(),
				getNode(source2));
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT min(Image source2) {
		Node node = new BpoNode(Opcode.BPO_MIN, getNode(),
				getNode(source2));
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT mul(Image source2) {
		Node node = new BpoNode(Opcode.BPO_MUL, getNode(),
				getNode(source2));
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT sub(Image source2) {
		Node node = new BpoNode(Opcode.BPO_SUB, getNode(),
				getNode(source2));
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT negdiv(Image source2) {
		Node node = new BpoNode(Opcode.BPO_NEGDIV, getNode(),
				getNode(source2));
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT posdiv(Image source2) {
		Node node = new BpoNode(Opcode.BPO_POSDIV, getNode(),
				getNode(source2));
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT absdiv(Image source2) {
		Node node = new BpoNode(Opcode.BPO_ABSDIV, getNode(),
				getNode(source2));
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public final ImageT add(Pixel<float[]> value, int x, int y) {
		Node node = new SvoNode(Opcode.SVO_ADD, value, getNode(), x, y);
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	/* NPO */

	@Override
	public final ImageT collectMax() {
		Node node = new NpoNode(Opcode.NPO_MAX, getNode());
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public ImageT collectMin() {
		Node node = new NpoNode(Opcode.NPO_MIN, getNode());
		return createNewImage(node, getWidth(), getHeight(), extent);
	}

	@Override
	public ImageT collect(Image other) {
		Node myNode = getNode();
		if (myNode instanceof NpoNode) {
			((NpoNode) myNode).addSource(getNode(other));
			return this;
		} else {
			return null; // FIXME error
		}
	}

	/* Reduce */

	@Override
	public final ImageT pixSum() {
		Node node = new ReduceNode(Opcode.REDUCE_SUM, getNode());
		return createNewImage(node, 1, 1, extent);
	}

	@Override
	public final ImageT pixMax() {
		Node node = new ReduceNode(Opcode.REDUCE_MAX, getNode());
		return createNewImage(node, 1, 1, extent);
	}

	@Override
	public final ImageT pixMin() {
		Node node = new ReduceNode(Opcode.REDUCE_MIN, getNode());
		return createNewImage(node, 1, 1, extent);
	}

	@Override
	public final ImageT pixProduct() {
		Node node = new ReduceNode(Opcode.REDUCE_PRODUCT, getNode());
		return createNewImage(node, 1, 1, extent);
	}

	@Override
	public final ImageT pixSup() {
		Node node = new ReduceNode(Opcode.REDUCE_SUP, getNode());
		return createNewImage(node, 1, 1, extent);
	}

	@Override
	public final ImageT pixInf() {
		Node node = new ReduceNode(Opcode.REDUCE_INF, getNode());
		return createNewImage(node, 1, 1, extent);
	}

	/* Convolution */

	@Override
	public final ImageT convKernelSeparated(Image kernel) {
		return convKernelSeparated2d(kernel, kernel);
	}

	@Override
	public final ImageT convKernelSeparated2d(Image kernelX, Image kernelY) {
		Node nodeX = new ConvolutionNode(Opcode.CONVOLUTION_1D,
				getNode(), getNode(kernelX), 0);
		Node nodeY = new ConvolutionNode(Opcode.CONVOLUTION_1D,
				nodeX, getNode(kernelY), 1);
		return createNewImage(nodeY, width, height, extent);
	}

	@Override
	public final ImageT convolution(Image kernel) {
		Node node = new ConvolutionNode(Opcode.CONVOLUTION,
				getNode(), getNode(kernel));
		return createNewImage(node, width, height, extent);
	}

	@Override
	public final ImageT convolution1d(Image kernel, int dimension) {
		Node node = new ConvolutionNode(Opcode.CONVOLUTION_1D,
				getNode(), getNode(kernel), dimension);
		return createNewImage(node, width, height, extent);
	}

	@Override
	public final ImageT convolutionRotated1d(Image kernel, double phirad) {
		Node node = new ConvolutionNode(
				Opcode.CONVOLUTION_ROTATED_1D, getNode(),
				getNode(kernel), phirad);
		return createNewImage(node, width, height, extent);
	}

	/* ConvGauss */

	@Override
	public final ImageT gauss(double sigma, double truncation) {
		return gaussDerivative2d(sigma, 0, 0, truncation);
	}

	@Override
	public final ImageT gaussDerivative2d(double sigma, int orderDerivX,
			int orderDerivY, double truncation) {
		return convGauss2d(sigma, orderDerivX, truncation, sigma, orderDerivY,
				truncation);
	}

	@Override
	public final ImageT convGauss2d(double sigmaX, int orderDerivX,
			double truncationX, double sigmaY, int orderDerivY,
			double truncationY) {
		Node node = new GaussFilterNode(Opcode.CONV_GAUSS,
				getNode(), sigmaX, orderDerivX, truncationX, sigmaY,
				orderDerivY, truncationY);
		return createNewImage(node, width, height, extent);
	}

	@Override
	public ImageT convGauss1x2d(double sigmaR, double sigmaT, double phiDegrees,
			int derivativeT, double n) {
		Node node = new GaussFilterNode(Opcode.CONV_GAUSS_1X2D,
				getNode(), sigmaR, sigmaT, phiDegrees, derivativeT, n);
		return createNewImage(node, width, height, extent);
	}

	@Override
	public final ImageT convGaussAnisotropic2d(double sigmaU, int orderDerivU,
			double truncationU, double sigmaV, int orderDerivV,
			double truncationV, double phiRad) {
		Node node = new GaussFilterNode(
				Opcode.CONV_GAUSS_ANISOTROPIC, getNode(), sigmaU,
				orderDerivU, truncationU, sigmaV, orderDerivV, truncationV,
				phiRad);
		return createNewImage(node, width, height, extent);
	}

	/* Geometric */

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
	 * @return the rotated image
	 */
	@Override
	public final ImageT rotate(double alpha, boolean linearInterpolation,
			boolean resize, Pixel<float[]> background) {

		GeometricMatrixNode node = new GeometricMatrixNode(
				Opcode.GEOMETRIC_ROTATE, getNode(), alpha, linearInterpolation,
				resize, background);
		if (resize) {
			double theta = (alpha % 360) / 360 * 2 * Math.PI;
			double sinA = Math.abs(Math.sin(theta));
			double cosA = Math.abs(Math.cos(theta));

			// TODO Rounding should be OK and the same as used in Jorus
			int newWidth = (int) ((width * cosA + height * sinA) + .5);
			int newHeight = (int) ((width * sinA + height * cosA) + .5);

			return createNewImage(node, newWidth, newHeight, extent);
		} else {
			return createNewImage(node, width, height, extent);
		}

	}

	private final ImageT geometricOpROI(int newImWidth, int newImHeight,
			Pixel<float[]> background, int beginX, int beginY) {
		Node node = new GeometricROINode(
				Opcode.GEOMETRIC_ROI_RESIZE, getNode(), newImWidth,
				newImHeight, background, beginX, beginY);
		return createNewImage(node, newImWidth, newImHeight, extent);
	}

	@Override
	public final ImageT extend(int newImWidth, int newImHeight,
			Pixel<float[]> background, int beginX, int beginY) {
		return geometricOpROI(newImWidth, newImHeight, background, -beginX,
				-beginY);
	}

	@Override
	public final ImageT restrict(int beginX, int beginY, int endX, int endY) {
		int newImWidth = endX - beginX + 1;
		int newImHeight = endY - beginY + 1;
		return geometricOpROI(newImWidth, newImHeight, null, beginX, beginY);
	}

	// @Override
	// public final ImageT transform(Matrix transformationMatrix,
	// boolean forward, boolean linearInterpolation, boolean resize,
	// Pixel<T> background) {
	// Matrix matrix = transformationMatrix.copy();
	// GeometricMatrixNode<T> node = new GeometricMatrixNode<T>(
	// Opcode.GEOMETRIC_TRANSFORMATION, getNode(), getWidth(),
	// getHeight(), extent, matrix, forward, linearInterpolation,
	// resize, background);
	// return createNewImage(node, node.getNewWidth(), node.getNewHeight(),
	// extent);
	// }

}
