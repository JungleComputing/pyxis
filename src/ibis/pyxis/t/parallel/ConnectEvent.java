/**
 * 
 */
package ibis.pyxis.t.parallel;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;

/**
 * @author timo
 *
 */
public class ConnectEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -6696583006128591190L;

    /**
     * @param source
     * @param target
     */
    public ConnectEvent(ActivityIdentifier source, ActivityIdentifier target) {
        super(source, target, null);
    }

}
