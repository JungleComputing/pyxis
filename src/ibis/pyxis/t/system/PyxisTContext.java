package ibis.pyxis.t.system;

import ibis.constellation.context.UnitActivityContext;


public class PyxisTContext extends UnitActivityContext {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2645962513581382089L;
    

    public PyxisTContext(long rank) {
        super(PyxisTExecutor.PYXIS_T_CONTEXT_NAME, rank);
    }
    

}
