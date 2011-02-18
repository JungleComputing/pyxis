package ibis.pyxis;

import java.io.IOException;

public abstract class Pyxis {

//    private static Pyxis pyxis;
//
//    public static Pyxis getPyxis() {
//        return pyxis;
//    }

//    public static Pyxis init(boolean dataparallel, boolean taskparallel)
//            throws Exception {
//        if (dataparallel) {
//            if (taskparallel) {
//                // TODO init Pyxis-DT
////                pyxis = new PyxisDT();
//
//            } else {
//                // TODO init Pyxis-D
////                pyxis = new PyxisD();
//            }
//        } else if (taskparallel) {
//            pyxis = new PyxisT();
//            // init Pyxis-T
//        } else {
//        	//sequential
//        	// TODO init Pyxis
//        }
//        
//        return pyxis;
//    }

    protected Pyxis() {
    }
    
    /**
     * Clean up Pyxis when done
     */
    public abstract void end();


    /**
     * Convert an {@link ibis.imaging4j.Image} image to the {@link FloatImageT}
     * format
     * 
     * @param image
     *            the source image
     * @return the resulting {@link FloatImageT}
     * @throws IOException
     *             if the conversion for the specific image is not supported
     */
    public abstract Image importFloatImage(ibis.imaging4j.Image image)
            throws IOException;

    public abstract Image createImage(int width, int height, int extent,
            float[] data);


//    /**
//	 * Override when Constellation is present
//	 * @return the constellation for this Pyxis
//	 */
//	public Constellation getConstellation() {
//	    return null;
//	}

//	public abstract void gc();
    
    public abstract boolean isMaster();

}
