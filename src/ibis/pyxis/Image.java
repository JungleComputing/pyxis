package ibis.pyxis;

import ibis.pyxis.operations.Bpo;
import ibis.pyxis.operations.ConvGauss;
import ibis.pyxis.operations.Convolution;
import ibis.pyxis.operations.Geometric;
import ibis.pyxis.operations.Npo;
import ibis.pyxis.operations.Reduce;
import ibis.pyxis.operations.Svo;
import ibis.pyxis.operations.Upo;

public interface Image<T, U extends Image<T,U>> extends Svo<T, U>, Upo<T, U>,
        Bpo<T, U>, Npo<T, U>, Convolution<T, U>, ConvGauss<T, U>, Reduce<T, U>,
        Geometric<T, U> {

    public T getData() throws Exception;

    public int getWidth();

    public int getHeight();

    public int getDepth();

    public int getExtent();
    
    public ibis.imaging4j.Image toImaging4j() throws Exception;
}
