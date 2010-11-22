package ibis.pyxis.t.taskgraph.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.Opcode;
import ibis.pyxis.t.system.PyxisTContext;
import jorus.pixel.Pixel;

public final class UpoDescriptor<Type> extends OperationDescriptor<Type> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7645403880059955722L;
    private Pixel<Type> value;

    public UpoDescriptor(PyxisTContext context, int opcode, Pixel<Type> value, ActivityIdentifier parent) {
        super(context, opcode, parent);
        this.value = value;
    }

    @Override
    protected ImageData<Type> calculate(Object[] parents) {
        ImageData<Type> image = (ImageData<Type>) parents[0];
        ImageData<Type> result;

        switch (getOpcode()) {
        case Opcode.UPO_SET_PIXEL:
            result = new ImageData<Type>(image.getArray().setVal(value,
                    image.inplace()));
            break;
        default:
            throw new Error("invalid opcode: " + getOpcode());
        }
        return result;
    }

}
