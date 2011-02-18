package ibis.pyxis.t.tasks;

import ibis.constellation.ActivityIdentifier;

import java.io.Serializable;

public class TaskIdentifier implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5463114834100264742L;
	
		private final long taskID;
		private ActivityIdentifier activityID;
	
		private static long nextTaskId = 0;
	
		protected static synchronized TaskIdentifier generateID() {
			return new TaskIdentifier(nextTaskId++);
		}
		
		private TaskIdentifier(long id) {
			this.taskID = id;
			activityID = null;
		}
			
		public void setActivityID(ActivityIdentifier activityID) {
			this.activityID = activityID;
		}
		
		public ActivityIdentifier getActivityID() {
			return activityID;
		}
		
		public long taskID() {
			return taskID;
		}
	
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof TaskIdentifier) {
				TaskIdentifier other = (TaskIdentifier) obj;			
				if(other.taskID == taskID) {
					//they are equal, now update taskIdentifiers if needed
					if (activityID == null && other.activityID != null) {
						activityID = other.activityID;
					} else if (activityID != null && other.activityID == null) {
						other.activityID = activityID;
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
			return (int) ((Long)taskID).hashCode();
		}
	
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "< " + taskID + ", " + activityID + ">";
		}
	
}
