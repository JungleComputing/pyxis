package ibis.pyxis.t.parallel;

import ibis.pyxis.t.nodes.OperationIdentifier;

import java.io.Serializable;

public class DeleteRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7288727683577995292L;
	private final OperationIdentifier operationID;
	private final int totalChildren;
	
	public DeleteRequest(OperationIdentifier operationID, int totalChildren) {
		this.operationID = operationID;
		this.totalChildren = totalChildren;
	}

	public OperationIdentifier getOperationID() {
		return operationID;
	}

	public int getTotalChildren() {
		return totalChildren;
	}
}
