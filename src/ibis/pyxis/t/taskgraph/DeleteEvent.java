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
public class DeleteEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -4678008169116526705L;

    /**
     * @param source
     * @param target
     * @param data
     */
    public DeleteEvent(ActivityIdentifier source, ActivityIdentifier target, Integer totalChildren) {
        super(source, target, totalChildren);
    }

}
