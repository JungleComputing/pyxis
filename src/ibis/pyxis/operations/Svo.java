package ibis.pyxis.operations;

import ibis.pyxis.Image;
import jorus.pixel.Pixel;

public interface Svo<T, U extends Image<T, U>> {
	
	public U add(Pixel<T> value, int x, int y);
	public U set(Pixel<T> value, int x, int y);
}
