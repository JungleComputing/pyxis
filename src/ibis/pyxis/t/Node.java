package ibis.pyxis.t;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Constellation;
import ibis.constellation.SingleEventCollector;
import ibis.pyxis.ImageData;
import ibis.pyxis.t.parallel.ConnectEvent;
import ibis.pyxis.t.parallel.DeleteEvent;
import ibis.pyxis.t.parallel.ImageEvent;
import ibis.pyxis.t.parallel.activities.OperationActivity;
import ibis.pyxis.t.system.PyxisT;
import ibis.pyxis.t.system.PyxisTContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Node<Type> {

	private static final Logger logger = LoggerFactory.getLogger(Node.class);

	// private final Opcode opcode;
	private final int opcode;
	private ImageData<Type> image;
	private boolean garbage;

	ActivityIdentifier operation;

	private Node<Type>[] parents;

	private int children;

	private PyxisTContext context;

	private final long id;

	private static long nextId = 0;

	private static synchronized long generateID() {
		return nextId++;
	}

	protected static PyxisTContext generateContext(Node<?>[] parents,
			int children) {
		long rank = 0;
		if (parents == null) {
			return PyxisTContext.createSourceContext(rank);
		}

		int nParents = parents.length;
		for (Node<?> node : parents) {
			if (node.getContext().rank >= rank) {
				rank = node.getContext().rank + 1;
			}
		}

		if (children > 1) {
			if (nParents == 1) {
				return PyxisTContext.createSplitContext(rank);
			} else {
				return PyxisTContext.createBarrierContext(rank);
			}
		} else { // children <=1
			if (nParents == 1) {
				if (parents[0].children > 1) {
					return PyxisTContext.createBranchContext(rank);
				} else {
					return PyxisTContext.createPipeContext(rank);
				}
			} else {
				return PyxisTContext.createJoinContext(rank);
			}
		}
	}

	protected Node(int opcode, Node<Type>... parents) {
		context = null;
		// internal = false;
		this.opcode = opcode;
		image = null;

		children = 0;
		this.parents = parents;
		for (Node<Type> parent : parents) {
			parent.addChild(this);
		}
		garbage = false;
		id = generateID();
		// nodeDescriptor = null;
	}

	protected Node(int opcode, ImageData<Type> data) {
		context = null;
		// internal = false;
		this.opcode = opcode;
		image = data;
		children = 0;
		parents = null;
		garbage = false;
		id = generateID();
		// nodeDescriptor = null;
	}

	/**
	 * @return the id
	 */
	public final long getId() {
		return id;
	}

	protected void addChild(Node<Type> node) {
		children++;
	}

	protected void removeChild(Node<?> child) {
		children--;
	}

	protected void addNodeToSource(Node<Type> source) {
		// FIXME stupid type hierarchy hack for NpoNode
		source.addChild(this);
	}

	/**
	 * Constellation Activities are generated in here
	 * 
	 * @param constellation
	 * @return
	 */
	private ActivityIdentifier getOperation(Constellation constellation) {
		if (operation != null) {
			return operation;
		}
		// give implementation the opportunity to override the parent setup
		parents = setParents(parents);

		OperationActivity<Type> descriptor;

		if (parents == null || parents.length == 0) {
			synchronized (this) {
				descriptor = createOperation(opcode);
				operation = constellation.submit(descriptor);
				if (garbage) {
					constellation.send(new DeleteEvent(null, operation,
							children));
				}
			}
		} else {
			ActivityIdentifier[] ids = new ActivityIdentifier[parents.length];
			for (int i = 0; i < parents.length; i++) {
				ids[i] = parents[i].getOperation(constellation);
			}
			parents = null;
			synchronized (this) {
				descriptor = createOperation(opcode, ids);
				operation = constellation.submit(descriptor);
				if (garbage) {
					constellation.send(new DeleteEvent(null, operation,
							children));
				}
			}
		}
		return operation;
	}

	/**
	 * Before operation creation, the actual implementation is allowed to
	 * override the parent set. Default implementation does nothing.
	 * 
	 * @param The
	 *            current parents
	 * @return The new parent set
	 */
	protected abstract Node<Type>[] setParents(Node<Type>[] parents);
	
	protected Node<Type>[] getParents() {
		return parents;
	}

	/**
	 * Entry point of task graph execution. Add intelligence from here
	 * 
	 * @param constellation
	 *            The Constellation that will calculate this graph
	 * @return the resulting image
	 */
	@SuppressWarnings("unchecked")
	private ImageData<Type> execute(PyxisT pyxis) {
		Constellation constellation = pyxis.getConstellation();
		SingleEventCollector sec = new SingleEventCollector(generateContext(
				new Node<?>[] { this }, 0));
		ActivityIdentifier secID = constellation.submit(sec);

		constellation
				.send(new ConnectEvent(secID, getOperation(constellation)));
		children++; // The SingleEventcollector is also a child :-)

		ImageEvent result = (ImageEvent) sec.waitForEvent();
		return (ImageData<Type>) result.data;
	}

	protected ImageData<Type> getImage(PyxisT pyxis) {
		if (image == null) {
			pyxis.gc();
			image = execute(pyxis);
		}
		return image;
	}

	/**
	 * @return the context
	 */
	protected final PyxisTContext getContext() {
		if (context == null) {
			context = generateContext(parents, children);
		}
		return context;
	}

	protected abstract OperationActivity<Type> createOperation(int opcode,
			ActivityIdentifier... parents);

	public final synchronized void referenceLost(Constellation constellation) {
		garbage = true;
		if (operation != null) {
			constellation.send(new DeleteEvent(null, operation, children));
		}
	}
}
