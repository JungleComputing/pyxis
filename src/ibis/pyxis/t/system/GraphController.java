package ibis.pyxis.t.system;

import ibis.constellation.Constellation;
import ibis.pyxis.t.ImageT;
import ibis.pyxis.t.Node;

import java.lang.ref.ReferenceQueue;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Graph Controller manages the node graph and all supporting objects, like
 * the {@link ImageReference}s, and provides access to the current
 * {@link GraphParser} and {@link JorusController}.
 * 
 * @author Timo van Kessel
 */
class GraphController implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(GraphController.class);

	private static GraphController graphController = null;

	ReferenceQueue<ImageT<?, ?>> queue;
	private final HashSet<ImageReference> refs;
	private final Constellation constellation;

	private final Thread thread;

	private volatile boolean done;

	protected static GraphController init(Constellation constellation)
			throws Exception {
		// if (graphController == null) {
		// final String JorusPoolname = System.getProperty("jorus.pool.name");
		// final String JorusPoolSize = System.getProperty("jorus.pool.size");
		//
		// if (JorusPoolname == null || JorusPoolSize == null
		// || Integer.parseInt(JorusPoolSize) <= 0) {
		// if(logger.isDebugEnabled()) {
		// logger.debug("Sequential Jorus");
		// }
		// graphController = new GraphController(new GraphParser(),
		// new JorusController());
		// } else {
		// if(logger.isDebugEnabled()) {
		// logger.debug("Parallel Jorus");
		// }
		// graphController = new GraphController(new GraphParser(),
		// new JorusController(JorusPoolname, JorusPoolSize));
		// }
		// }
		graphController = new GraphController(constellation);
		return graphController;
	}

	private GraphController(Constellation constellation) {
		done = false;
		this.constellation = constellation;
		refs = new HashSet<ImageReference>();
		queue = new ReferenceQueue<ImageT<?, ?>>();
		thread = new Thread(this, "GraphController Thread");
		thread.setDaemon(true);
		thread.start();
		if (logger.isInfoEnabled()) {
			logger.info("GraphController started!");
		}
	}

	@Override
	public void run() {
		if (logger.isInfoEnabled()) {
			logger.info("GraphController Thread started!");
		}
		while (!done) {
			doClean();

			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				// ignore
			}
		}
	}

	/**
	 * Initiates garbage collector and removes obsolete {@link Node} objects
	 * from the task graph
	 */
	public void clean() {
		System.gc();
		doClean();
	}

	/**
	 * Initiates garbage collector and removes obsolete {@link Node} objects
	 * from the task graph
	 */
	private synchronized void doClean() {
		if (logger.isDebugEnabled()) {
			logger.debug("cleaning started");
		}

		ImageReference n = (ImageReference) queue.poll();

		while (n != null) {
			n.destroy();
			n = (ImageReference) queue.poll();
		}
	}

	void addReference(final ImageReference ref) {
		refs.add(ref);
	}

	void removeReference(final ImageReference ref) {
		refs.remove(ref);
	}

	/**
	 * @return the current {@link GraphController}
	 */
	public static GraphController getController() {
		if (graphController == null) {
			throw new RuntimeException("GraphController not initialized");
		}
		return graphController;
	}

	public Constellation getConstellation() {
		return constellation;
	}

	public void done() {
		done = true;
	}
}
