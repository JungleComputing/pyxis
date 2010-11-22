package ibis.pyxis;

import java.io.Serializable;

import jorus.array.Array2d;


public class ImageData<T> implements Serializable, Cloneable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1634228011716448036L;

    private Array2d<T,?> array;
    
    public ImageData(Array2d<T,?> array) {
        this.array = array;
    }
    
    public int getExtent() {
        return array.getExtent();
    }

    public int getWidth() {
        return array.getWidth();
    }

    public int getHeight() {
        return array.getHeight();
    }

    public boolean equalDimensions(ImageData<?> other) {
        return (getHeight() == other.getHeight() && getWidth() == other.getWidth() && getExtent() == other.getExtent());
    }

    public boolean equalDataType(ImageData<?> other) {
        return getClass().getTypeParameters()[0].equals(other.getClass().getTypeParameters()[0]);
    }

    public Array2d<T, ?> getArray() {
        return array;
    }

    public boolean inplace() {
        return false;
    }

    public ImageData<T> clone() {
        return new ImageData<T>(array.clone());
    }
    
//    public  int getExtent();
//
//    public  int getWidth();
//
//    public  int getHeight();
//
//    public  boolean equalDimensions(ImageData<?> other);
//
//    public  boolean equalDataType(ImageData<?> other);
//
//    public  ImageData<T> clone();
//    
//    public  Array2d<T,?> getArray();
//    
//    public  boolean inplace();;
}
