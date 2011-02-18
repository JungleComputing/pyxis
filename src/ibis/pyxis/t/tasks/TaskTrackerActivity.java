package ibis.pyxis.t.tasks;

import ibis.constellation.Activity;
import ibis.constellation.Event;
import ibis.constellation.context.UnitActivityContext;
import ibis.pyxis.t.system.OperationLookupRequest;
import ibis.pyxis.t.system.TaskLookupRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskTrackerActivity extends Activity {

	private static final Logger logger = LoggerFactory
			.getLogger(TaskTrackerActivity.class);
	
	public static final UnitActivityContext TASK_TRACKER_CONTEXT = new UnitActivityContext("TASK_TRACKER");
	
	public TaskTrackerActivity() {
		super(TASK_TRACKER_CONTEXT, false, true);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7548070760568334957L;

	@Override
	public void initialize() throws Exception {
		if(logger.isDebugEnabled()) {
			logger.debug("TaskTrackerActivity" + identifier() + " initialized");
		}
		suspend();
	}

	@Override
	public void process(Event e) throws Exception {
		if (e instanceof OperationLookupRequest) {
			((TaskTracker)getExecutor()).lookup((OperationLookupRequest)(e));
		} else if (e instanceof TaskLookupRequest) {
			((TaskTracker)getExecutor()).lookup((TaskLookupRequest)(e));
		} else if (e instanceof TaskRegistration) {
			((TaskTracker)getExecutor()).register((TaskRegistration)(e));
		} else if (e instanceof OperationDeregistration) {
			((TaskTracker)getExecutor()).deregister((OperationDeregistration)(e));
		} else {
			logger.error("unknown event: " + e);
		}
		suspend();
	}

	@Override
	public void cleanup() throws Exception {
//		System.out.println("TaskTrackerActivity " + identifier() + " finished");
	}

	@Override
	public void cancel() throws Exception {
		
	}
	

}
