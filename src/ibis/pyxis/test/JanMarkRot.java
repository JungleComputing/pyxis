package ibis.pyxis.test;

import ibis.imaging4j.Format;
import ibis.imaging4j.Imaging4j;
import ibis.imaging4j.test.ImageViewer;
import ibis.pyxis.Pyxis;
import ibis.pyxis.PyxisFactory;
import ibis.pyxis.t.FloatImageT;

import java.io.File;

import jorus.pixel.PixelFloat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JanMarkRot {
    private static final Logger logger = LoggerFactory
            .getLogger(JanMarkRot.class);

    private static final int ITER = 5;

    private static final int EXTENT = 1;

    private static final int MIN_THETA = 0;
    private static final int MAX_THETA = 180;
    private static final int ROTATIONS = 36;
    private static final double ROT_STEP = ((double) (MAX_THETA - MIN_THETA))
            / (double) ROTATIONS;
    // private static final int STEP_THETA = 10;
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

    static FloatImageT convRot(FloatImageT source, boolean adjustSize)
            throws Exception {
        int newSize = 0, beginX = 0, beginY = 0;
        int width = source.getWidth();
        int height = source.getHeight();

        float[] pData = new float[source.getExtent()];
        float[] qData = new float[source.getExtent()];
        for (int i = 0; i < pData.length; i++) {
            pData[i] = 0;
            qData[i] = .5f; // 5. / 255;
        }
        PixelFloat p = new PixelFloat(pData);
        PixelFloat q = new PixelFloat(qData);

        // set border when we want to adjust the size
        if (adjustSize) {

            newSize = (int) (Math.sqrt((double) (width * width + height
                    * height))) + 1; // length of the diagonal
            // rounded upwards
            beginX = (width - newSize) / 2;
            beginY = (height - newSize) / 2;

            // FIXME not implemented (yet)
            source = source.extend(newSize, newSize, q, beginX, beginY);
        }

        FloatImageT resultImage = null;
        // resultImage = Upo.set(resultImage, p);

        /*** Loop over entire orientation scale-space ***/
        for (int rot = 0; rot < ROTATIONS; rot++) {
            double theta = MIN_THETA + rot * ROT_STEP;
            // for (int theta = MIN_THETA; theta < MAX_THETA; theta +=
            // STEP_THETA) {
            // for (float theta = MIN_THETA; theta < MAX_THETA; theta +=
            // STEP_THETA) {
            // FIXME not implemented (yet)
            FloatImageT rotatedImg = source.rotate(-theta, true, false, p);

            // NPO-version
            FloatImageT contrIm = rotatedImg.set(p).collectMax();
            // non-NPO version
            // FloatImageT contrIm = rotatedImg.set(p);

            FloatImageT filtIm1;
            FloatImageT filtIm2;

            for (float sy = MIN_SY; sy < MAX_SY; sy += STEP_SY) {
                for (float sx = MIN_SX; sx < Max_sx(sy); sx += Step_sx(sx)) {
                    if (sx != sy) {

                        filtIm1 = rotatedImg.convGauss2d(sx, 2, 3, sy, 0, 3);
                        filtIm2 = rotatedImg.convGauss2d(sx, 0, 3, sy, 0, 3);

                        // filtIm1 = filtIm1.posdiv(filtIm2);
                        filtIm1 = filtIm1.negdiv(filtIm2);
                        // filtIm1 = filtIm1.absdiv(filtIm2);

                        filtIm1 = filtIm1.mul(new PixelFloat(sx * sy));

                        // NPO-version
                        contrIm.collect(filtIm1);
                        // non-NPO version
                        // contrIm = contrIm.max(filtIm1);
                    }
                }
            }

            FloatImageT backIm = contrIm.rotate(theta, true, false, p);

            // NPO-version
            if (resultImage == null) {
                resultImage = backIm.collectMax();
            } else {
                resultImage = resultImage.collect(backIm);
            }
            // non-NPO version
            // if (resultImage == null) {
            // resultImage = backIm;
            // } else {
            // resultImage = resultImage.max(backIm);
            // }

            // resultImage.getData();
            // logger.debug("convUV: theta = " + theta + " finished");
        }
        // if (adjustSize) {
        // resultImage = resultImage.restrict(beginX,
        // beginY, beginX + width, beginY + height);
        // }
        return resultImage;
    }

    private static void viewImage(FloatImageT image, String text) {
        try {
            ibis.imaging4j.Image outputImage;

            outputImage = image.toImaging4j();
//            outputImage = Imaging4j.convert(
//                    Imaging4j.convert(outputImage, Format.GREY), Format.ARGB32);
            outputImage = Imaging4j.convert(Imaging4j.convert(outputImage, Format.PYXIS_FLOATARGB), Format.ARGB32);
            ImageViewer viewer = new ImageViewer(outputImage.getWidth(),
                    outputImage.getHeight());
            viewer.setImage(outputImage, text);
        } catch (Exception e) {
            // ignore, we cannot show the image on this machine
        }
    }

    private static void saveImage(FloatImageT image, String filename)
            throws Exception {
        ibis.imaging4j.Image outputImage;
        ibis.imaging4j.Image outputJpeg;

        outputImage = image.toImaging4j();
//        outputJpeg = Imaging4j.convert(Imaging4j.convert(
//                Imaging4j.convert(outputImage, Format.GREY), Format.ARGB32),
//                Format.RGB24);
        
//        outputJpeg = Imaging4j.convert(Imaging4j.convert(outputImage, Format.PYXIS_FLOATARGB), Format.ARGB32);
        outputJpeg = Imaging4j.convert(outputImage, Format.PYXIS_FLOATARGB);
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
            FloatImageT image, result;

            ibis.imaging4j.Image im = Imaging4j.load(file);
            im = Imaging4j.convert(im, Format.ARGB32);
            im = Imaging4j.convert(im, Format.PYXIS_FLOATARGB);
            im = Imaging4j.convert(im, Format.PYXIS_FLOATGREY);
            image = pyxis.importFloatImage(im);

            long time = 0;
            long graphTime = 0;

            float[] data = image.getData();
             saveImage(image, "output/original");
//             viewImage(image, "original");

            result = null;
            if (logger.isInfoEnabled()) {
                logger.info("convRot:");
            }
            for (int i = 0; i < ITER; i++) {
                time = System.currentTimeMillis();
                result = convRot(image, false);
                // result = convRot(image, true);
                graphTime = System.currentTimeMillis() - time;
                data = result.getData();
                time = System.currentTimeMillis() - time;
//                viewImage(result, "Rot " + i);
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("convRot took %.3f seconds",
                            (double) time / 1000));
                    logger.info(String
                            .format("From which %.3f seconds are used for creating the graph",
                                    (double) graphTime / 1000));
                } else {
                    System.out.println((double) time / 1000);
                }

            }

            saveImage(result, "output/rot");
            // viewImage(result, "Rot");
        }
        pyxis.end();
        System.out.println("JanMarkRot done");
    }
}
