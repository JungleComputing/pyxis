package ibis.pyxis.operations;

import ibis.pyxis.Image;
import jorus.pixel.Pixel;

/**
 * @author Timo van Kessel
 *
 * @param <T>
 * @param <U> 
 */
public interface Bpo<T, U extends Image<T, U>> {
	
	public U add(Pixel<T> pixel);
	public U sub(Pixel<T> pixel);
	public U mul(Pixel<T> pixel);
	public U div(Pixel<T> pixel);
	public U min(Pixel<T> pixel);
	public U max(Pixel<T> pixel);
	public U negdiv(Pixel<T> pixel);
	public U posdiv(Pixel<T> pixel);
	public U absdiv(Pixel<T> pixel);
	public U add(U source2);
	public U sub(U source2);
	public U mul(U source2);
	public U div(U source2);
	public U min(U source2);
	public U max(U source2);
	public U negdiv(U source2);
	public U posdiv(U source2);
	public U absdiv(U source2);
}
