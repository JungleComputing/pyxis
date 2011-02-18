package ibis.pyxis.t.system;

import ibis.constellation.Constellation;
import ibis.constellation.ConstellationFactory;
import ibis.constellation.Executor;
import ibis.constellation.SingleEventCollector;
import ibis.constellation.StealPool;
import ibis.imaging4j.Format;
import ibis.pyxis.Pyxis;
import ibis.pyxis.t.ImageData;
import ibis.pyxis.t.ImageT;
import ibis.pyxis.t.nodes.ImportNode;
import ibis.pyxis.t.nodes.Node;
import ibis.pyxis.t.parallel.ImageEvent;
import ibis.pyxis.t.parallel.ImageRequest;
import ibis.pyxis.t.tasks.Task;
import ibis.pyxis.t.tasks.TaskActivity;
import ibis.pyxis.t.tasks.TaskGraphGenerator;
import ibis.pyxis.t.tasks.TaskTracker;

import java.io.IOException;
import java.nio.ByteBuffer;

import jorus.array.Array2dFloat;
import jorus.array.Array2dScalarFloat;
import jorus.array.Array2dVecFloat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TODO  better javadoc
/**
 * Starting point for the programming model. Need some better info here
 * 
 * @author Timo van Kessel
 */
public class PyxisT extends Pyxis {
    private static final Logger logger = LoggerFactory.getLogger(PyxisT.class);
    
    public static final StealPool TASK_POOL = new StealPool("TASK_POOL");
    public static final StealPool TRACKER_POOL = new StealPool("TRACKER_POOL");

    private final GraphController controller;
    private final TaskGraphGenerator tgGenerator;
    private final TaskTracker taskTracker;
    
    /**
	 * @return the controller
	 */
	public GraphController getController() {
		return controller;
	}

	private final Constellation constellation;

    public PyxisT() throws Exception {
        tgGenerator = new TaskGraphGenerator(this);
        taskTracker = new TaskTracker();
        constellation = initConstellation();
        controller = GraphController.createController(constellation);
    }

    private Constellation initConstellation() {
        Constellation constellation = null;
        int n_executors = Integer.getInteger("pyxis.t.executors", java.lang.Runtime.getRuntime().availableProcessors());
        if(n_executors < 1) {
        	throw new Error("No suitable number of executors defined");
        }
        
        
        boolean deployTracker = Boolean.getBoolean("pyxis.t.deployTaskTracker");
        Executor[] executors;
        if(deployTracker) {
        	// add taskTracker
	        executors = new Executor[n_executors +1];
	        executors[n_executors] = taskTracker;
        } else {
        	// no taskTracker
        	executors = new Executor[n_executors];	        
        }
        for (int i = 0; i < n_executors; i++) {
            executors[i] = new PyxisTExecutor();
            
        }
        
        try {
            constellation = ConstellationFactory.createConstellation(executors);
        } catch (Exception e1) {
            e1.printStackTrace();
            System.exit(1);
        }
        
        if(logger.isDebugEnabled()) {
            logger.debug("activating Constellation, " + n_executors + " executors");
        }
        constellation.activate();
       	
        return constellation;
    }
    
    public Constellation getConstellation() {
        return constellation;
    }
    
    @Override
    public ImageT importFloatImage(ibis.imaging4j.Image image)
            throws IOException {
        final Format format = image.getFormat();
        int extent;

        switch (format) {
        case PYXIS_FLOATARGB:
            extent = 4;
            break;
        case PYXIS_FLOATGREY:
            extent = 1;
            break;
        default:
            throw new IOException("Image format not supported");
        }

        final int width = image.getWidth();
        final int height = image.getHeight();

        final float[] array = new float[width * height * extent];
        final ByteBuffer buffer = image.getData();
        buffer.rewind();
        buffer.asFloatBuffer().get(array);

        Array2dFloat<?> array2d;
        if (extent == 1) {
            array2d = new Array2dScalarFloat(width, height, array, true);
        } else {
            array2d = new Array2dVecFloat(width, height, extent, array, true);
        }
        ImportNode node = new ImportNode(array2d);
        return new ImageT(node, width, height, extent, this);
    }
    
    @Override
    public ImageT createImage(int width, int height, int extent,
            float[] data) {
        Array2dFloat<?> array;
        if (extent == 1) {
            array = new Array2dScalarFloat(width, height, data, true);
        } else {
            array = new Array2dVecFloat(width, height, extent, data, true);
        }
        ImportNode node = new ImportNode(array);
        return new ImageT(node, width, height, extent, this);
    }
    
    @Override
    public void end() {
        constellation.done();
        controller.done();
    }

    public void gc() {
        controller.clean();
    }

    @Override
    public boolean isMaster() {
        return constellation.isMaster();
    }
    
	public ImageData getImageFromTask(Node node) {
		
		//Add node representing the sec
		node.addOut(null);
		
		Task task = node.getTask();
		if(task == null) {
			task = tgGenerator.createAndRunTaskGraph(node);
		}
		
		TaskActivity taskActivity = task.getActivity();
		if(taskActivity == null) {
			//TODO PANIC!!!!!!!!!
		}
		SingleEventCollector sec = new SingleEventCollector(PyxisTContext.createDefaultContext(0));
		constellation.submit(sec);
		
		constellation.send(new ImageRequest(sec.identifier(), taskActivity.identifier(), node.identifier().getOperationID()));
		ImageEvent e = (ImageEvent) sec.waitForEvent();
		return e.getImage();
	}
	
}
