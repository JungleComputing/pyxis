package ibis.pyxis.t;

import ibis.pyxis.t.nodes.OperationIdentifier;

import java.io.Serializable;

import jorus.array.Array2d;


public class ImageData implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1634228011716448036L;

    private volatile Array2d<float[],?> array;
    
    private boolean inplace;
    private transient boolean localImage;
    
    private final OperationIdentifier operationIdentifier;
    
    public ImageData(Array2d<float[],?> array, OperationIdentifier operationID) {
        this.array = array;
        inplace = false;
        this.operationIdentifier = operationID;
        localImage = true; 
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

    public boolean equalDimensions(ImageData other) {
        return (getHeight() == other.getHeight() && getWidth() == other.getWidth() && getExtent() == other.getExtent());
    }

//    public boolean equalDataType(ImageData other) {
//        return getClass().getTypeParameters()[0].equals(other.getClass().getTypeParameters()[0]);
//    }

    public Array2d<float[], ?> getArray() {
        return array;
    }

    public boolean inplace() {
        return inplace;
    }
    
    public void allowInplace(boolean inplace) {
    	this.inplace = inplace;
    }

	public OperationIdentifier getOperationId() {
		return operationIdentifier;
	}
    
	public boolean isLocalImage() {
		return localImage;
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
//    public  boolean inplace();
}
