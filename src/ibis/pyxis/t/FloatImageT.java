package ibis.pyxis.t;

import ibis.imaging4j.Format;

import java.nio.ByteBuffer;

import jorus.array.Array2d;

public class FloatImageT extends ImageT<float[], FloatImageT> {

    public FloatImageT(Node<float[]> node, int width, int height, int extent) {
        super(node, width, height, extent);
    }

    @Override
    protected FloatImageT createNewImage(Node<float[]> node, int width,
            int height, int extent) {
        return new FloatImageT(node, width, height, extent);
    }

    @Override
    public float[] getData() throws Exception {
        Array2d<float[], ?> array = getNode().getImage().getArray();
        
        return array.clone(0, 0).getData();
    }

    @Override
    public ibis.imaging4j.Image toImaging4j() throws Exception {

        // FIXME normalize broken --> now implemented in Imaging4j?
        float[] data = /* normalize(). */getData();
        ByteBuffer buffer = ByteBuffer.allocate(data.length * (Float.SIZE / 8));
        buffer.asFloatBuffer().put(data);
        ibis.imaging4j.Image result;
        switch (extent) {
        case 1:
            result = new ibis.imaging4j.Image(Format.TGFLOATGREY, width,
                    height, buffer);
            break;
        case 4:
            result = new ibis.imaging4j.Image(Format.TGFLOATARGB, width,
                    height, buffer);
            break;
        default:
            result = null;// exception
        }
        return result;
    }
}
