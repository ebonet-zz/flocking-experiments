package viewer;

import graph.FlockingGraph;
import graph.TraditionalGraph;


/**
 * 
 * @author ewertbe
 * 
 *         The class {@link FlockingGraphViewer} handles the display of the graph. it does not handle graphics itself,
 *         but has control over the frame.
 * 
 */
public class FlockingGraphViewer extends GraphViewer{

	/**
	 * Creates an example of viewer for a 4 node fully connected graph. Window has size (600,600).
	 */
	public FlockingGraphViewer() {

		super();
		computeCenters();

		this.mFrame = new GraphViewerFrame(this.mHeight, this.mWidth, this.mDistances, this.mCenters,
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
	public FlockingGraphViewer(int[][] graph, int start, int goal) {

		super(new TraditionalGraph(graph));
		this.mStart = start;
		this.mGoal = goal;

		computeCenters();
		
		this.mFrame = new GraphViewerFrame(this.mHeight, this.mWidth, this.mDistances, this.mCenters,
				this.mNumberOfNodes, this.mGoal, this.mStart);
	}

	/**
	 * Creates a viewer for an object of the class Flocking Graph, with default (600,600) window size.
	 * 
	 * @param graph
	 *            - The graph to be displayed
	 */
	public FlockingGraphViewer(FlockingGraph graph) {
		super(graph);
		
		this.mGoal = -1;
		this.mStart = -1;
		computeCenters();

		this.mFrame = new GraphViewerFrame(this.mHeight, this.mWidth, this.mDistances, this.mCenters,
				this.mNumberOfNodes, this.mGoal, this.mStart);
	}

	/**
	 * Computes the centers with the nodes forming a circle.
	 */
	protected void computeCenters() {
		this.mCenters = new int[this.mNumberOfNodes][2];
		int radius = this.mHeight / 2 - 30;

		double ratio = 2 * Math.PI / this.mNumberOfNodes;

		for (int i = 0; i < this.mNumberOfNodes; i++) {

			this.mCenters[i][0] = (int) (this.mHeight / 2 - radius * Math.cos(ratio * i));
			this.mCenters[i][1] = (int) (this.mWidth / 2 + radius * Math.sin(ratio * i));
		}
	}

	public static void main(String[] args) {
		FlockingGraphViewer f = new FlockingGraphViewer();
		f.openViewer();
	}

}
