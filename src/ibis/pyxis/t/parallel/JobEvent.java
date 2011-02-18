/**
 * 
 */
package ibis.pyxis.t.parallel;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;
import ibis.pyxis.t.parallel.activities.OperationActivity;

/**
 * @author Timo van Kessel
 *
 */
public class JobEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = 5857934697791760910L;

    /**
     * @param source
     * @param target
     * @param data
     */
    public JobEvent(ActivityIdentifier source, ActivityIdentifier target,
    		OperationActivity<?> job) {
        super(source, target, job);
    }

}
