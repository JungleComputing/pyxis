package ibis.pyxis.t;

import ibis.imaging4j.Format;
import ibis.pyxis.t.system.PyxisT;

import java.nio.ByteBuffer;

import jorus.array.Array2d;

public class DoubleImageT extends ImageT<double[], DoubleImageT> {

    public DoubleImageT(Node<double[]> node, int width, int height, int extent, PyxisT pyxis) {
        super(node, width, height, extent, pyxis);
    }

    @Override
    protected DoubleImageT createNewImage(Node<double[]> node, int width,
            int height, int extent) {
        return new DoubleImageT(node, width, height, extent, getPyxis());
    }

    @Override
    public double[] getData() throws Exception {
        Array2d<double[], ?> array = getNode().getImage(getPyxis()).getArray();
        return array.clone(0, 0).getData();
    }

    @Override
    public ibis.imaging4j.Image toImaging4j() throws Exception {
        // DoubleImage normalizedImage = normalize();

        // FIXME normalize broken --> now implemented in Imaging4j?
        double[] data = /* normalize(). */getData();
        ByteBuffer buffer = ByteBuffer
                .allocate(data.length * (Double.SIZE / 8));
        buffer.asDoubleBuffer().put(data);
        ibis.imaging4j.Image result;
        switch (extent) {
        case 1:
            result = new ibis.imaging4j.Image(Format.PYXIS_DOUBLEGREY, width,
                    height, buffer);
            break;
        case 4:
            result = new ibis.imaging4j.Image(Format.PYXIS_DOUBLEARGB, width,
                    height, buffer);
            break;
        default:
            result = null;// exception
        }
        return result;
    }
}
