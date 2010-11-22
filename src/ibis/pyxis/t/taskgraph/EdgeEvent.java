/**
 * 
 */
package ibis.pyxis.t.taskgraph;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;

/**
 * @author timo
 *
 */
public class EdgeEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = 5857934697791760910L;

    /**
     * @param source
     * @param target
     * @param data
     */
    public EdgeEvent(ActivityIdentifier source, ActivityIdentifier target,
            Object data) {
        super(source, target, data);
        // TODO Auto-generated constructor stub
    }

}
