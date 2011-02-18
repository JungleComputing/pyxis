package ibis.pyxis.t.parallel.operations;

import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.nodes.OperationIdentifier;
import jorus.pixel.Pixel;

public class GeometricROIOperation extends
        Operation {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8936677748269979065L;
    private int newImageWidth, newImageHeight, beginX, beginY;
    private Pixel<float[]> background;

    public GeometricROIOperation(OperationIdentifier nodeID, boolean internal, int outputs, int opcode, int newImageWidth,
            int newImageHeight, Pixel<float[]> background, int beginX, int beginY, Node input) {
        super(nodeID, internal, outputs, opcode, input);
        this.beginX = beginX;
        this.beginY = beginY;
        this.newImageWidth = newImageWidth;
        this.newImageHeight = newImageHeight;
        this.background = background;
    }

    @Override
	protected void processImageEvent(int index, ImageData image) {
		//do nothing
	}
    
    @Override
    protected ImageData execute(ImageData[] parents) {
        ImageData image = parents[0];
        ImageData result = null;

        switch (getOpcode()) {
        case Opcode.GEOMETRIC_ROI_RESIZE:
            result = new ImageData(image.getArray().geometricOpROI(
                    newImageWidth, newImageHeight, background, beginX, beginY), identifier());
            break;
        default:
            throw new Error("invalid opcode: " + getOpcode());
        }

        return result;
    }
}
