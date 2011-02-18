package ibis.pyxis.t.system;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;
import ibis.pyxis.t.nodes.OperationIdentifier;

public class OperationLookupReply extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7409802518016188964L;
	
	public OperationLookupReply(ActivityIdentifier target, OperationIdentifier operationIdentifier) {
		super(null, target, operationIdentifier);
	}
	
	public OperationIdentifier getOperationID() {
		return (OperationIdentifier) this.data;
	}
}
