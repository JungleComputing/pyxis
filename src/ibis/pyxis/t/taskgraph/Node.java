package ibis.pyxis.t.taskgraph;

import ibis.constellation.Activity;
import ibis.constellation.ActivityContext;
import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Event;
import ibis.constellation.context.UnitActivityContext;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Node<E> extends Activity {

    private static final Logger logger = LoggerFactory.getLogger(Node.class);

    /**
     * 
     */
    private static final long serialVersionUID = -3526946285465796936L;

    private ActivityIdentifier[] parents;
    private Object[] payload;
    private int receivedPayloads;
    private E myPayload;

    private boolean finished;
    private int totalChildren;
    private int servicedChildren;

    private HashSet<ActivityIdentifier> children;
    
    protected Node(ActivityContext context, ActivityIdentifier... parents) {
        super(context, false, true);
        this.parents = parents;
        children = new HashSet<ActivityIdentifier>();
        if (parents == null || parents.length == 0) {
            payload = null;
        } else {
            payload = new Object[parents.length];
        }
        receivedPayloads = 0;
        myPayload = null;

        finished = false;
        totalChildren = 0;
        servicedChildren = 0;
    }

    @Override
    public void initialize() throws Exception {
        // System.out.println("initialize..");
        if (parents == null || parents.length == 0) {
            process();
        } else {
            ActivityIdentifier myID = identifier();

            HashSet<ActivityIdentifier> hs = new HashSet<ActivityIdentifier>(
                    parents.length);
            for (ActivityIdentifier ai : parents) {
                hs.add(ai);
            }
            for (ActivityIdentifier ai : hs) {
                executor.send(new ConnectEvent(myID, ai));
            }
        }
        suspend();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void process(Event e) throws Exception {
        // System.out.println("event received");
        if (e instanceof EdgeEvent) {
            if (logger.isDebugEnabled()) {
                logger.debug("EdgeEvent received, data = " + e.data.toString());
            }
            E data = (E) e.data;
            for (int i = 0; i < parents.length; i++) {
                if (e.source.equals(parents[i])) {
                    if (payload[i] != null) {
                        // already got this event, so we quit
                        return;
                    }
                    payload[i] = data;
                    receivedPayloads++;
                    processEvent(e.source, i, data);
                }
            }
            if (receivedPayloads == parents.length) {
                process();
            }
            endAction();
            return;
        } else if (e instanceof ConnectEvent) {
            if (logger.isDebugEnabled()) {
                logger.debug("ConnectEvent received from "
                        + e.source.toString());
            }
            if (myPayload == null) {
                children.add(e.source);
            } else {
                executor.send(new EdgeEvent(e.target, e.source, myPayload));
                servicedChildren++;
            }
            endAction();
            return;
        } else if (e instanceof DeleteEvent) {
//            System.err.println("DeleteEvent received at " + identifier());
            if (logger.isDebugEnabled()) {
                logger.debug("DeleteEvent received at " + identifier());
            }
            totalChildren = (Integer)(e.data);
            finished = true;
            endAction();
            return;
        }
    }

    /**
     * This function can be implemented by subclasses when some action needs to
     * be taken when a new event arrives. Default implements does nothing.
     * 
     * @param source
     *            The origin of the event
     * @param index
     *            The index of the Event
     * @param payload
     *            the contents of the event
     */
    protected void processEvent(ActivityIdentifier source, int index, E payload) {

    }

    @Override
    public void cleanup() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancel() throws Exception {
        // TODO Auto-generated method stub

    }

    private void endAction() {
        if (finished && totalChildren == servicedChildren) {
            finish();
        } else {
            suspend();
        }
    }

    private void process() {
        myPayload = calculate(payload);
        payload = null;
        for (ActivityIdentifier child : children) {
            executor.send(new EdgeEvent(identifier(), child, myPayload));
        }
        servicedChildren += children.size();
        children.clear();
    }

    protected abstract E calculate(Object[] parentData);

}
