package ibis.pyxis;

import ibis.pyxis.t.nodes.Node;
import jorus.pixel.Pixel;


public abstract class Image {

    protected abstract Node getNode();
    protected Node getNode(Image other) {
    	return other.getNode();
    }
	
    public abstract float[] getData() throws Exception;

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getDepth();

    public abstract int getExtent();
    
    public abstract ibis.imaging4j.Image toImaging4j() throws Exception;
    
	/* SVO */
    public abstract Image add(Pixel<float[]> value, int x, int y);
    public abstract Image set(Pixel<float[]> value, int x, int y);
	
	/* UPO */
    public abstract Image set(Pixel<float[]> value);
	
	/* BPO */
    public abstract Image add(Pixel<float[]> pixel);
    public abstract Image sub(Pixel<float[]> pixel);
    public abstract Image mul(Pixel<float[]> pixel);
    public abstract Image div(Pixel<float[]> pixel);
    public abstract Image min(Pixel<float[]> pixel);
    public abstract Image max(Pixel<float[]> pixel);
    public abstract Image negdiv(Pixel<float[]> pixel);
    public abstract Image posdiv(Pixel<float[]> pixel);
    public abstract Image absdiv(Pixel<float[]> pixel);

    public abstract Image add(Image source2);
    public abstract Image sub(Image source2);
    public abstract Image mul(Image source2);
    public abstract Image div(Image source2);
    public abstract Image min(Image source2);
    public abstract Image max(Image source2);
    public abstract Image negdiv(Image source2);
    public abstract Image posdiv(Image source2);
    public abstract Image absdiv(Image source2);
	
	/* NPO */
    public abstract Image collectMax();
    public abstract Image collectMin();
    public abstract <T extends Image> Image collect(T other);
	
	/* Reduction operations */
    public abstract Image pixMin();
    public abstract Image pixMax();
    public abstract Image pixSum();
    public abstract Image pixProduct();
    public abstract Image pixSup();
    public abstract Image pixInf();

	 /* Convolution */
    public abstract Image convKernelSeparated2d(Image kernelX, Image kernelY);
    public abstract Image convKernelSeparated(Image kernel);
    public abstract Image convolution(Image kernel);
    public abstract Image convolution1d(Image kernel, int dimension);
    public abstract Image convolutionRotated1d(Image kernel, double phirad);
	 
	/* Gaussian Convolution */
    public abstract Image convGauss2d(double sigmaX, int orderDerivX, double truncationX,
			double sigmaY, int orderDerivY, double truncationY);
    public abstract Image convGauss1x2d(double sigmaR, double sigmaT, double phiDegrees,
			int derivativeT, double n);
    public abstract Image gauss(double sigma, double truncation);
    public abstract Image gaussDerivative2d(double sigma, int orderDerivX,
			int orderDerivY, double truncation);
    public abstract Image convGaussAnisotropic2d(double sigmaFloatImage, int orderDerivFloatImage,
			double truncationFloatImage, double sigmaV, int orderDerivV,
			double truncationV, double phiRad);
	
	/* Geometric transformation */
	
    public abstract Image rotate(double alpha, boolean linearInterpolation, boolean resize,
			Pixel<float[]> background);
    public abstract Image extend(int newImWidth, int newImHeight,
			Pixel<float[]> background, int beginX, int beginY);
    public abstract Image restrict(int beginX, int beginY, int endX, int endY);
}
