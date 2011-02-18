package ibis.pyxis.t.parallel.operations;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.nodes.OperationIdentifier;

import java.io.Serializable;
import java.util.HashSet;

public abstract class Operation implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -2074986705893661691L;

	private final int opcode;
	private final OperationIdentifier operationID;
	private ImageData image;
	private final OperationIdentifier[] input;
	private ImageData[] inputImages;
	private int attachedInputs;
	private HashSet<ActivityIdentifier> outputs;
	private int attachedOutputs;
	private volatile int totalOutputs;
	private volatile boolean internal;

	protected Operation(OperationIdentifier operationID, boolean internal,
			int outputs, int opcode, Node... inputNodes) {
		image = null;
		this.operationID = operationID;
		this.opcode = opcode;

		if (inputNodes == null || inputNodes.length == 0) {
			input = null;
		} else {
			input = new OperationIdentifier[inputNodes.length];
			for (int i = 0; i < inputNodes.length; i++) {
				input[i] = inputNodes[i].identifier();
			}
		}

		inputImages = null;
		attachedInputs = 0;

		attachedOutputs = 0;

		this.internal = internal;
		totalOutputs = -1;//outputs;
	}

	public final int getOpcode() {
		return opcode;
	}

	public final OperationIdentifier identifier() {
		return operationID;
	}

	public boolean isReady() {

		return (input == null || attachedInputs == input.length);
	}

	public HashSet<OperationIdentifier> getMissingInput() {
		if (input == null || input.length == 0) {
			return null;
		}
		HashSet<OperationIdentifier> result = new HashSet<OperationIdentifier>();
		if (inputImages != null) {
			for (int i = 0; i < input.length; i++) {
				if (inputImages[i] == null) {
					result.add(input[i]);
				}
			}
		} else {
			for (OperationIdentifier op : input) {
				result.add(op);
			}
		}

		return result;
	}

	public void addInputImage(ImageData image) {
		if (input != null) {
			OperationIdentifier id = image.getOperationId();
			for (int i = 0; i < input.length; i++) {
				if (id.equals(input[i])) {
					if (inputImages == null) {
						inputImages = new ImageData[input.length];
					}
					inputImages[i] = image;
					processImageEvent(i, image);
					attachedInputs++;
				}
			}
		}
	}

	public ImageData getImage() {
		if (image == null && isReady()) {
			image = execute(inputImages);
			// save memory: throw away the input images
			inputImages = null;
		}
		return image;
	}

	protected abstract void processImageEvent(int index, ImageData image);

	protected abstract ImageData execute(ImageData[] parents);

	public void internal(int outputs) {
		if (!internal) {
			internal = true;
			totalOutputs = outputs;
		}
	}

	public boolean isInternal() {
		return internal;
	}

	/**
	 * 
	 * @param source
	 * @return true when result is/can be calculated for this operation, and the
	 *         reply should be sent immediately
	 */
	public boolean addOutput(ActivityIdentifier source) {
		attachedOutputs++;
		if (image != null || isReady()) {
			return true;
		} else {
			if (outputs == null) {
				outputs = new HashSet<ActivityIdentifier>();
			}
			outputs.add(source);
			return false;
		}
	}

	public HashSet<ActivityIdentifier> removeOutputs() {
		HashSet<ActivityIdentifier> result = outputs;
		outputs = null;
		return result;
	}

	public boolean finished() {
		return internal && (image != null) && (attachedOutputs == totalOutputs)
				&& (outputs == null);
	}
}
