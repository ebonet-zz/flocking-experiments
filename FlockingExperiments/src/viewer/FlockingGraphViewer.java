package viewer;

import graph.FlockingGraph;

import java.util.List;

/**
 * 
 * @author ewertbe
 * 
 *         The class {@link FlockingGraphViewer} handles the display of the graph. it does not handle graphics itself,
 *         but has control over the frame.
 * 
 */
public class FlockingGraphViewer {

	private int[][] mGraph;
	private int mGoal;
	private int mStart;
	private int mHeight;
	private int mWidth;
	private int[][] mCenters;
	private int mNumberOfNodes;

	private FlockinGraphViewerFrame mFrame;

	/**
	 * Creates an example of viewer for a 4 node fully connected graph. Window has size (600,600).
	 */
	public FlockingGraphViewer() {

		this.mHeight = 600;
		this.mWidth = 600;

		createGraph1();
		computeCenters();

		this.mFrame = new FlockinGraphViewerFrame(this.mHeight, this.mWidth, this.mGraph, this.mCenters,
				this.mNumberOfNodes, this.mGoal, this.mStart);
	}

	/**
	 * Creates a window to display a graph. The window has a default bounding box of (600,600)
	 * 
	 * @param graph
	 *            - the distance matrix
	 * @param start
	 *            - the start node (0 indexed)
	 * @param goal
	 *            - the goal node (0 indexed)
	 * @param numberOfNodes
	 *            - the number of nodes
	 */
	public FlockingGraphViewer(int[][] graph, int start, int goal, int numberOfNodes) {
		this.mHeight = 600;
		this.mWidth = 600;

		this.mGraph = graph;
		this.mStart = start;
		this.mGoal = goal;
		this.mNumberOfNodes = numberOfNodes;

		computeCenters();
		this.mFrame = new FlockinGraphViewerFrame(this.mHeight, this.mWidth, this.mGraph, this.mCenters,
				this.mNumberOfNodes, this.mGoal, this.mStart);
	}

	/**
	 * Creates a viewer for an object of the class Flocking Graph, with default (600,600) window size.
	 * 
	 * @param graph
	 *            - The graph to be displayed
	 */
	public FlockingGraphViewer(FlockingGraph graph) {

		this.mHeight = 600;
		this.mWidth = 600;
		this.mGraph = graph.distanceMatrix;
		this.mNumberOfNodes = graph.getNumberOfNodes();
		this.mGoal = -1;
		this.mStart = -1;
		computeCenters();

		this.mFrame = new FlockinGraphViewerFrame(this.mHeight, this.mWidth, this.mGraph, this.mCenters,
				this.mNumberOfNodes, this.mGoal, this.mStart);
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

	private void computeCenters() {
		this.mCenters = new int[this.mNumberOfNodes][2];
		int radius = this.mHeight / 2 - 30;

		double ratio = 2 * Math.PI / this.mNumberOfNodes;

		for (int i = 0; i < this.mNumberOfNodes; i++) {

			this.mCenters[i][0] = (int) (this.mHeight / 2 - radius * Math.cos(ratio * i));
			this.mCenters[i][1] = (int) (this.mWidth / 2 + radius * Math.sin(ratio * i));
		}
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

	private void createGraph1() {
		this.mGraph = new int[4][4];

		this.mGraph[0][0] = 0;
		this.mGraph[0][1] = 3;
		this.mGraph[0][2] = 5;
		this.mGraph[0][3] = 8;

		this.mGraph[1][0] = 3;
		this.mGraph[1][1] = 0;
		this.mGraph[1][2] = 1;
		this.mGraph[1][3] = 4;

		this.mGraph[2][0] = 5;
		this.mGraph[2][1] = 1;
		this.mGraph[2][2] = 0;
		this.mGraph[2][3] = 9;

		this.mGraph[3][0] = 8;
		this.mGraph[3][1] = 4;
		this.mGraph[3][2] = 9;
		this.mGraph[3][3] = 0;

		this.mStart = 1;
		this.mGoal = 3;

		this.mNumberOfNodes = 4;
	}

	public static void main(String[] args) {
		FlockingGraphViewer f = new FlockingGraphViewer();
		f.openViewer();
	}

}
