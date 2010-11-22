package ibis.pyxis.operations;

import ibis.pyxis.Image;

/**
 * @author Timo van Kessel
 *
 * @param <T>
 */
public interface Npo<T, U extends Image<T, U>> {
	
	public U collectMax();
	public U collectMin();
	public U collect(U other);
}
