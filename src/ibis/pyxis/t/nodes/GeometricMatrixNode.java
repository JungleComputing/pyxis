package ibis.pyxis.t.nodes;

import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.parallel.operations.GeometricMatrixOperation;
import ibis.pyxis.t.parallel.operations.Operation;
import jorus.pixel.Pixel;

public final class GeometricMatrixNode extends Node {

    /**
     * 
     */
    private static final long serialVersionUID = 4947210002460950794L;
    private boolean linearInterpolation;
    private boolean resize;
    private Pixel<float[]> background;
    private double alpha;

    public GeometricMatrixNode(int opcode, Node source, double alpha,
            boolean linearInterpolation, boolean resize, Pixel<float[]> background) {
        super(opcode, source);
        this.alpha = alpha;
        this.linearInterpolation = linearInterpolation;
        this.resize = resize;
        this.background = background;

    }

    // @Override
    // protected JorusImage<T> doExecute(JorusImage<T>[] parents) {
    // JorusImage<T> image = parents[0];
    // calculateDimensions();
    // return transform(image, btm);
    //
    // }

    @Override
    public Operation createOperation(boolean internal, int outputs, Node... input) {
    	int opcode = getOpcode();
        switch (opcode) {
        case Opcode.GEOMETRIC_ROTATE:
            return new GeometricMatrixOperation(identifier(), internal, outputs, opcode, alpha,
                    linearInterpolation, resize, background, input[0]);
        default:
            throw new Error("invalid opcode: " + opcode);
        }
    }
    
    
    @Override
	protected Node[] initInput(Node[] parents) {
		return parents;
	}
}
