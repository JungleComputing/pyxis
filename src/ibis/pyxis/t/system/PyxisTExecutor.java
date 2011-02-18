package ibis.pyxis.t.system;

import ibis.constellation.Activity;
import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Executor;
import ibis.constellation.StealPool;
import ibis.constellation.StealStrategy;
import ibis.constellation.WorkerContext;
import ibis.constellation.context.UnitWorkerContext;
import ibis.pyxis.t.nodes.OperationIdentifier;
import ibis.pyxis.t.tasks.OperationDeregistration;
import ibis.pyxis.t.tasks.TaskRegistration;
import ibis.pyxis.t.tasks.TaskTrackerActivity;

import java.util.LinkedList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PyxisTExecutor extends Executor {

    /**
     * 
     */
    private static final long serialVersionUID = -179221617559966759L;

    private static final Logger logger = LoggerFactory
            .getLogger(PyxisTExecutor.class);

    protected static final String PYXIS_T_DEFAULT_CONTEXT  = "PyxisT.default";
//    protected static final String PYXIS_T_SOURCE_CONTEXT  = "PyxisT.source";
//    protected static final String PYXIS_T_PIPE_CONTEXT    = "PyxisT.pipe";
//    protected static final String PYXIS_T_JOIN_CONTEXT    = "PyxisT.join";
//    protected static final String PYXIS_T_SPLIT_CONTEXT   = "PyxisT.split";
//    protected static final String PYXIS_T_BRANCH_CONTEXT  = "PyxisT.branch";
//    protected static final String PYXIS_T_BARRIER_CONTEXT = "PyxisT.barrier";
    
    	protected static final WorkerContext pyxisWorkerContext = new UnitWorkerContext(PYXIS_T_DEFAULT_CONTEXT);

//    protected static final OrWorkerContext pyxisWorkerContext = new OrWorkerContext(
//            new UnitWorkerContext[] {
//                    new UnitWorkerContext(PYXIS_T_BRANCH_CONTEXT),
//                    new UnitWorkerContext(PYXIS_T_SPLIT_CONTEXT),
//                    new UnitWorkerContext(PYXIS_T_JOIN_CONTEXT),
//                    new UnitWorkerContext(PYXIS_T_PIPE_CONTEXT),
//                    new UnitWorkerContext(PYXIS_T_SOURCE_CONTEXT)
//            }, true);

    private ActivityIdentifier taskTracker = null;
    
    public PyxisTExecutor() {
    	//FIXME fix constellation such that the "stealFrom" can be just TASK_POOL
//        super(PyxisT.TASK_POOL, new StealPool(PyxisT.TASK_POOL, PyxisT.TRACKER_POOL), pyxisWorkerContext,
//                StealStrategy.SMALLEST, StealStrategy.BIGGEST,
//                StealStrategy.BIGGEST);
//        super(PyxisT.TASK_POOL, new StealPool(PyxisT.TASK_POOL, PyxisT.TRACKER_POOL), pyxisWorkerContext,
//                StealStrategy.SMALLEST, StealStrategy.SMALLEST,
//                StealStrategy.BIGGEST);
        super(StealPool.WORLD, StealPool.WORLD, pyxisWorkerContext,
                StealStrategy.SMALLEST, StealStrategy.BIGGEST,
                StealStrategy.BIGGEST);
    }
    
    /**
     * Looks up the location of an operation
     * @param source The Activity that did the request
     * @param operation The operation that needs to be located
     */
    public void lookup(ActivityIdentifier source, OperationIdentifier operation) {
    	//TODO do a local lookup first
   		send(new OperationLookupRequest(source, taskTracker, operation));
    }
    
    public void lookup(ActivityIdentifier source, Set<OperationIdentifier> operations) {
    	//TODO do a local lookup first
    	for(OperationIdentifier op: operations) {
    		// FIXME make batched requests?
    		lookup(source, op);
    	}
    }
    

    public void register(LinkedList<OperationIdentifier> operations) {
		send(new TaskRegistration(operations, taskTracker));	
	}
    
    public void deregister(OperationIdentifier operation) {
		send(new OperationDeregistration(operation, taskTracker));	
	}

	@Override
    public void run() {

        if (logger.isDebugEnabled()) {
            logger.debug("Starting Executor!");
        }
        
        taskTracker = submit(new TaskTrackerActivity());

        boolean done = false;

        while (!done) {
            done = processActivities();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Executor done!");
        }
    }

}
