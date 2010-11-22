package ibis.pyxis.operations;

import ibis.pyxis.Image;

public interface Reduce<T, U extends Image<T, U>> {
	public U pixMin();
	public U pixMax();
	public U pixSum();
	public U pixProduct();
	public U pixSup();
	public U pixInf();
}
