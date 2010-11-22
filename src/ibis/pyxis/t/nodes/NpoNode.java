package ibis.pyxis.t.nodes;

import ibis.constellation.ActivityIdentifier;
import ibis.pyxis.t.Node;
import ibis.pyxis.t.taskgraph.nodes.NpoDescriptor;
import ibis.pyxis.t.taskgraph.nodes.OperationDescriptor;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NpoNode<Type> extends Node<Type> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7327921696258168338L;

    private static final Logger logger = LoggerFactory.getLogger(NpoNode.class);

    private transient LinkedList<Node<Type>> sources;
    private boolean alreadyProcessed;

    public NpoNode(int opcode, Node<Type> source) {
        super(opcode);
        this.sources = new LinkedList<Node<Type>>();
        alreadyProcessed = false;
        addSource(source);
    }

    public boolean addSource(Node<Type> source) {
        if (alreadyProcessed) {
            logger.error("PANIC!!! already executed");
            return false;
        }
        sources.addLast(source);
        addNodeToSource(source);
        return true;
    }

    @Override
    protected OperationDescriptor<Type> createOperation(int opcode,
            ActivityIdentifier... parents) {
        return new NpoDescriptor<Type>(getContext(), opcode, parents);
    }

    /* (non-Javadoc)
     * @see pyxis.t.Node#setParents(pyxis.t.Node<T>[])
     */
    @Override
    protected Node<Type>[] setParents(Node<Type>[] parents) {
        Node<Type>[] array = (Node<Type>[]) new Node<?>[sources.size()];
        return sources.toArray(array);
    }
    
    
}
