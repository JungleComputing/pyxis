package ibis.pyxis.t.tasks;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;
import ibis.pyxis.t.nodes.OperationIdentifier;

public class OperationDeregistration extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3692128757751515612L;
	
	public OperationDeregistration(OperationIdentifier identifier, ActivityIdentifier target) {
		super(identifier.getTaskID().getActivityID(), target, identifier);
	}
	
	public OperationIdentifier getID() {		
		return (OperationIdentifier) data;
	}
}
