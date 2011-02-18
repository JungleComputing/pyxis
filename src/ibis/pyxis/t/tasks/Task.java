package ibis.pyxis.t.tasks;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Constellation;
import ibis.constellation.Executor;
import ibis.pyxis.t.nodes.OperationIdentifier;
import ibis.pyxis.t.parallel.DeleteEvent;
import ibis.pyxis.t.parallel.operations.Operation;
import ibis.pyxis.t.system.PyxisTContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Task implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3658950250476535566L;

	private static final Logger logger = LoggerFactory.getLogger(Task.class);

	private HashSet<Task> out;
	private HashSet<Task> in;
	private HashMap<OperationIdentifier, Operation> operations;

	private TaskActivity activity;

	private TaskIdentifier identifier;

	private long rank; //1 + rank of all input tasks

	public Task(Operation... operations) {
		out = new HashSet<Task>();
		in = new HashSet<Task>();
		this.operations = new HashMap<OperationIdentifier, Operation>();
		if (operations != null) {
			for (Operation operation : operations) {
				this.operations.put(operation.identifier(), operation);
			}
		}
		identifier = TaskIdentifier.generateID();

		rank = 0;
	}

	protected void addOut(Task task) {
		out.add(task);
	}

	private void removeOut(Task task) {
		out.remove(task);
	}

	protected void addIn(Task task) {
		in.add(task);
	}

	private void removeIn(Task task) {
		in.remove(task);
	}

	public TaskActivity getActivity() {
		return activity;
	}

	protected synchronized void startActivity(Constellation constellation) {
		if (activity == null) {
			// restrict to local when task is a source task
//			boolean restrictToLocal = in.size() == 0;
			boolean restrictToLocal = false;
			activity = new TaskActivity(identifier,
					PyxisTContext.createDefaultContext(rank), restrictToLocal,
					operations.values(), in.toArray(new Task[in.size()]));
			ActivityIdentifier activityID = constellation.submit(activity);
			setActivity(activityID);
			// For the sake of garbage collection:
			removeFromGraph();
		}
	}

	protected synchronized void startActivity(Executor executor) {
		if (activity == null) {
			// restrict to local when task is a source task
			
			// boolean restrictToLocal = in.size() == 0;
			boolean restrictToLocal = false;
			activity = new TaskActivity(identifier,
					PyxisTContext.createDefaultContext(rank), restrictToLocal,
					operations.values(), in.toArray(new Task[in.size()]));
			ActivityIdentifier activityID = executor.submit(activity);
			setActivity(activityID);
			// For the sake of garbage collection:
			removeFromGraph();
		}
	}

	private void setActivity(ActivityIdentifier activityID) {
		identifier.setActivityID(activityID);
		for (Operation op : operations.values()) {
			op.identifier().setTaskID(identifier);
		}
	}

	public synchronized void referenceLost(Constellation constellation, OperationIdentifier operation, int outputs) {
		if (activity == null) {
			//activity is not started yet, We tell in advance that this operation is internal
			Operation op = operations.get(operation);
			op.internal(outputs);
		} else {
			// activity already started, we just send a Delete Event
			constellation.send(new DeleteEvent(activity.identifier(), activity.identifier(),
					operation, outputs));
			//we tell Constellation this Activity sends this message to itself
		}
	}

	private void removeFromGraph() {
		for (Task input : in) {
			input.removeOut(this);
		}
		in.clear();

		for (Task output : out) {
			output.removeIn(this);
		}
		out.clear();
	}

	/**
	 * 
	 * @param parent
	 * @return the rank of the Task
	 */
	protected void makeTree(Task parent) {
		// parent is the only output node
		if (parent != null) {
			for (Task task : out) {
				if (task != parent) {
					task.removeIn(this);
				}
			}
			out.clear();
			out.add(parent);
		}

		// I am the parent of all my children
		for (Task task : in) {
			// TODO warning: recursive tree editing loop: is the iterator
			// invalid in here? check it!
			if (in.contains(task)) {
				task.makeTree(this);
			}
		}
		
	}

	protected long generateBFSRanks() {
		if(rank == 0) {
			rank = 1;
			for(Task input: in) {
				rank += input.generateBFSRanks();
			}
		}
		return rank;
	}
	
	protected void generateDFSRanks(long parentRank) {
		rank = parentRank-1;
		for(Task input: in) {
			input.generateDFSRanks(rank);
		}
	}
	
	@Override
	public String toString() {
		return "DeleteEvent" + super.toString();
	}
}
