package ibis.pyxis.t.nodes;

import ibis.constellation.Constellation;
import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.parallel.operations.Operation;
import ibis.pyxis.t.system.PyxisT;
import ibis.pyxis.t.tasks.Task;

import java.util.HashSet;
import java.util.Set;

import jorus.array.Array2dFloat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Node {

	private static final Logger logger = LoggerFactory.getLogger(Node.class);

	private final int opcode;
	private ImageData image;
	private boolean internalNode;
	private final OperationIdentifier identifier;
//	PyxisTContext context;
	
	private Node[] input;
	private Set<Node> output;
	private int outputs;
	private Task task;

	protected Node(int opcode, Node... input) {
		this.opcode = opcode;
		
		this.input = input;
		output = new HashSet<Node>();
		outputs = 0;
		for (Node node : input) {
			node.addOut(this);
		}
		
		internalNode = false;
		identifier = OperationIdentifier.generateID();
		task = null;
		image = null;
	}
	
	protected Node(int opcode, Array2dFloat<?> array) {
		this.opcode = opcode;
		
		input = null;
		output = new HashSet<Node>();
		
		internalNode = false;
		identifier = OperationIdentifier.generateID();
		task = null;
		image = new ImageData(array, identifier);
	}
	
		
	public void addOut(Node node) {
		if(node != null) {
			output.add(node);
		}
		outputs++;
	}
	
	public void removeOut(Node node) {
		output.remove(node);
	}
	
	public Node[] getInputs() {
		input = initInput(input);
		return input;
	}
	
	public Set<Node> getOutputs() {
		return output;
	}
	
	public Task getTask() {
		return task;
	}
	
	public void setTask(Task task) {
		this.task = task;
		removeFromNodeGraph();
	}
	
	private void removeFromNodeGraph() {
		output.clear();
		input = null;
	}

	public Operation getOperation() {
		return createOperation(internalNode, outputs, getInputs());
	}
	
	protected int getOpcode() {
		return opcode;
	}
	/**************************************************************************/

//	private static PyxisTContext generateContext(Node[] parents) {
//		long rank = 0;
//		if (parents == null) {
////		return PyxisTContext.createSourceContext(rank);
//			return PyxisTContext.createDefaultContext(rank);
//		}
//	
//		for (Node node : parents) {
//			if (node.getContext().rank >= rank) {
//				rank = node.getContext().rank + 1;
//			}
//		}
//		return PyxisTContext.createDefaultContext(rank);
//	}

	/**
	 * @return the id
	 */
	public final OperationIdentifier identifier() {
		return identifier;
	}

	protected void addNodeToSource(Node source) {
		// FIXME stupid type hierarchy hack for NpoNode
		source.addOut(this);
	}
		
	/**
	 * Before operation creation, the actual implementation is allowed to
	 * override the parent set. Default implementation does nothing special.
	 * 
	 * @param The current parents
	 * @return The new parent set
	 */
	protected abstract Node[] initInput(Node[] parents);

	public ImageData getImage(PyxisT pyxis) {
		if (image == null) {
			image = pyxis.getImageFromTask(this);
		}
		
		return image;
	}

//	/**
//	 * @return the context
//	 */
//	public final PyxisTContext getContext() {
//		if (context == null) {
//			context = generateContext(input);
//		}
//		return context;
//	}

	protected abstract Operation createOperation(boolean internal, int outputs, Node... input);

	public final synchronized void referenceLost(Constellation constellation) {
		internalNode = true;
		
		if (task != null) {
			task.referenceLost(constellation, identifier(), outputs);
		}
	}	
}
