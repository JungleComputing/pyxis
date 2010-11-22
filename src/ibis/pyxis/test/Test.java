package ibis.pyxis.test;

import ibis.imaging4j.Format;
import ibis.imaging4j.Imaging4j;
import ibis.imaging4j.test.ImageViewer;
import ibis.pyxis.ImageData;
import ibis.pyxis.Pyxis;
import ibis.pyxis.t.FloatImageT;
import jorus.pixel.PixelFloat;

public class Test {

    private static void viewImage(FloatImageT image, String text)
            throws Exception {
        ibis.imaging4j.Image outputImage;

        outputImage = image.toImaging4j();
        if (image.getExtent() == 1) {
            outputImage = Imaging4j.convert(outputImage, Format.TGFLOATARGB);
        }

        outputImage = Imaging4j.convert(outputImage, Format.ARGB32);
        ImageViewer viewer = new ImageViewer(outputImage.getWidth(),
                outputImage.getHeight());
        viewer.setImage(outputImage, text);
    }

    private void run2() throws Exception {
        PixelFloat pix0 = new PixelFloat(new float[] { 0 });
        PixelFloat pix1 = new PixelFloat(new float[] { 1 });
        FloatImageT image = Pyxis.getPyxis().createImage(255, 255, 1,
                new float[255 * 255]);

        image.set(pix0);

        image = image.set(pix1, 200, 100);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                image = image.set(pix1, i, j);
            }
        }
        for (int i = 100; i < 150; i++) {
            for (int j = 100; j < 150; j++) {
                image = image.set(pix1, i, j);
            }
        }
        // try {
        // viewImage(image, "1");
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        int sx = 14, sy = 14;
        int derX = 0, derY = 0;
        // image = AdvancedFilter.convolutionGaussian2x1d(image,
        // Opcode.SETBORDER_MIRROR, null, sx, sy, derX, derY); //works!
        // image = AdvancedFilter.convolutionGaussian1x2d(image,
        // Opcode.SETBORDER_MIRROR, null, sx, sy, 0, derX, 3); //broken, does
        // not take first derivative
        // image = AdvancedFilter.convolutionAnisotropicGaussian2x1d(image,
        // //broken, does not take derivative of first order and looks wrong at
        // the corners
        // Opcode.SETBORDER_MIRROR, null, sx, sy, 0, derX, derY, 0.995,
        // 0.995, (int) (sx * 3 * 2 + 1), (int) (sy * 3 * 2 + 1));
        try {
            viewImage(image, "satin 2");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void run() throws Exception {
        PixelFloat pix0 = new PixelFloat(new float[] { 0, 0, 0, 1 });
        PixelFloat pix1 = new PixelFloat(new float[] { 1, 0, 1, 1 });
        PixelFloat pix2 = new PixelFloat(new float[] { 2, 2, 0, 1 });

        FloatImageT image1 = Pyxis.getPyxis()
                .createImage(160, 160, 4, new float[160 * 160 * 4]).set(pix0);
        FloatImageT image2 = Pyxis.getPyxis()
                .createImage(160, 160, 4, new float[160 * 160 * 4]).set(pix1);
        FloatImageT image3 = Pyxis.getPyxis()
                .createImage(160, 160, 4, new float[160 * 160 * 4]).set(pix2);

        // for (int i = 0; i < 20; i++) {
        // for (int j = 0; j < 20; j++) {
        // image3 = image3.set(pix1, i, j);
        // }
        // }

        FloatImageT image4 = image1.add(image2);
        FloatImageT image4b = image3.convGauss2d(3, 0, 3, 3, 0, 3);
        FloatImageT image5 = image4.mul(image4b);
        FloatImageT image6 = image5.div(image1);
        FloatImageT image7 = image5.convolution(image6);
        FloatImageT sink = image5;// .set(new double[] { 4 });

        // Node<?>[] roots = sink.getNode().getRoots();
        // System.out.println("roots: " + roots.length);
        // for(Node<?> root: roots) {
        // System.out.println(root.getImageData().getName());
        // }
        //
        // System.out.printf("result: %s\n", sink.getData().getName());
        //
        // System.out.println("roots: " + roots.length);
        // for(Node<?> root: roots) {
        // System.out.println(root.getImageData().getName());
        // }

        float[] im = image4.getData();
        System.out.println("Got image 4");
        image6.getData();
        System.out.println("Got image 6");
        sink.getData();
        System.out.println("Got sink image");

        try {
            viewImage(image3, "satin 1");
            viewImage(image4b, "satin 1b");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Pyxis pyxis = Pyxis.init(false, true);
        if (pyxis.isMaster()) {
            Test test = new Test();
            try {
                test.run();
                // test.run2();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        pyxis.end();
    }

}