package ibis.pyxis;

import ibis.pyxis.t.system.PyxisT;

public class PyxisFactory {
	public static Pyxis createPyxis(boolean dataparallel, boolean taskparallel)
			throws Exception {
		if (dataparallel) {
			if (taskparallel) {
				// TODO init Pyxis-DT
				// pyxis = new PyxisDT();

			} else {
				// TODO init Pyxis-D
				// pyxis = new PyxisD();
			}
		} else if (taskparallel) {
			return new PyxisT();
			// init Pyxis-T
		} else {
			// sequential
			// TODO init Pyxis
		}

		return null;
	}
}
