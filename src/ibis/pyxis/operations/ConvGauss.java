package ibis.pyxis.operations;

import ibis.pyxis.Image;


public interface ConvGauss<T, U extends Image<T, U>> {
	
//	public Image<T> convGauss2x1d(double sigmaX, int orderDerivX, int truncationX,
//			double sigmaY, int orderDerivY, int truncation);

	/*public Image<T> convGauss1x2d(Opcode borderType, Pixel<T> borderConstant, double stdDevT,
			double stdDevR, double phi, int derivativeT, double n);

	public Image<T> convGaussAniso2x1d(Opcode borderType, Pixel<T> borderConstant,
			double stdDevX, double stdDevY, double phi, int orderX, int orderY,
			double accuracyX, double accuracyY, int filterWidthX,
			int filterWidthY);*/
	
	public U convGauss2d(double sigmaX, int orderDerivX,
			double truncationX, double sigmaY, int orderDerivY,
			double truncationY);

	public U convGauss1x2d(double sigmaR, double sigmaT,
			double phiDegrees, int derivativeT, double n);
	
	public U gauss(double sigma, double truncation);

	public U gaussDerivative2d(double sigma, int orderDerivX,
			int orderDerivY, double truncation);
	
	/** Anisotropic Convolution **/
	
	public U convGaussAnisotropic2d(double sigmaU,
			int orderDerivU, double truncationU, double sigmaV,
			int orderDerivV, double truncationV, double phiRad);

}
