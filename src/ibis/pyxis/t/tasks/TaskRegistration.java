package ibis.pyxis.t.tasks;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;
import ibis.pyxis.t.nodes.OperationIdentifier;

import java.util.LinkedList;

public class TaskRegistration extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3692128757751515612L;
	
	public TaskRegistration(LinkedList<OperationIdentifier> identifiers, ActivityIdentifier target) {
		super(identifiers.getFirst().getTaskID().getActivityID(), target, identifiers);
	}

	@SuppressWarnings("unchecked")
	public LinkedList<OperationIdentifier> getIDs() {		
		return (LinkedList<OperationIdentifier>) data;
	}

}
