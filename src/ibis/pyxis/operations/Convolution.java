package ibis.pyxis.operations;

import ibis.pyxis.Image;


public interface Convolution<T, U extends Image<T, U>> {

	public abstract U convKernelSeparated2d(U kernelX, U kernelY);

	public U convKernelSeparated(U kernel);

	public U convolution(U kernel);

	public U convolution1d(U kernel, int dimension);

	public U convolutionRotated1d(U kernel, double phirad);

}
