/**
 * 
 */
package ibis.pyxis.t.tasks;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Executor;
import ibis.constellation.StealPool;
import ibis.constellation.StealStrategy;
import ibis.constellation.context.UnitWorkerContext;
import ibis.pyxis.t.nodes.OperationIdentifier;
import ibis.pyxis.t.system.OperationLookupReply;
import ibis.pyxis.t.system.OperationLookupRequest;
import ibis.pyxis.t.system.PyxisT;
import ibis.pyxis.t.system.TaskLookupReply;
import ibis.pyxis.t.system.TaskLookupRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Timo van Kessel
 * 
 */
public class TaskTracker extends Executor {

	private static final Logger logger = LoggerFactory
			.getLogger(TaskTracker.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 2378316867513348313L;
	public static final UnitWorkerContext TASK_TRACKER_CONTEXT = new UnitWorkerContext(
			"TASK_TRACKER");


	private HashMap<Long, TaskIdentifier> taskMap;
	private HashMap<Long, HashSet<ActivityIdentifier>> pendingTaskLookups;

	private HashMap<Long, OperationIdentifier> operationMap;
	private HashMap<Long, HashSet<ActivityIdentifier>> pendingOperationLookups;
	
	/**
	 * 
	 */
	public TaskTracker() {
//		super(PyxisT.TRACKER_POOL, PyxisT.TASK_POOL, TASK_TRACKER_CONTEXT,
//				StealStrategy.ANY, StealStrategy.ANY, StealStrategy.ANY);
		super(StealPool.WORLD, StealPool.WORLD, TASK_TRACKER_CONTEXT,
				StealStrategy.ANY, StealStrategy.ANY, StealStrategy.ANY);
		taskMap = new HashMap<Long, TaskIdentifier>();
		pendingTaskLookups = new HashMap<Long, HashSet<ActivityIdentifier>>();

		operationMap = new HashMap<Long, OperationIdentifier>();
		pendingOperationLookups = new HashMap<Long, HashSet<ActivityIdentifier>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ibis.constellation.Executor#run()
	 */
	@Override
	public void run() {
		if (logger.isDebugEnabled()) {
			logger.debug("Starting TaskTracker!");
		}
		
		boolean done = false;

		while (!done) {
			done = processActivities();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("TaskTracker done!");
		}
	}

	public void lookup(OperationLookupRequest operationLookupRequest) {
		ActivityIdentifier source = operationLookupRequest.source;
		OperationIdentifier requestID = operationLookupRequest.getOperationID();
		OperationIdentifier result = operationMap.get(requestID);
		if (result == null) {
			// operation not found, put request in "pending" set
			long opID = requestID.getOperationID();
			HashSet<ActivityIdentifier> set = pendingOperationLookups.get(opID);
			if (set == null) {
				set = new HashSet<ActivityIdentifier>();
				pendingOperationLookups.put(opID, set);
			}
			set.add(source);
		} else {
			//operation found, send result
			send(new OperationLookupReply(source, result));
		}
	}

	public void lookup(TaskLookupRequest taskLookupRequest) {
		ActivityIdentifier source = taskLookupRequest.source;
		TaskIdentifier requestID = taskLookupRequest.getTaskID();
		TaskIdentifier result = taskMap.get(requestID);
		if (result == null) {
			// task not found, put request in "pending" set
			long taskID = requestID.taskID();
			HashSet<ActivityIdentifier> set = pendingOperationLookups
					.get(taskID);
			if (set == null) {
				set = new HashSet<ActivityIdentifier>();
				pendingOperationLookups.put(taskID, set);
			}
			set.add(source);
		} else {
			//task found, send result
			send(new TaskLookupReply(source, result));
		}
		
	}

	private void doPendingLookups(OperationIdentifier operationID) {
		HashSet<ActivityIdentifier> set = pendingOperationLookups
				.remove(operationID.getOperationID());
		if (set != null) {
			for (ActivityIdentifier target : set) {
				send(new OperationLookupReply(target, operationID));
			}
		}
	}

	private void doPendingLookups(TaskIdentifier taskID) {
		HashSet<ActivityIdentifier> set = pendingTaskLookups.remove(taskID
				.taskID());
		if (set != null) {
			for (ActivityIdentifier target : set) {
				send(new TaskLookupReply(target, taskID));
			}
		}
	}

	public void register(TaskRegistration taskRegistration) {
		LinkedList<OperationIdentifier> identifiers = taskRegistration.getIDs();
		
		// add task to taskMap
		TaskIdentifier task = identifiers.getFirst().getTaskID();
		long taskID = task.taskID();

		TaskIdentifier previousTask = taskMap.put(taskID, task);
		if (previousTask == null) { // all good
			doPendingLookups(task);
		} else if (previousTask.equals(task)) {
			// already registered, skip handling of pending lookups
		} else { // previous was another ActivityIdentifier: PANIC!!!
			// FIXME error
			throw new Error("Task already registered at another activity");
		}

		// add operations to operationMap
		for (OperationIdentifier operationID : identifiers) {
			if (logger.isDebugEnabled()) {
				if (operationID.getTaskID() == null) {
					logger.debug("HELP: TaskIdentifier missing on Task registration!");
				}
			}

			OperationIdentifier previous = operationMap.put(operationID.getOperationID(), operationID);
			if (previous == null) { // all good
				doPendingLookups(operationID);
			} else if (previous.equals(taskID)) {
				// already registered, skip handling of pending lookups
				logger.debug("Operation already registered at this activity");
			} else { // previous was another ActivityIdentifier: PANIC!!!
				// FIXME error
				throw new Error("Operation already registered at another activity");
			}
		}
	}

	public void deregister(OperationDeregistration operationDeregistration) {
		OperationIdentifier identifier = operationDeregistration.getID();
		HashSet<ActivityIdentifier> pendingRequests = pendingOperationLookups.remove(identifier.getOperationID());
		if (pendingRequests == null || pendingRequests.isEmpty()) { // all good
			return;
		} else { // TODO huh, shouldn't be happening
			identifier.setTaskID(null);
			for (ActivityIdentifier target : pendingRequests) {
				send(new OperationLookupReply(target, identifier));
			}
		}
		// TODO also deregister task
		// FIXME what about pending node lookups???
	}

}
