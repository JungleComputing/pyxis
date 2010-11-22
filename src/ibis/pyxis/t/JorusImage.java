///**
// * 
// */
//package ibis.pyxis.t;
//
//import ibis.pyxis.ImageData;
//import jorus.array.Array2d;
//
///**
// * @author timo
// *
// */
//public class JorusImage<T> implements ImageData<T> {
//
//    /**
//     * 
//     */
//    private static final long serialVersionUID = 1634228011716448036L;
//
//    private Array2d<T,?> array;
//    
//    public JorusImage(Array2d<T,?> array) {
//        this.array = array;
//        
//    }
//    
//    private JorusImage(JorusImage<T> jorusImage) {
//        // TODO Auto-generated constructor stub
//    }
//    
//    /* (non-Javadoc)
//     * @see pyxis.ImageData#getExtent()
//     */
//    @Override
//    public int getExtent() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
//    /* (non-Javadoc)
//     * @see pyxis.ImageData#getWidth()
//     */
//    @Override
//    public int getWidth() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
//    /* (non-Javadoc)
//     * @see pyxis.ImageData#getHeight()
//     */
//    @Override
//    public int getHeight() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
//    /* (non-Javadoc)
//     * @see pyxis.ImageData#equalDimensions(pyxis.ImageData)
//     */
//    @Override
//    public boolean equalDimensions(ImageData<?> other) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /* (non-Javadoc)
//     * @see pyxis.ImageData#equalDataType(pyxis.ImageData)
//     */
//    @Override
//    public boolean equalDataType(ImageData<?> other) {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /* (non-Javadoc)
//     * @see pyxis.ImageData#getArray()
//     */
//    @Override
//    public Array2d<T, ?> getArray() {
//        return array;
//    }
//
//    /* (non-Javadoc)
//     * @see pyxis.ImageData#inplace()
//     */
//    @Override
//    public boolean inplace() {
//        // TODO Auto-generated method stub
//        return false;
//    }
//    /* (non-Javadoc)
//     * @see java.lang.Object#clone()
//     */
//    @Override
//    public JorusImage<T> clone() {
//        // TODO Auto-generated method stub
//        return new JorusImage<T>(this);
//    }
//    
//    
//
//}
