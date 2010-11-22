package ibis.pyxis.t.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ibis.constellation.Executor;
import ibis.constellation.StealPool;
import ibis.constellation.StealStrategy;
import ibis.constellation.WorkerContext;
import ibis.constellation.context.UnitWorkerContext;

public class PyxisTExecutor extends Executor {

    /**
     * 
     */
    private static final long serialVersionUID = -179221617559966759L;
    
    protected static final String PYXIS_T_CONTEXT_NAME = "PyxisT";

    private static final Logger logger = LoggerFactory
            .getLogger(PyxisTExecutor.class);

    public PyxisTExecutor() {
        super(StealPool.WORLD, StealPool.WORLD, new UnitWorkerContext(PYXIS_T_CONTEXT_NAME),
                StealStrategy.BIGGEST, StealStrategy.SMALLEST, StealStrategy.SMALLEST);
    }

    @Override
    public void run() {

        if (logger.isDebugEnabled()) {
            logger.debug("Starting Executor!");
        }

        boolean done = false;

        while (!done) {
            done = processActivities();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Executor done!");
        }
    }

}
