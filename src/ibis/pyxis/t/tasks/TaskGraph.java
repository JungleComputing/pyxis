package ibis.pyxis.t.tasks;

import java.util.HashSet;


public class TaskGraph {
	private Task sink;
	private HashSet<Task> sources;
	
	protected TaskGraph(Task sink,	HashSet<Task> sources) {
		this.sink = sink;
		this.sources = sources;
	}

	public Task getSink() {
		return sink;
	}
	
	protected void makeTree() {
		sink.makeTree(null);
//		sink.generateBFSRanks();
		sink.generateDFSRanks(0);
	}

//	protected HashSet<Task> getSources() {
//		return sources;
//	}

	protected HashSet<Task> removeSources() {
		HashSet<Task> result = sources;
		sources = null; 
		return result;
	}
	
	
//	public void addParent(TaskGraph parent) {
//		parents.add(parent);
//	}
//	
//	public void addParents(Collection<TaskGraph> parents) {
//		parents.addAll(parents);
//	}
//	
//	public void addChild(TaskGraph child) {
//		children.add(child);
//	}
//	
//	public void addChildren(Collection<TaskGraph> children) {
//		children.addAll(children);
//	}
//	
//	public void sendTask(PyxisT pyxis, ActivityIdentifier target, TaskActivity taskActivity) {
//		started = true;
//		pyxis.getConstellation().send(new TaskEvent(target, taskActivity));
//	}
//	
}
