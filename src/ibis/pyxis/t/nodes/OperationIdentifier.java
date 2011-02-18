package ibis.pyxis.t.nodes;

import ibis.pyxis.t.tasks.TaskIdentifier;

import java.io.Serializable;

public class OperationIdentifier implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5463114834100264742L;

	private final long operationID;
	private TaskIdentifier taskID;

	private static long nextOperationId = 0;

	protected static synchronized OperationIdentifier generateID() {
		return new OperationIdentifier(nextOperationId++);
	}
	
	private OperationIdentifier(long id) {
		this.operationID = id;
		taskID = null;
	}
		
	public void setTaskID(TaskIdentifier taskID) {
		this.taskID = taskID;
	}
	
	public TaskIdentifier getTaskID() {
		return taskID;
	}
	
	public long getOperationID() {
		return operationID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof OperationIdentifier) {
			OperationIdentifier other = (OperationIdentifier) obj;			
			if(other.operationID == operationID) {
				//they are equal, now update taskIdentifiers if needed
				if (taskID == null && other.taskID != null) {
					taskID = other.taskID;
				} else if (taskID != null && other.taskID == null) {
					other.taskID = taskID;
				}
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (int) ((Long)operationID).hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "< " + operationID + ", " + taskID + ">";
	}
	
	

}
