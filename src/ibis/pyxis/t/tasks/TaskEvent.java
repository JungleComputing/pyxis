package ibis.pyxis.t.tasks;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;

public class TaskEvent extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8466193688513082319L;
	
	public TaskEvent(ActivityIdentifier target, TaskActivity taskActivity) {
		super(null, target, taskActivity);
	}
	
	public TaskActivity getTask() {
		return (TaskActivity) this.data;
	}

}
