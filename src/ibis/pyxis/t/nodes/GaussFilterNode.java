package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.parallel.activities.GaussFilterActivity;
import ibis.pyxis.t.parallel.activities.OperationActivity;

public final class GaussFilterNode<Type> extends Node<Type> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8203823263646732583L;

    private final double truncationX, truncationY;
    private final double sigmaX, sigmaY;
    private final int orderDerivX, orderDerivY;

    private final double phiRad;

    public GaussFilterNode(int opcode, Node<Type> source, double sigmaX,
            int orderDerivX, double truncationX, double sigmaY,
            int orderDerivY, double truncationY) {
        super(opcode, source);
        this.orderDerivX = orderDerivX;
        this.orderDerivY = orderDerivY;
        this.sigmaX = sigmaX;
        this.sigmaY = sigmaY;
        this.truncationX = truncationX;
        this.truncationY = truncationY;
        phiRad = 0;
    }

    public GaussFilterNode(int opcode, Node<Type> source, double sigmaX,
            int orderDerivX, double truncationX, double sigmaY,
            int orderDerivY, double truncationY, double phiRad) {
        super(opcode, source);
        this.orderDerivX = orderDerivX;
        this.orderDerivY = orderDerivY;
        this.sigmaX = sigmaX;
        this.sigmaY = sigmaY;
        this.truncationX = truncationX;
        this.truncationY = truncationY;
        this.phiRad = phiRad;
    }

    public GaussFilterNode(int opcode, Node<Type> source, double sigmaR,
            double sigmaT, double phiDegrees, int derivativeT, double n) {
        super(opcode, source);
        this.sigmaX = sigmaR;
        this.sigmaY = sigmaT;
        phiRad = phiDegrees;
        this.orderDerivY = derivativeT;
        truncationY = n;

        orderDerivX = 0;
        truncationX = 0;
    }

    @Override
    protected OperationActivity<Type> createOperation(int opcode, ActivityIdentifier... parents) {
        switch (opcode) {
        case Opcode.CONV_GAUSS:
            return new GaussFilterActivity<Type>(getContext(), opcode, sigmaX, orderDerivX,
                    truncationX, sigmaY, orderDerivY, truncationY, parents[0]);
        case Opcode.CONV_GAUSS_ANISOTROPIC:
            return new GaussFilterActivity<Type>(getContext(), opcode, sigmaX, orderDerivX,
                    truncationX, sigmaY, orderDerivY, truncationY, phiRad, parents[0]);
        case Opcode.CONV_GAUSS_1X2D:
            return new GaussFilterActivity<Type>(getContext(), opcode, sigmaX, sigmaY, phiRad,
                    orderDerivY, truncationY, parents[0]);
        default:
            throw new Error("invalid opcode: " + opcode);
        }
    }
    
	@Override
	protected Node<Type>[] setParents(Node<Type>[] parents) {
		return parents;
	}
}
