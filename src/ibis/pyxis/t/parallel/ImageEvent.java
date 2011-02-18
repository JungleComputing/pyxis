/**
 * 
 */
package ibis.pyxis.t.parallel;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;
import ibis.pyxis.t.ImageData;

/**
 * @author timo
 *
 */
public class ImageEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = 5857934697791760910L;

    /**
     * @param source
     * @param target
     * @param data
     */
    public ImageEvent(ActivityIdentifier source, ActivityIdentifier target,
            ImageData data) {
        super(source, target, data);
    }
    
    public ImageData getImage() {
    	return (ImageData)data;
    }
    
    @Override
	public String toString() {
		return "ImageEvent" + super.toString();
	}

}
