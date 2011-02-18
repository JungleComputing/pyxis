package ibis.pyxis.test;

import ibis.imaging4j.Format;
import ibis.imaging4j.Imaging4j;
import ibis.imaging4j.test.ImageViewer;
import ibis.pyxis.Image;
import ibis.pyxis.Pyxis;
import ibis.pyxis.PyxisFactory;

import java.io.File;

import jorus.pixel.PixelFloat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JanMark2D {
    private static final Logger logger = LoggerFactory
            .getLogger(JanMark2D.class);

    private static final int ITER = 5;

    private static final int EXTENT = 1;

    private static final int MIN_THETA = 0;
    private static final int MAX_THETA = 180;
    private static final int ROTATIONS = 36;
    private static final double ROT_STEP = ((double) (MAX_THETA - MIN_THETA))
            / (double) ROTATIONS;
    // private static final int STEP_THETA = 1; // 15 for minimal measurement

    private static final float MIN_SX = 1; // 1.0 for minimal measurement
    private static final float MAX_SX = 4; // 5.0 for minimal measurement
    private static final float STEP_SX = 1; // 2.0 for minimal measurement

    private static final float MIN_SY = 3; // 3.0 for minimal measurement
    private static final float MAX_SY = 9; // 11.0 for minimal measurement
    private static final float STEP_SY = 2; // 4.0 for minimal measurement

    private static final boolean Fixed = true; // Fixed MAX_SX: YES/NO

    private static double Max_sx(double sy) {
        return (Fixed ? MAX_SX : (sy * 0.75));
    }

    private static double Step_sx(double sx) {
        return (Fixed ? STEP_SX : (sx / 2));
    }

    static Image conv2D(Image source) throws Exception {
        float sx, sy;

        Image resultImage = null;

        /*** Loop over entire orientation scale-space ***/

        // for (int theta = MIN_THETA; theta < MAX_THETA; theta += STEP_THETA) {
        for (int rot = 0; rot < ROTATIONS; rot++) {
            double theta = MIN_THETA + rot * ROT_STEP;
            for (sy = MIN_SY; sy < MAX_SY; sy += STEP_SY) {
                for (sx = MIN_SX; sx < Max_sx(sy); sx += Step_sx(sx)) {
                    if (sx != sy) {
                    	Image filtIm1;
                    	Image filtIm2;
                        // FIXME not implemented (yet)
                        filtIm1 = source.convGauss1x2d(sx, sy, -theta, 2, 3);
                        filtIm2 = source.convGauss1x2d(sx, sy, -theta, 0, 3);
                        filtIm1 = filtIm1.posdiv(filtIm2);
                        // filtIm1 = filtIm1.negdiv(filtIm2);
                        filtIm1 = filtIm1.mul(new PixelFloat(sx * sy));
                        if (resultImage == null) {
                            resultImage = filtIm1.collectMax();
                        } else {
                            resultImage = resultImage.collect(filtIm1);
                        }
                    }
                }
                // resultImage.getData();
                // logger.debug("conv2D: theta = " + theta + ", sy = " + sy +
                // " finished");
            }
            // resultImage.getData();
            // logger.debug("conv2D: theta = " + theta + " finished");
        }
        return resultImage;
    }

    private static void viewImage(Image image, String text) {
        try {
            ibis.imaging4j.Image outputImage;

            outputImage = image.toImaging4j();
            outputImage = Imaging4j.convert(
                    Imaging4j.convert(outputImage, Format.GREY), Format.ARGB32);
            ImageViewer viewer = new ImageViewer(outputImage.getWidth(),
                    outputImage.getHeight());
            viewer.setImage(outputImage, text);
        } catch (Exception e) {
            // ignore, we cannot show the image on this machine
        }
    }

    private static void saveImage(Image image, String filename)
            throws Exception {
        ibis.imaging4j.Image outputImage;
        ibis.imaging4j.Image outputJpeg;

        outputImage = image.toImaging4j();
        outputJpeg = Imaging4j.convert(Imaging4j.convert(
                Imaging4j.convert(outputImage, Format.GREY), Format.ARGB32),
                Format.RGB24);
        File file = new File(filename + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        Imaging4j.save(outputJpeg, file);
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Pyxis pyxis = PyxisFactory.createPyxis(false, true);

        if (pyxis.isMaster()) {
            String fileName;
            if (args.length == 0) {
                fileName = "images/celegcolor.jpg";
            } else {
                fileName = args[0];
            }

            File file = new File(fileName);
            logger.debug("file size: " + file.length());
            Image image, result;
            result = null;

            ibis.imaging4j.Image im = Imaging4j.load(file);
            im = Imaging4j.convert(im, Format.ARGB32);
            im = Imaging4j.convert(im, Format.PYXIS_FLOATARGB);
            im = Imaging4j.convert(im, Format.PYXIS_FLOATGREY);
            image = pyxis.importFloatImage(im);

            // image = null;
            long time = 0;
            long graphTime = 0;

            @SuppressWarnings("unused")
            float[] data = image.getData();

            // saveImage(image, "output/original");
            // viewImage(image, "original");

            if (logger.isInfoEnabled()) {
                System.out.println("conv2D:");
            }
            for (int i = 0; i < ITER; i++) {
                time = System.currentTimeMillis();
                result = conv2D(image);
                graphTime = System.currentTimeMillis() - time;
                data = result.getData();
                time = System.currentTimeMillis() - time;
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("conv2D took %.3f seconds",
                            (double) time / 1000));
                    logger.info(String
                            .format("From which %.3f seconds are used for creating the graph",
                                    (double) graphTime / 1000));
                } else {
                    System.out.println((double) time / 1000);
                }
            }

            saveImage(result, "output/2df");
            viewImage(result, "2DF");
        }
        pyxis.end();
    }

}
