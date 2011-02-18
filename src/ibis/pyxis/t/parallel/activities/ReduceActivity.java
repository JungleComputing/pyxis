package ibis.pyxis.t.parallel.activities;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.system.PyxisTContext;
import jorus.array.Array2d;

public class ReduceActivity<Type> extends OperationActivity<Type> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4912070279145139181L;

	public ReduceActivity(PyxisTContext context, int opcode, ActivityIdentifier parent) {
		super(context, opcode, parent);
	}

	@Override
	protected ImageData<Type> calculate(ImageData<?>[] parents) {
		Array2d<Type,?> array;
		Array2d<Type,?> source = ((ImageData<Type>)parents[0]).getArray();
		switch (getOpcode()) {
		case Opcode.REDUCE_SUM:
			array = source.pixSum();
			break;
		case Opcode.REDUCE_PRODUCT:
			array = source.pixProduct();
			break;
		case Opcode.REDUCE_MIN:
			array = source.pixMin();
			break;
		case Opcode.REDUCE_MAX:
			array = source.pixMax();
			break;
		case Opcode.REDUCE_SUP:
			array = source.pixSup();
			break;
		case Opcode.REDUCE_INF:
			array = source.pixInf();
			break;
		default:
			throw new Error("invalid opcode: " + getOpcode());
		}
		return new ImageData<Type>(array);
	}

}
