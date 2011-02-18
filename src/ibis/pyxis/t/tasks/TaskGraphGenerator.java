package ibis.pyxis.t.tasks;

import ibis.constellation.Constellation;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.system.PyxisT;

import java.util.HashSet;

public class TaskGraphGenerator {

	private final PyxisT pyxis;

	public TaskGraphGenerator(PyxisT pyxis) {
		this.pyxis = pyxis;
	}

	public Task createAndRunTaskGraph(Node node) {
		TaskGraph taskgraph = createTaskGraph(node);
		pyxis.gc();
		Task result = runTaskGraph(taskgraph);
		
		return result;
	}

	private Task runTaskGraph(TaskGraph taskgraph) {
		//FIXME implement
		
		Constellation c = pyxis.getConstellation();
		
		//first execute the sources, as they might need to be run on localhost
		HashSet<Task> sources = taskgraph.removeSources(); 
		for(Task task: sources) {
			runTask(c, task);
		}
		taskgraph.makeTree();
		
		Task sink = taskgraph.getSink(); 
		runTask(c, sink);
				
		return sink; 
	}

	private void runTask(Constellation constellation, Task task) {
		task.startActivity(pyxis.getConstellation());
	}

	
	public TaskGraph createTaskGraph(Node sinkNode) {
		HashSet<Task> sources = new HashSet<Task>();
		Task sink = createTasks(sinkNode, sources);
		return new TaskGraph(sink, sources); 		
	}

	/**
	 * creates a separate task for every node
	 * @param node
	 * @return
	 */
	private Task createTasks(Node node, HashSet<Task> sources) {
		Task task = node.getTask();
		if(task == null) {
			// create a new task for this Node
			task = new Task(node.getOperation());
			
			Node[] sourceNodes = node.getInputs();
			if(sourceNodes == null || sourceNodes.length == 0) {
				//this is a source Task: add to sources set
				sources.add(task);
			} else {
				// this is an intermediate task: create inputs and connect to them
				for(Node sourceNode: sourceNodes) {
					Task sourceTask = createTasks(sourceNode, sources);
					sourceTask.addOut(task);
					task.addIn(sourceTask);
				}
			}
			node.setTask(task);
		} else {
			// this Node already is in a Task
			
			// remove Node from NodeGraph
			node.setTask(task);
		}
		return task;
		
	}
}
