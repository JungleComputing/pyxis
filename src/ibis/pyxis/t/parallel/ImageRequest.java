/**
 * 
 */
package ibis.pyxis.t.parallel;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;

/**
 * @author Timo van Kessel
 *
 */
public class ImageRequest extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -6696583006128591190L;

    /**
     * @param source
     * @param target
     */
    public ImageRequest(ActivityIdentifier source, ActivityIdentifier target, Long operationID) {
        super(source, target, operationID);
    }
    
    public Long getoperationID() {
    	return (Long) data;
    }
    
    @Override
	public String toString() {
		return "ImageRequest" + super.toString();
	}

}
