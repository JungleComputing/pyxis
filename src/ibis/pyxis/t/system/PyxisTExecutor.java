package ibis.pyxis.t.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ibis.constellation.Executor;
import ibis.constellation.StealPool;
import ibis.constellation.StealStrategy;
import ibis.constellation.WorkerContext;
import ibis.constellation.context.OrWorkerContext;
import ibis.constellation.context.UnitWorkerContext;

public class PyxisTExecutor extends Executor {

    /**
     * 
     */
    private static final long serialVersionUID = -179221617559966759L;

    private static final Logger logger = LoggerFactory
            .getLogger(PyxisTExecutor.class);

    protected static final String PYXIS_T_SOURCE_CONTEXT  = "PyxisT.source";
    protected static final String PYXIS_T_PIPE_CONTEXT    = "PyxisT.pipe";
    protected static final String PYXIS_T_JOIN_CONTEXT    = "PyxisT.join";
    protected static final String PYXIS_T_SPLIT_CONTEXT   = "PyxisT.split";
    protected static final String PYXIS_T_BRANCH_CONTEXT  = "PyxisT.branch";
    protected static final String PYXIS_T_BARRIER_CONTEXT = "PyxisT.barrier";

    protected static final OrWorkerContext pyxisWorkerContext = new OrWorkerContext(
            new UnitWorkerContext[] {
                    new UnitWorkerContext(PYXIS_T_BRANCH_CONTEXT),
                    new UnitWorkerContext(PYXIS_T_SPLIT_CONTEXT),
                    new UnitWorkerContext(PYXIS_T_JOIN_CONTEXT),
                    new UnitWorkerContext(PYXIS_T_PIPE_CONTEXT),
                    new UnitWorkerContext(PYXIS_T_SOURCE_CONTEXT)
            }, true);

    public PyxisTExecutor() {
        super(StealPool.WORLD, StealPool.WORLD, pyxisWorkerContext,
                StealStrategy.BIGGEST, StealStrategy.SMALLEST,
                StealStrategy.SMALLEST);
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
