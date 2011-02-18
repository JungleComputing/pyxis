/**
 * 
 */
package ibis.pyxis.t.parallel;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;
import ibis.pyxis.t.nodes.OperationIdentifier;

/**
 * @author Timo van Kessel
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
    public DeleteEvent(ActivityIdentifier source, ActivityIdentifier target, OperationIdentifier nodeID, int children) {
        super(source, target, new DeleteRequest(nodeID, children));
    }
    
    public DeleteRequest getDeleteRequest() {
    	return (DeleteRequest)data;
    }

}
