package ibis.pyxis.t.nodes;

import ibis.pyxis.t.parallel.operations.NpoOperation;
import ibis.pyxis.t.parallel.operations.Operation;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NpoNode extends Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7327921696258168338L;

	private static final Logger logger = LoggerFactory.getLogger(NpoNode.class);

	private transient LinkedList<Node> sources;
	private boolean alreadyProcessed;

	public NpoNode(int opcode, Node source) {
		super(opcode);
		this.sources = new LinkedList<Node>();
		alreadyProcessed = false;
		addSource(source);
	}

	public boolean addSource(Node source) {
		if (alreadyProcessed) {
			logger.error("PANIC!!! already executed");
			return false;
		}
		sources.addLast(source);
		addNodeToSource(source);
		return true;
	}

	@Override
	public Operation createOperation(boolean internal, int outputs, Node... input) {
		return new NpoOperation(identifier(), internal, outputs, getOpcode(), input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pyxis.t.Node#getParentsforExecution()
	 */
	@Override
	protected Node[] initInput(Node[] parents) {
		if (sources == null) {
			// input already initialized before
			return parents;
		} else {
			Node[] array = new Node[sources.size()];
			array = sources.toArray(array);
			sources = null;
			return array;
			
		}
	}

}
