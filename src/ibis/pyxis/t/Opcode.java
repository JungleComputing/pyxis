package ibis.pyxis.t;

//public enum Opcode {
public interface Opcode {
	public static final int COMPOSITE = -1;
	public static final int NOOP = 0;
	
	public static final int BPO_ADD  = 1;
	public static final int BPO_SUB = 2;
	public static final int BPO_MUL = 3;
	public static final int BPO_DIV = 4;
	public static final int BPO_MIN = 5;
	public static final int BPO_MAX = 6;
	public static final int BPO_NEGDIV = 7;
	public static final int BPO_POSDIV = 8;
	public static final int BPO_ABSDIV = 9;
	
	public static final int CONVOLUTION = 10;
	public static final int CONVOLUTION_1D = 11;
	public static final int CONVOLUTION_ROTATED_1D = 12;
	public static final int CONV_GAUSS = 13;
	public static final int CONV_GAUSS_ANISOTROPIC = 14;
	public static final int CONV_GAUSS_1X2D = 15;
	
	
	public static final int GEOMETRIC_ROTATE = 16;
	public static final int GEOMETRIC_ROI_RESIZE = 17;
	
	public static final int LOAD = 18;	// Import
	
	public static final int NPO_MAX = 19;
	public static final int NPO_MIN = 20;
	public static final int NPO_ADD = 21;
        public static final int NPO_MUL = 22;
	
	public static final int REDUCE_SUM = 23;
	public static final int REDUCE_PRODUCT = 24;
	public static final int REDUCE_MAX = 25;
	public static final int REDUCE_MIN = 26;
	public static final int REDUCE_SUP = 27;
	public static final int REDUCE_INF = 28;
	
	public static final int SETBORDER_CONSTANT = 29;
	public static final int SETBORDER_MIRROR = 30;
	
	public static final int SVO_ADD = 31;
	public static final int SVO_SET = 32;

	// TODO Upo's are wrong??
	public static final int UPO_SET_IMAGE = 33; 
	public static final int UPO_SET_PIXEL = 34;
}
