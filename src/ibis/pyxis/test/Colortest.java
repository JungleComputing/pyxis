package ibis.pyxis.test;

import ibis.imaging4j.Format;
import ibis.imaging4j.Imaging4j;
import ibis.imaging4j.test.ImageViewer;
import ibis.pyxis.ImageData;
import ibis.pyxis.Pyxis;
import ibis.pyxis.PyxisFactory;
import ibis.pyxis.t.FloatImageT;
import ibis.pyxis.t.nodes.ImportNode;
import ibis.pyxis.t.system.PyxisT;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import jorus.array.Array2dFloat;
import jorus.array.Array2dScalarFloat;
import jorus.array.Array2dVecFloat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Colortest {
	private static final Logger logger = LoggerFactory
			.getLogger(Colortest.class);

	private static FloatImageT createFloatImage(ibis.imaging4j.Image image, PyxisT pyxis)
			throws IOException {
		final Format format = image.getFormat();
		int extent;

		switch (format) {
		case PYXIS_FLOATARGB:
			extent = 4;
			break;
		case PYXIS_FLOATGREY:
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
		return new FloatImageT(node, width, height, extent, pyxis);
	}

	private static void viewImage(FloatImageT image, String text)
			throws Exception {
		ibis.imaging4j.Image outputImage;

		outputImage = image.toImaging4j();
		// outputImage = Imaging4j.convert(
		// Imaging4j.convert(outputImage, Format.GREY), Format.ARGB32);
		outputImage = Imaging4j.convert(outputImage, Format.ARGB32);
		ImageViewer viewer = new ImageViewer(outputImage.getWidth(),
				outputImage.getHeight());
		viewer.setImage(outputImage, text);
	}

	private static void saveImage(FloatImageT image, String filename)
			throws Exception {
		ibis.imaging4j.Image outputImage;
		ibis.imaging4j.Image outputJpeg;

		outputImage = image.toImaging4j();
		// outputJpeg = Imaging4j.convert(Imaging4j.convert(
		// Imaging4j.convert(outputImage, Format.GREY), Format.ARGB32),
		// Format.RGB24);

		outputJpeg = Imaging4j.convert(outputImage, Format.ARGB32);
		File file = new File(filename + ".jpg");
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		Imaging4j.save(outputJpeg, file);
	}

	private static void testImage(ibis.imaging4j.Image image, String name)
			throws Exception {
		new ImageViewer(image.getWidth(), image.getHeight()).setImage(image,
				name);
		File file = new File("colortest/" + name + ".jpg");
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();

		Imaging4j.save(image, file);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Pyxis pyxis = PyxisFactory.createPyxis(false, true);
		try {

			String fileName;
			if (args.length == 0) {
				fileName = "images/colortest.jpg";
			} else {
				fileName = args[0];
			}

			File file = new File(fileName);
			FloatImageT image, result;

			ibis.imaging4j.Image im = Imaging4j.load(file);
//			testImage(im, "import");
			im = Imaging4j.convert(im, Format.BGR24);
			testImage(im, "bgr24");
			im = Imaging4j.convert(im, Format.ARGB32);
			testImage(im, "argb32");

			im = Imaging4j.convert(im, Format.PYXIS_FLOATARGB);
			testImage(im, "tgfloatargb");
			
			im = Imaging4j.convert(im, Format.PYXIS_FLOATGREY);
//			testImage(im, "tgfloatgrey");
			
			im = Imaging4j.convert(im, Format.PYXIS_FLOATARGB);
			testImage(im, "tgfloatargb-grey");

			// viewImage(image, "original");
			// saveImage(image, "colortest/original");

			// ibis.imaging4j.Image grey = Imaging4j.convert(im,
			// Format.TGDOUBLEGREY);
			// result = createFloatImage(im);
			// viewImage(result, "grey");
			// saveImage(result, "colortest/grey");
		} finally {
			pyxis.end();
		}

	}
}
