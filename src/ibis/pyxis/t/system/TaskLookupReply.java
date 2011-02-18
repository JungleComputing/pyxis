package ibis.pyxis.t.system;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;
import ibis.pyxis.t.tasks.TaskIdentifier;

public class TaskLookupReply extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7409802518016188964L;
	
	public TaskLookupReply(ActivityIdentifier target, TaskIdentifier taskIdentifier) {
		super(null, target, taskIdentifier);
	}
	
	public TaskIdentifier getTaskID() {
		return (TaskIdentifier) this.data;
	}
}
