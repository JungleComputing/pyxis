package ibis.pyxis.t;

import ibis.pyxis.Image;
import ibis.pyxis.t.nodes.BpoNode;
import ibis.pyxis.t.nodes.ConvolutionNode;
import ibis.pyxis.t.nodes.GaussFilterNode;
import ibis.pyxis.t.nodes.GeometricMatrixNode;
import ibis.pyxis.t.nodes.GeometricROINode;
import ibis.pyxis.t.nodes.NpoNode;
import ibis.pyxis.t.nodes.ReduceNode;
import ibis.pyxis.t.nodes.SvoNode;
import ibis.pyxis.t.nodes.UpoNode;
import ibis.pyxis.t.system.ImageReference;
import jorus.pixel.Pixel;

/**
 * @author Timo van Kessel
 * 
 * @param <Type>
 * @param <U>
 */
public abstract class ImageT<Type, U extends ImageT<Type,U>> implements Image<Type, ImageT<Type,U>> {

    private final Node<Type> node;
    protected int width, height;
    protected int extent;

    protected ImageT(Node<Type> node, int width, int height, int extent) {
        this.node = node;
        this.width = width;
        this.height = height;
        this.extent = extent;
        new ImageReference(this, node);
    }

    protected abstract U createNewImage(Node<Type> node, int width, int height,
            int extent);

