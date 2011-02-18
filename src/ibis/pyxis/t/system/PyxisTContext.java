package ibis.pyxis.t.system;

import ibis.constellation.context.UnitActivityContext;


public class PyxisTContext extends UnitActivityContext {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2645962513581382089L;
    
    public static PyxisTContext createDefaultContext(long rank) {
        return new PyxisTContext(PyxisTExecutor.PYXIS_T_DEFAULT_CONTEXT, rank);
    }
    
//    public static PyxisTContext createSourceContext(long rank) {
//        return new PyxisTContext(PyxisTExecutor.PYXIS_T_SOURCE_CONTEXT, rank);
//    }
//        
//    public static PyxisTContext createPipeContext(long rank) {
//        return new PyxisTContext(PyxisTExecutor.PYXIS_T_PIPE_CONTEXT, rank);
//    }
//    
//    public static PyxisTContext createSplitContext(long rank) {
//        return new PyxisTContext(PyxisTExecutor.PYXIS_T_SPLIT_CONTEXT, rank);
//    }
//    
//    public static PyxisTContext createBranchContext(long rank) {
//        return new PyxisTContext(PyxisTExecutor.PYXIS_T_BRANCH_CONTEXT, rank);
//    }
//    
//    public static PyxisTContext createBarrierContext(long rank) {
//        return new PyxisTContext(PyxisTExecutor.PYXIS_T_BARRIER_CONTEXT, rank);
//    }
//    
//    public static PyxisTContext createJoinContext(long rank) {
//        return new PyxisTContext(PyxisTExecutor.PYXIS_T_JOIN_CONTEXT, rank);
//    }

    private PyxisTContext(String name, long rank) {
        super(name, rank);
    }
    

}
