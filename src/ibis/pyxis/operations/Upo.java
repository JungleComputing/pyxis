package ibis.pyxis.operations;

import ibis.pyxis.Image;
import jorus.pixel.Pixel;

public interface Upo<T, U extends Image<T, U>> {
	public U set(Pixel<T> value);
}
