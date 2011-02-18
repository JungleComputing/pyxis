package ibis.pyxis.t.system;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;
import ibis.pyxis.t.nodes.OperationIdentifier;

public class OperationLookupRequest extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1976504872788891725L;
	
	protected OperationLookupRequest(ActivityIdentifier source, ActivityIdentifier destination, OperationIdentifier operationID) {
		super(source, destination, operationID);
	}

	public OperationIdentifier getOperationID() {
		return (OperationIdentifier) data;
	}

}
