package ibis.pyxis.t.parallel.activities;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.system.PyxisTContext;
import jorus.pixel.Pixel;

public class GeometricROIActivity<Type> extends
        OperationActivity<Type> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8936677748269979065L;
    private int newImageWidth, newImageHeight, beginX, beginY;
    private Pixel<Type> background;

    public GeometricROIActivity(PyxisTContext context, int opcode, int newImageWidth,
            int newImageHeight, Pixel<Type> background, int beginX, int beginY, ActivityIdentifier parent) {
        super(context, opcode, parent);
        this.beginX = beginX;
        this.beginY = beginY;
        this.newImageWidth = newImageWidth;
        this.newImageHeight = newImageHeight;
        this.background = background;
    }

    @Override
    protected ImageData<Type> calculate(ImageData<?>[] parents) {
        ImageData<Type> image = (ImageData<Type>) parents[0];
        ImageData<Type> result = null;

        switch (getOpcode()) {
        case Opcode.GEOMETRIC_ROI_RESIZE:
            result = new ImageData<Type>(image.getArray().geometricOpROI(
                    newImageWidth, newImageHeight, background, beginX, beginY));
            break;
        default:
            throw new Error("invalid opcode: " + getOpcode());
        }

        return result;
    }
}
