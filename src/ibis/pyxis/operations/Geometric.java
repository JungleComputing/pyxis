package ibis.pyxis.operations;

import ibis.pyxis.Image;
import jorus.pixel.Pixel;


public interface Geometric<T, U extends Image<T, U>> {

	// public U transform(Matrix transformationMatrix, boolean forward,
	// boolean linearInterpolation, boolean resize, Pixel<T> background);

	public U rotate(double alpha, boolean linearInterpolation, boolean resize,
			Pixel<T> background);


	//ROI operations
	public U extend(int newImWidth, int newImHeight,
			Pixel<T> background, int beginX, int beginY);

	public U restrict(int beginX, int beginY, int endX, int endY);
}
