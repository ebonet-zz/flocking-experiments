package viewer;


import graph.EuclideanGraph;
import graph.TraditionalGraph;


/**
 * 
 * @author ewertbe
 * 
 *         The class {@link FlockingGraphViewer} handles the display of the graph. it does not handle graphics itself,
 *         but has control over the frame.
 * 
 */
public class EuclideanGraphViewer extends GraphViewer{

	/**
	 * Creates an example of viewer for a 4 node fully connected graph. Window has size (600,600).
	 */
	public EuclideanGraphViewer() {

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
	public EuclideanGraphViewer(int[][] graph, int start, int goal) {

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
	public EuclideanGraphViewer(EuclideanGraph graph) {
		super(graph);
		
		this.mGoal = -1;
		this.mStart = -1;
		computeCenters();

		this.mFrame = new GraphViewerFrame(this.mHeight, this.mWidth, this.mDistances, this.mCenters,
				this.mNumberOfNodes, this.mGoal, this.mStart);
	}

	/**
	 * Computes the centers of the nodes according to their euclidean positions.
	 */
	protected void computeCenters() {
		double [][] coords = ((EuclideanGraph) mGraph).getAllCitiesCoords();
		double maxX=-1,minX=Double.MAX_VALUE,maxY=-1,minY=Double.MAX_VALUE;
		
		for(int i = 0; i < coords[0].length ; i++){
			if(coords[i][0] > maxX) maxX = coords[i][0];
			else if(coords[i][0] < minX) minX = coords[i][0];
			
			if(coords[i][1] > maxY) maxY = coords[i][1];
			else if(coords[i][1] < minY) minY = coords[i][1];
		}
		

		for (int i = 0; i < this.mNumberOfNodes; i++) {

			this.mCenters[i][0] = (int) ((coords[i][0]-minX)/(maxX-minX)*(this.mWidth-60)+30);
			this.mCenters[i][1] = (int) ((coords[i][1]-minY)/(maxY-minY)*(this.mHeight-60)+30);
		}
	}

	public static void main(String[] args) {
		FlockingGraphViewer f = new FlockingGraphViewer();
		f.openViewer();
	}

}
