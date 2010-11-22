package ibis.pyxis.t.system;

import ibis.constellation.Constellation;
import ibis.constellation.ConstellationFactory;
import ibis.constellation.Executor;
import ibis.constellation.SimpleExecutor;
import ibis.imaging4j.Format;
import ibis.pyxis.ImageData;
import ibis.pyxis.Pyxis;
import ibis.pyxis.t.DoubleImageT;
import ibis.pyxis.t.FloatImageT;
import ibis.pyxis.t.nodes.ImportNode;

import java.io.IOException;
import java.nio.ByteBuffer;

import jorus.array.Array2dDouble;
import jorus.array.Array2dFloat;
import jorus.array.Array2dScalarDouble;
import jorus.array.Array2dScalarFloat;
import jorus.array.Array2dVecDouble;
import jorus.array.Array2dVecFloat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TODO  better javadoc
/**
 * Starting point for the programming model. Need some better info here
 * 
 * @author Timo van Kessel
 */
public class PyxisT extends Pyxis {
    private static final Logger logger = LoggerFactory.getLogger(PyxisT.class);

    private final GraphController controller;
    private final Constellation constellation;

    public PyxisT() throws Exception {
        constellation = initConstellation();
        controller = GraphController.init(constellation);
    }

    private Constellation initConstellation() {
        Constellation constellation;
        int n_executors = Integer.getInteger("pyxis.t.executors", java.lang.Runtime.getRuntime().availableProcessors());
        
        Executor[] executors = new Executor[n_executors];
        for (int i = 0; i < n_executors; i++) {
            executors[i] = new PyxisTExecutor();
        }

        
        try {
            constellation = ConstellationFactory.createConstellation(executors);
        } catch (Exception e1) {
            e1.printStackTrace();
            System.exit(1);
            return null; // prevents annoying comments that the constellation variable isn't initialized properly 
        }
        if(logger.isDebugEnabled()) {
            logger.debug("activating Constellation, " + n_executors + " executors");
        }
        constellation.activate();
        return constellation;
    }
    
    @Override
    public Constellation getConstellation() {
        return constellation;
    }
    
    @Override
    protected DoubleImageT createDoubleImage(ibis.imaging4j.Image image)
            throws IOException {
        final Format format = image.getFormat();
        int extent;

        switch (format) {
        case TGDOUBLEARGB:
            extent = 4;
            break;
        case TGDOUBLEGREY:
            extent = 1;
            break;
        default:
            throw new IOException("Image format not supported");
        }

        final int width = image.getWidth();
        final int height = image.getHeight();

        final double[] array = new double[width * height * extent];
        final ByteBuffer buffer = image.getData();
        buffer.rewind();
        buffer.asDoubleBuffer().get(array);

        Array2dDouble<?> array2d;
        if (extent == 1) {
            array2d = new Array2dScalarDouble(width, height, array, true);
        } else {
            array2d = new Array2dVecDouble(width, height, extent, array, true);
        }
        ImageData<double[]> jImage = new ImageData<double[]>(array2d);
        ImportNode<double[]> node = new ImportNode<double[]>(jImage);
        return new DoubleImageT(node, width, height, extent);
    }

    @Override
    protected FloatImageT createFloatImage(ibis.imaging4j.Image image)
            throws IOException {
        final Format format = image.getFormat();
        int extent;

        switch (format) {
        case TGFLOATARGB:
            extent = 4;
            break;
        case TGFLOATGREY:
            extent = 1;
            break;
        default:
            throw new IOException("Image format not supported");
        }

        final int width = image.getWidth();
        final int height = image.getHeight();

        final float[] array = new float[width * height * extent];
        final ByteBuffer buffer = image.getData();
        buffer.rewind();
        buffer.asFloatBuffer().get(array);

        Array2dFloat<?> array2d;
        if (extent == 1) {
            array2d = new Array2dScalarFloat(width, height, array, true);
        } else {
            array2d = new Array2dVecFloat(width, height, extent, array, true);
        }
        ImageData<float[]> jImage = new ImageData<float[]>(array2d);
        ImportNode<float[]> node = new ImportNode<float[]>(jImage);
        return new FloatImageT(node, width, height, extent);
    }
    
    @Override
    public FloatImageT createImage(int width, int height, int extent,
            float[] data) {
        Array2dFloat<?> array;
        if (extent == 1) {
            array = new Array2dScalarFloat(width, height, data, true);
        } else {
            array = new Array2dVecFloat(width, height, extent, data, true);
        }
        ImageData<float[]> image = new ImageData<float[]>(array);
        ImportNode<float[]> node = new ImportNode<float[]>(image);
        return new FloatImageT(node, width, height, extent);
    }
    
    @Override
    public DoubleImageT createImage(int width, int height, int extent,
            double[] data) {
        Array2dDouble<?> array;
        if (extent == 1) {
            array = new Array2dScalarDouble(width, height, data, true);
        } else {
            array = new Array2dVecDouble(width, height, extent, data, true);
        }
        ImageData<double[]> image = new ImageData<double[]>(array);
        ImportNode<double[]> node = new ImportNode<double[]>(image);
        return new DoubleImageT(node, image.getWidth(), image.getHeight(), image.getExtent());
    }

    @Override
    public void end() {
        constellation.done();
        controller.done();
    }

    @Override
    public void gc() {
        controller.clean();
    }

    @Override
    public boolean isMaster() {
        return constellation.isMaster();
    }
}
