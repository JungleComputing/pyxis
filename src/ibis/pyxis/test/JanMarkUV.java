package ibis.pyxis.test;

import ibis.imaging4j.Format;
import ibis.imaging4j.Imaging4j;
import ibis.imaging4j.test.ImageViewer;
import ibis.pyxis.Image;
import ibis.pyxis.Pyxis;
import ibis.pyxis.PyxisFactory;

import java.io.File;

import jorus.parallel.PxSystem;
import jorus.pixel.PixelFloat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JanMarkUV {
    private static final Logger logger = LoggerFactory
            .getLogger(JanMarkUV.class);

    private static final int ITER = 5;

    private static final int EXTENT = 1;

    private static final int MIN_THETA = 0;
    private static final int MAX_THETA = 180;
    private static final int ROTATIONS = 36;
    private static final double ROT_STEP = ((double) (MAX_THETA - MIN_THETA))
            / (double) ROTATIONS;
    // private static final int STEP_THETA = 1; // 15 for minimal measurement
    // private static final double STEP_THETA = 0.1; // 15 for minimal
    // measurement

    private static final float MIN_SX = 1; // 1.0 for minimal measurement
    private static final float MAX_SX = 4; // 5.0 for minimal measurement
    private static final float STEP_SX = 1; // 2.0 for minimal measurement

    private static final float MIN_SY = 3; // 3.0 for minimal measurement
    private static final float MAX_SY = 9; // 11.0 for minimal measurement
    private static final float STEP_SY = 2; // 4.0 for minimal measurement

    private static final boolean Fixed = true; // Fixed MAX_SX: YES/NO

    private static float Max_sx(float sy) {
        return (Fixed ? MAX_SX : (sy * 0.75f));
    }

    private static float Step_sx(float sx) {
        return (Fixed ? STEP_SX : (sx / 2));
    }

    private static double radians(double angle) {
        return (angle / 180.) * Math.PI;
    }

    static Image convUV(Image source) throws Exception {
        float sx, sy;
        Image resultImage = null;

        /*** Loop over entire orientation scale-space ***/
        for (int rot = 0; rot < ROTATIONS; rot++) {
            double theta = MIN_THETA + rot * ROT_STEP;
            // for (int theta = MIN_THETA; theta < MAX_THETA; theta +=
            // STEP_THETA) {
            final double phiRad = radians(theta);
            // for (double theta = MIN_THETA; theta < MAX_THETA; theta +=
            // STEP_THETA) {
            for (sy = MIN_SY; sy < MAX_SY; sy += STEP_SY) {
                for (sx = MIN_SX; sx < Max_sx(sy); sx += Step_sx(sx)) {
                    if (sx != sy) {
                    	Image filtIm1;
                    	Image filtIm2;
                        // filtIm1 = source.convGaussAniso2x1d(
                        // Opcode.SETBORDER_MIRROR, null, sx, sy, theta,
                        // 2, 0, 0.995, 0.995, (int) (sx * 3 * 2 + 1),
                        // (int) (sy * 3 * 2 + 1));
                        // filtIm2 = source.convGaussAniso2x1d(
                        // Opcode.SETBORDER_MIRROR, null, sx, sy, theta,
                        // 0, 0, 0.995, 0.995, (int) (sx * 3 * 2 + 1),
                        // (int) (sy * 3 * 2 + 1));
                        filtIm1 = source.convGaussAnisotropic2d(sx, 2, 3, sy,
                                0, 3, phiRad);
                        filtIm2 = source.convGaussAnisotropic2d(sx, 0, 3, sy,
                                0, 3, phiRad);
                        // filtIm1 = filtIm1.negdiv(filtIm2);
                        filtIm1 = filtIm1.posdiv(filtIm2);
                        // filtIm1 = filtIm1.absdiv(filtIm2);
                        filtIm1 = filtIm1.mul(new PixelFloat(sx * sy)); // mul
                        // with
                        // pixel
                        if (resultImage == null) {
                            resultImage = filtIm1.collectMax();
                        } else {
                            resultImage.collect(filtIm1);
                        }
                    }
                }
            }
            // resultImage.getData();
            // logger.debug("convUV: theta = " + theta + " finished");
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
        // outputJpeg = Imaging4j.convert(Imaging4j.convert(
        // outputImage, Format.GREY), Format.RGB24);
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

            long time = 0;
            long graphTime = 0;

            float[] data = image.getData();
            // saveImage(image, "output/original");
            // viewImage(image, "original");

            System.out.println("convUV:");
            for (int i = 0; i < ITER; i++) {
                time = System.currentTimeMillis();
                result = convUV(image);
                graphTime = System.currentTimeMillis() - time;
                data = result.getData();
                time = System.currentTimeMillis() - time;
                if (PxSystem.initialized()) {
                    PxSystem.get().printStatistics();
                }
                viewImage(result, "UV");
                System.out.println(String.format("ConvUV took %.3f seconds",
                        (double) time / 1000));
                System.out
                        .println(String
                                .format("From which %.3f seconds are used for creating the graph",
                                        (double) graphTime / 1000));
                // System.err.println("run " + i + ": float[] #"
                // + Array2dFloat.getAndResetcreateCounter());

            }
            if (data == null) {
                System.out.println("data == null!!!");
            }
            // System.err.println("double[] #" + Array2dDoubles.createCounter);
            // saveImage(result, "output/uv");
//            viewImage(result, "UV");
        }
        pyxis.end();
    }
}
