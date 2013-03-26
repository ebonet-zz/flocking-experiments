package viewer;

import java.util.List;

import graph.TraditionalGraph;

public class GraphViewer {

	protected TraditionalGraph mGraph;
	protected int [][] mDistances;
	protected int mHeight, mWidth;
	protected int mGoal;
	protected int mStart;
	protected int[][] mCenters;
	protected int mNumberOfNodes;
	
	protected GraphViewerFrame mFrame;
	
	
	public GraphViewer(){
		
		mDistances = createGraph1();
		mGraph = new TraditionalGraph(mDistances);
		mStart = 0;
		mHeight = 600;
		mWidth = 600;
		mNumberOfNodes = mDistances[0].length;
		mGoal = mNumberOfNodes-1;
		mFrame = null;
	}
	
	public GraphViewer(TraditionalGraph graph){
		
		mGraph = graph;
		mDistances = graph.getDistanceMatrix();
		mStart = 0;
		mHeight = 600;
		mWidth = 600;
		mNumberOfNodes = mDistances[0].length;
		mGoal = mNumberOfNodes-1;
		mFrame = null;
	}
	


	/**
	 * Opens the frame and display the graph
	 */
	public void openViewer() {
		this.mFrame.setVisible(true);
	}

	/**
	 * Closes the frame
	 */
	public void closeViewer() {
		this.mFrame.setVisible(false);
	}

	/**
	 * Updates the position of the moving object, displaying the new ones in the viewer.
	 * 
	 * @param movingObjects
	 *            - List of graph.Positions
	 */
	public void updateViewer(List<MovingObject> movingObjects) {

		this.mFrame.update(movingObjects);
	}
	
	/**
	 * Sets a new size for the window
	 * 
	 * @param width
	 * @param height
	 */
	public void setWindowSize(int width, int height) {
		this.mWidth = width;
		this.mHeight = height;

		computeCenters();

		this.mFrame.setWindowSize(this.mWidth, this.mHeight, this.mCenters);
	}
	
	protected void computeCenters(){
		
	}
	
	/**
	 * Creates a sample distance matrix
	 * 
	 * @return
	 * 		A new distance matrix
	 */


	private int [][] createGraph1() {
		
		int [][] newGraph = new int[4][4]; 

		newGraph[0][0] = 0;
		newGraph[0][1] = 3;
		newGraph[0][2] = 5;
		newGraph[0][3] = 8;

		newGraph[1][0] = 3;
		newGraph[1][1] = 0;
		newGraph[1][2] = 1;
		newGraph[1][3] = 4;

		newGraph[2][0] = 5;
		newGraph[2][1] = 1;
		newGraph[2][2] = 0;
		newGraph[2][3] = 9;

		newGraph[3][0] = 8;
		newGraph[3][1] = 4;
		newGraph[3][2] = 9;
		newGraph[3][3] = 0;

		return newGraph;
	}
	
	
	
	
	
}
