package ibis.pyxis.t.tasks;

import ibis.constellation.Activity;
import ibis.constellation.ActivityContext;
import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;
import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.nodes.OperationIdentifier;
import ibis.pyxis.t.parallel.DeleteEvent;
import ibis.pyxis.t.parallel.DeleteRequest;
import ibis.pyxis.t.parallel.ImageEvent;
import ibis.pyxis.t.parallel.ImageRequest;
import ibis.pyxis.t.parallel.operations.Operation;
import ibis.pyxis.t.system.OperationLookupReply;
import ibis.pyxis.t.system.PyxisTExecutor;
import ibis.pyxis.t.system.TaskLookupReply;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskActivity extends Activity {
	private static final Logger logger = LoggerFactory
			.getLogger(TaskActivity.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 3304622612362953696L;
	private Operation[] operations;

	private transient HashSet<OperationIdentifier> missingInputs;

	private TaskIdentifier identifier;

	private Task[] subGraphs;

	private int finishedOperations;

	public TaskActivity(TaskIdentifier identifier, ActivityContext context,
			boolean restrictToLocal, Collection<Operation> operations,
			Task[] subGraphs) {
		super(context, restrictToLocal, true);
		if(operations != null) {
			this.operations = operations.toArray(new Operation[operations.size()]);
		}

		this.identifier = identifier;
		this.subGraphs = subGraphs;
		missingInputs = null;
		finishedOperations = 0;
	}

	private HashSet<OperationIdentifier> getMissingInputs() {
		if (missingInputs == null) {
			missingInputs = new HashSet<OperationIdentifier>();
			for (Operation operation : operations) {
				HashSet<OperationIdentifier> mips = operation.getMissingInput();
				if (mips != null) {
					missingInputs.addAll(operation.getMissingInput());
				} else {
					operation.getImage();
					// This operation can already be executed
				}
			}
		}
		return missingInputs;
	}

	private void foundInput(OperationIdentifier input) {
		if (missingInputs != null) {
			missingInputs.remove(input);
		}
	}

	public PyxisTExecutor getExecutor() {
		return (PyxisTExecutor) (super.getExecutor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ibis.constellation.Activity#initialize()
	 */
	@Override
	public void initialize() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Initializing task " + identifier());
		}
		PyxisTExecutor executor = getExecutor();

		identifier.setActivityID(identifier());
		LinkedList<OperationIdentifier> opIDs = new LinkedList<OperationIdentifier>();
		for (Operation op : operations) {
			OperationIdentifier opID = op.identifier();
			opIDs.add(opID);
		}
		// if(logger.isDebugEnabled()) {
		// logger.debug("identifiers initialized");
		// }

		for (Task task : subGraphs) {
			task.startActivity(executor);
			// if(logger.isDebugEnabled()) {
			// logger.debug("subGraph of task " +
			// task.getActivity().identifier() + " submitted");
			// }
		}
		subGraphs = null;

		// TODO set operation IDs
		executor.register(opIDs);

		// collect all input operations for this Activity
		// clone to prevent ConcurrentModificationException @foundInput() a few
		// lines below
		@SuppressWarnings("unchecked")
		HashSet<OperationIdentifier> missingInputs = (HashSet<OperationIdentifier>) getMissingInputs()
				.clone();

		// if(logger.isDebugEnabled()) {
		// logger.debug("Input operations identified");
		// }

		// connect to all input operations of this Activity
		for (OperationIdentifier input : missingInputs) {
			TaskIdentifier taskID = input.getTaskID();
			if (taskID != null) {
				// taskID found
				ActivityIdentifier AID = taskID.getActivityID();
				if (AID != null) { // parent is already known, send Image
									// request immediately
					executor.send(new ImageRequest(identifier(), AID, input
							.getOperationID()));
					foundInput(input);
				} else { // parent is missing
					executor.lookup(identifier(), input);
				}
			} else {
				executor.lookup(identifier(), input);
			}
		}
		// if(logger.isDebugEnabled()) {
		// logger.debug("Lookup requests sent");
		// }

		if (logger.isDebugEnabled()) {
			logger.info("Task " + identifier() + " initialized");
		}
		suspend();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ibis.constellation.Activity#process(ibis.constellation.Event)
	 */
	@Override
	public void process(Event e) throws Exception {
		if (e instanceof TaskLookupReply) {
			TaskIdentifier parent = ((TaskLookupReply) e).getTaskID();
			// TODO what now?
			// find operations to look up at this task
			HashSet<OperationIdentifier> mips = getMissingInputs();
			for (OperationIdentifier op : mips) {
				if (op.getTaskID().equals(parent)) {
					op.setTaskID(parent);
					foundInput(op);
					ImageRequest ir = new ImageRequest(identifier(),
							parent.getActivityID(), op.getOperationID());
					getExecutor().send(ir);
				}
			}
			suspend();
		} else if (e instanceof OperationLookupReply) {
			OperationIdentifier parent = ((OperationLookupReply) e)
					.getOperationID();
			executor.send(new ImageRequest(identifier(), parent.getTaskID()
					.getActivityID(), parent.getOperationID()));
			suspend();
		} else if (e instanceof ImageEvent) {
			if (logger.isDebugEnabled()) {
				logger.debug("ImageEvent received, ID = "
						+ ((ImageEvent) e).getImage().getOperationId()
								.toString());
			}
			ImageData receivedImage = ((ImageEvent) e).getImage();
			for(int i = 0; i < operations.length; i++) {
				operations[i].addInputImage(receivedImage);
				ImageData image = operations[i].getImage();
				if (image != null) {
					HashSet<ActivityIdentifier> outputs = operations[i]
							.removeOutputs();
					if (outputs != null) {
						for (ActivityIdentifier output : outputs) {
							executor.send(new ImageEvent(identifier(), output,
									image));
						}
					}
					if(operations[i].finished()) {
						operations[i] = null;
						finishedOperations++;
					}
				}
			}
			endAction();
		} else if (e instanceof ImageRequest) {
			if (logger.isDebugEnabled()) {
				logger.debug("ImageRequest received from " + e.source
						+ ", ID = " + ((ImageRequest) e).getoperationID());
			}
			long operationID = ((ImageRequest) e).getoperationID();
			for(int i = 0; i < operations.length; i++) {
				if (operationID == operations[i].identifier().getOperationID()) {
					if (operations[i].addOutput(e.source)) {
						executor.send(new ImageEvent(e.target, e.source,
								operations[i].getImage()));
					}
					if(operations[i].finished()) {
						operations[i] = null;
						finishedOperations++;
					}
					endAction();
					return;
				}
			}
			// FIXME PANIC!!!!!!!!!!
			logger.error("Invalid Connect Request:" + e.toString());
			suspend();
		} else if (e instanceof DeleteEvent) {
			if (logger.isDebugEnabled()) {
				logger.debug("DeleteEvent received at " + identifier());
			}

			DeleteRequest dr = ((DeleteEvent) e).getDeleteRequest();

			OperationIdentifier target = dr.getOperationID();
			for(int i = 0; i < operations.length; i++) {
				if (target.equals(operations[i].identifier())) {
					operations[i].internal(dr.getTotalChildren());
					if (operations[i].finished()) {
						operations[i] = null;
						finishedOperations++;
					}
					endAction();
					return;
				}
			}
			// FIXME PANIC!!!!!!!!!!
			logger.error("Invalid Delete Request:" + e.toString());
			return;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Unknown event:" + e);
			}
			throw new Exception("Invalid Event");
		}
	}

	private void endAction() {
		if (operations == null || operations.length == finishedOperations) {
			// let garbage collector collect the operations
			finish();
		} else {
			suspend();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ibis.constellation.Activity#cleanup()
	 */
	@Override
	public void cleanup() throws Exception {
//		System.out.println("Activity " + identifier() + " finished");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ibis.constellation.Activity#cancel()
	 */
	@Override
	public void cancel() throws Exception {
		// TODO Auto-generated method stub
	}

	// public List<OperationIdentifier> getOperationIDs() {
	// List<OperationIdentifier> result = new LinkedList<OperationIdentifier>();
	// for(Operation op: operations) {
	// result.add(op.identifier());
	// }
	// return result;
	// }
}
