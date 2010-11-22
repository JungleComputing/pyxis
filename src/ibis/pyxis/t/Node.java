package ibis.pyxis.t;

import ibis.constellation.ActivityIdentifier;
import ibis.constellation.Constellation;
import ibis.constellation.SingleEventCollector;
import ibis.pyxis.ImageData;
import ibis.pyxis.Pyxis;
import ibis.pyxis.t.system.PyxisTContext;
import ibis.pyxis.t.taskgraph.ConnectEvent;
import ibis.pyxis.t.taskgraph.DeleteEvent;
import ibis.pyxis.t.taskgraph.EdgeEvent;
import ibis.pyxis.t.taskgraph.nodes.OperationDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Node<Type> {

    private static final Logger logger = LoggerFactory.getLogger(Node.class);

    // private final Opcode opcode;
    private final int opcode;
    private ImageData<Type> image;
    private boolean garbage;

    ActivityIdentifier operation;

    private Node<Type>[] parents;

    private int children;
    private final PyxisTContext context;

    protected static PyxisTContext generateContext(Node<?>[] parents) {
        long rank = 0;
        if (parents != null) {
            for (Node<?> node : parents) {
                if (node.context.rank >= rank) {
                    rank = node.context.rank + 1;
                }
            }
        }
        return new PyxisTContext(rank);
    }

    protected Node(int opcode, Node<Type>... parents) {
        context = generateContext(parents);
        // internal = false;
        this.opcode = opcode;
        image = null;

        children = 0;
        this.parents = parents;
        for (Node<Type> parent : parents) {
            parent.addChild(this);
        }
        garbage = false;
        // nodeDescriptor = null;
    }

    protected Node(int opcode, ImageData<Type> data) {
        context = generateContext(null);
        // internal = false;
        this.opcode = opcode;
        image = data;
        children = 0;
        parents = null;
        garbage = false;
        // nodeDescriptor = null;
    }

    protected void addChild(Node<Type> node) {
        children++;
    }

    protected void removeChild(Node<?> child) {
        children--;
    }

    protected void addNodeToSource(Node<Type> source) {
        // FIXME stupid type hierarchy hack for NpoNode
        source.addChild(this);
    }

    /**
     * Constellation Activities are generated in here
     * 
     * @param constellation
     * @return
     */
    private ActivityIdentifier getOperation(Constellation constellation) {
        if (operation != null) {
            return operation;
        }
        // TODO give implementation the opportunity to override the parent setup
        parents = setParents(parents);

        OperationDescriptor<Type> descriptor;

        if (parents == null || parents.length == 0) {
            synchronized (this) {
                descriptor = createOperation(opcode);
                operation = constellation.submit(descriptor);
                if (garbage) {
                    constellation.send(new DeleteEvent(null, operation,
                            children));
                }
            }
        } else {
            ActivityIdentifier[] ids = new ActivityIdentifier[parents.length];
            for (int i = 0; i < parents.length; i++) {
                ids[i] = parents[i].getOperation(constellation);
            }
            parents = null;
            synchronized (this) {
                descriptor = createOperation(opcode, ids);
                operation = constellation.submit(descriptor);
                if (garbage) {
                    constellation.send(new DeleteEvent(null, operation,
                            children));
                }
            }
        }
        return operation;
    }

    /**
     * Default implementation does not change the parent set
     * 
     * @param parents
     * @return
     */
    protected Node<Type>[] setParents(Node<Type>[] parents) {
        return parents;
    }

    /**
     * Entry point of task graph execution. Add intellignece from here
     * 
     * @param constellation
     *            The Constellation that will calculate this graph
     * @return the resulting image
     */
    @SuppressWarnings("unchecked")
    private ImageData<Type> execute(Constellation constellation) {
        SingleEventCollector sec = new SingleEventCollector(new PyxisTContext(
                Long.MAX_VALUE));
        ActivityIdentifier secID = constellation.submit(sec);

        constellation
                .send(new ConnectEvent(secID, getOperation(constellation)));
        children++; // The SingleEventcollector is also a child :-)

        EdgeEvent result = (EdgeEvent) sec.waitForEvent();
        return (ImageData<Type>) result.data;
    }

    protected ImageData<Type> getImage() {
        if (image == null) {
            Pyxis pyxis = Pyxis.getPyxis();
            pyxis.gc();
            image = execute(pyxis.getConstellation());
        }
        return image;
    }

    /**
     * @return the context
     */
    protected final PyxisTContext getContext() {
        return context;
    }

    protected abstract OperationDescriptor<Type> createOperation(int opcode,
            ActivityIdentifier... parents);

    public final synchronized void referenceLost(Constellation constellation) {
        garbage = true;
        if (operation != null) {
            constellation.send(new DeleteEvent(null, operation, children));
        }
    }
}
