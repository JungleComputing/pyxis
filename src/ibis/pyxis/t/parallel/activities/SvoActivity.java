package ibis.pyxis.t.parallel.activities;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.system.PyxisTContext;
import jorus.pixel.Pixel;

public class SvoActivity<Type> extends OperationActivity<Type> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3839843358570171514L;
	private Pixel<Type> value;
	private int x, y;

	public SvoActivity(PyxisTContext context, int opcode, Pixel<Type> value, int x, int y, ActivityIdentifier parent) {
		super(context, opcode, parent);
		this.value = value;
		this.x = x;
		this.y = y;
	}

	@Override
	protected ImageData<Type> calculate(ImageData<?>[] parents) {
	    ImageData<Type> result;
	    ImageData<Type> source = (ImageData<Type>) parents[0];
		switch (getOpcode()) {
		case Opcode.SVO_SET:
			result = new ImageData<Type>(source.getArray().setSingleValue(
					value, x, y, source.inplace()));
			break;
		case Opcode.SVO_ADD:
			result = new ImageData<Type>(source.getArray().addSingleValue(
					value, x, y, source.inplace()));
			break;
		default:
			throw new Error("invalid opcode: " + getOpcode());
		}
		return result;
	}
}
