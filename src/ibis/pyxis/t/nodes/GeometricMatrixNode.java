package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.parallel.activities.GeometricMatrixActivity;
import ibis.pyxis.t.parallel.activities.OperationActivity;
import jorus.pixel.Pixel;

public final class GeometricMatrixNode<Type> extends Node<Type> {

    /**
     * 
     */
    private static final long serialVersionUID = 4947210002460950794L;
    private boolean linearInterpolation;
    private boolean resize;
    private Pixel<Type> background;
    private double alpha;

    public GeometricMatrixNode(int opcode, Node<Type> source, double alpha,
            boolean linearInterpolation, boolean resize, Pixel<Type> background) {
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
    protected OperationActivity<Type> createOperation(int opcode, ActivityIdentifier... parents) {
        switch (opcode) {
        case Opcode.GEOMETRIC_ROTATE:
            return new GeometricMatrixActivity<Type>(getContext(), opcode, alpha,
                    linearInterpolation, resize, background, parents[0]);
        default:
            throw new Error("invalid opcode: " + opcode);
        }
    }
    
	@Override
	protected Node<Type>[] setParents(Node<Type>[] parents) {
		return parents;
	}
}
