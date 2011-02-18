package ibis.pyxis.t.system;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;
import ibis.pyxis.t.tasks.TaskIdentifier;

public class TaskLookupRequest extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1976504872788891725L;
	
	public TaskLookupRequest(ActivityIdentifier source, ActivityIdentifier destination, TaskIdentifier nodeID) {
		super(source, destination, nodeID);
	}

	public TaskIdentifier getTaskID() {
		return (TaskIdentifier) data;
	}

}
