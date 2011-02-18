package ibis.pyxis.t;

import java.util.Collection;



public abstract class Task<Type> extends Node<Type> {
	
	private final Collection<Node<Type>> sources;
	private final Node<Type> sink;
	
	protected Task(Collection<Node<Type>> sources, Node<Type> sink) {
		super(Opcode.COMPOSITE, getData(sources));
		this.sources = sources;
		this.sink = sink;
	}


	private static <Type> Node<Type>[] getData(Collection<Node<Type>> sources) {
		for(Node<Type> node: sources) {
			node.getParents();
		}
		return null;
	}	
}