    protected final Node<Type> getNode() {
        return node;
    }

//    @Override
//    public final JorusImage<T> getData() throws Exception {
//        JorusImage<T> result = getNode().getImageData();
//        return result;
//    }

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
    public final U set(Pixel<Type> value, int x, int y) {
        Node<Type> node = new SvoNode<Type>(Opcode.SVO_SET, value, this.getNode(), x,
                y);
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    /* UPO */

    @Override
    public final U set(Pixel<Type> value) {
        Node<Type> node = new UpoNode<Type>(Opcode.UPO_SET_PIXEL, value, this
                .getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    /* BPO */

    @Override
    public final U add(Pixel<Type> pixel) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_ADD, pixel, this.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U div(Pixel<Type> pixel) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_DIV, pixel, this.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U max(Pixel<Type> pixel) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_MAX, pixel, this.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U min(Pixel<Type> pixel) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_MIN, pixel, this.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U mul(Pixel<Type> pixel) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_MUL, pixel, this.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U sub(Pixel<Type> pixel) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_SUB, pixel, this.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U negdiv(Pixel<Type> pixel) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_NEGDIV, pixel, this.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U posdiv(Pixel<Type> pixel) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_POSDIV, pixel, this.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U absdiv(Pixel<Type> pixel) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_ABSDIV, pixel, this.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U add(ImageT<Type,U> source2) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_ADD, this.getNode(), source2
                .getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U div(ImageT<Type,U> source2) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_DIV, this.getNode(), source2
                .getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U max(ImageT<Type,U> source2) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_MAX, this.getNode(), source2
                .getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U min(ImageT<Type,U> source2) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_MIN, this.getNode(), source2
                .getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U mul(ImageT<Type,U> source2) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_MUL, this.getNode(), source2
                .getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U sub(ImageT<Type,U> source2) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_SUB, this.getNode(), source2
                .getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U negdiv(ImageT<Type,U> source2) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_NEGDIV, this.getNode(),
                source2.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U posdiv(ImageT<Type,U> source2) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_POSDIV, this.getNode(),
                source2.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U absdiv(ImageT<Type,U> source2) {
        Node<Type> node = new BpoNode<Type>(Opcode.BPO_ABSDIV, this.getNode(),
                source2.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public final U add(Pixel<Type> value, int x, int y) {
        Node<Type> node = new SvoNode<Type>(Opcode.SVO_ADD, value, this.getNode(), x,
                y);
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    /* NPO */

    @Override
    public final U collectMax() {
        Node<Type> node = new NpoNode<Type>(Opcode.NPO_MAX, this.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @Override
    public U collectMin() {
        Node<Type> node = new NpoNode<Type>(Opcode.NPO_MIN, this.getNode());
        return createNewImage(node, getWidth(), getHeight(), extent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public U collect(ImageT<Type,U> other) {
        Node<Type> myNode = getNode();
        if (myNode instanceof NpoNode<?>) {
            ((NpoNode<Type>) myNode).addSource(other.getNode());
            return (U) this;
        } else {
            return null; // FIXME error
        }
    }

    /* Reduce */

    @Override
    public final U pixSum() {
        Node<Type> node = new ReduceNode<Type>(Opcode.REDUCE_SUM, getNode());
        return createNewImage(node, 1, 1, extent);
    }

    @Override
    public final U pixMax() {
        Node<Type> node = new ReduceNode<Type>(Opcode.REDUCE_MAX, getNode());
        return createNewImage(node, 1, 1, extent);
    }

    @Override
    public final U pixMin() {
        Node<Type> node = new ReduceNode<Type>(Opcode.REDUCE_MIN, getNode());
        return createNewImage(node, 1, 1, extent);
    }

    @Override
    public final U pixProduct() {
        Node<Type> node = new ReduceNode<Type>(Opcode.REDUCE_PRODUCT, getNode());
        return createNewImage(node, 1, 1, extent);
    }

    @Override
    public final U pixSup() {
        Node<Type> node = new ReduceNode<Type>(Opcode.REDUCE_SUP, getNode());
        return createNewImage(node, 1, 1, extent);
    }

    @Override
    public final U pixInf() {
        Node<Type> node = new ReduceNode<Type>(Opcode.REDUCE_INF, getNode());
        return createNewImage(node, 1, 1, extent);
    }

    /* Convolution */

    @Override
    public final U convKernelSeparated(ImageT<Type,U> kernel) {
        return convKernelSeparated2d(kernel, kernel);
    }

    @Override
    public final U convKernelSeparated2d(ImageT<Type,U> kernelX, ImageT<Type,U> kernelY) {
        Node<Type> nodeX = new ConvolutionNode<Type>(Opcode.CONVOLUTION_1D, this
                .getNode(), kernelX.getNode(), 0);
        Node<Type> nodeY = new ConvolutionNode<Type>(Opcode.CONVOLUTION_1D, nodeX,
                kernelY.getNode(), 1);
        return createNewImage(nodeY, width, height, extent);
    }

    @Override
    public final U convolution(ImageT<Type,U> kernel) {
        Node<Type> node = new ConvolutionNode<Type>(Opcode.CONVOLUTION, this
                .getNode(), kernel.getNode());
        return createNewImage(node, width, height, extent);
    }

    @Override
    public final U convolution1d(ImageT<Type,U> kernel, int dimension) {
        Node<Type> node = new ConvolutionNode<Type>(Opcode.CONVOLUTION_1D, this
                .getNode(), kernel.getNode(), dimension);
        return createNewImage(node, width, height, extent);
    }

    @Override
    public final U convolutionRotated1d(ImageT<Type,U> kernel, double phirad) {
        Node<Type> node = new ConvolutionNode<Type>(Opcode.CONVOLUTION_ROTATED_1D,
                this.getNode(), kernel.getNode(), phirad);
        return createNewImage(node, width, height, extent);
    }

    /* ConvGauss */

    @Override
    public final U gauss(double sigma, double truncation) {
        return gaussDerivative2d(sigma, 0, 0, truncation);
    }

    @Override
    public final U gaussDerivative2d(double sigma, int orderDerivX,
            int orderDerivY, double truncation) {
        return convGauss2d(sigma, orderDerivX, truncation, sigma, orderDerivY,
                truncation);
    }

    @Override
    public final U convGauss2d(double sigmaX, int orderDerivX,
            double truncationX, double sigmaY, int orderDerivY,
            double truncationY) {
        Node<Type> node = new GaussFilterNode<Type>(Opcode.CONV_GAUSS,
                this.getNode(), sigmaX, orderDerivX, truncationX, sigmaY,
                orderDerivY, truncationY);
        return createNewImage(node, width, height, extent);
    }

    @Override
    public U convGauss1x2d(double sigmaR, double sigmaT, double phiDegrees,
            int derivativeT, double n) {
        Node<Type> node = new GaussFilterNode<Type>(Opcode.CONV_GAUSS_1X2D, this
                .getNode(), sigmaR, sigmaT, phiDegrees, derivativeT, n);
        return createNewImage(node, width, height, extent);
    }

    @Override
    public final U convGaussAnisotropic2d(double sigmaU, int orderDerivU,
            double truncationU, double sigmaV, int orderDerivV,
            double truncationV, double phiRad) {
        Node<Type> node = new GaussFilterNode<Type>(Opcode.CONV_GAUSS_ANISOTROPIC,
                this.getNode(), sigmaU, orderDerivU, truncationU, sigmaV,
                orderDerivV, truncationV, phiRad);
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
    public final U rotate(double alpha, boolean linearInterpolation,
            boolean resize, Pixel<Type> background) {

        GeometricMatrixNode<Type> node = new GeometricMatrixNode<Type>(
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

    private final U geometricOpROI(int newImWidth, int newImHeight,
            Pixel<Type> background, int beginX, int beginY) {
        Node<Type> node = new GeometricROINode<Type>(Opcode.GEOMETRIC_ROI_RESIZE,
                getNode(), newImWidth, newImHeight, background, beginX, beginY);
        return createNewImage(node, newImWidth, newImHeight, extent);
    }

    @Override
    public final U extend(int newImWidth, int newImHeight, Pixel<Type> background,
            int beginX, int beginY) {
        return geometricOpROI(newImWidth, newImHeight, background, -beginX,
                -beginY);
    }

    @Override
    public final U restrict(int beginX, int beginY, int endX, int endY) {
        int newImWidth = endX - beginX + 1;
        int newImHeight = endY - beginY + 1;
        return geometricOpROI(newImWidth, newImHeight, null, beginX, beginY);
    }

    
    
    // @Override
    // public final U transform(Matrix transformationMatrix,
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
