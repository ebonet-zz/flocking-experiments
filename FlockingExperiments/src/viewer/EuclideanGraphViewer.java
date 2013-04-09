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

		for(int i = 0;i<mNumberOfNodes;i++){
			for(int j = 0;j<mNumberOfNodes;j++){
				System.out.print(mDistances[i][j]+" ");
				
			}
		}
		
		this.mFrame = new GraphViewerFrame(this.mHeight, this.mWidth, this.mDistances, this.mCenters,
				this.mNumberOfNodes, this.mGoal, this.mStart);
	}

	/**
	 * Computes the centers of the nodes according to their euclidean positions.
	 */
	protected void computeCenters() {
		mCenters = new int[this.mNumberOfNodes][2];
		double [][] coords = ((EuclideanGraph) mGraph).getAllCitiesCoords();
		double maxX=-1,minX=Double.MAX_VALUE,maxY=-1,minY=Double.MAX_VALUE;
		
		for(int i = 0; i < mNumberOfNodes ; i++){
			if(coords[i][0] > maxX) maxX = coords[i][0];
			else if(coords[i][0] < minX) minX = coords[i][0];
			
			if(coords[i][1] > maxY) maxY = coords[i][1];
			else if(coords[i][1] < minY) minY = coords[i][1];
			
		}
		
		for(int i=0; i<mNumberOfNodes; i++) {
			mCenters[i][0] = (int) ((coords[i][0]-minX)*this.mWidth/(maxX-minX));
			mCenters[i][1] = (int) ((coords[i][1]-minY)*this.mHeight/(maxY-minY));
		}
	}

	public static void main(String [] args) {
		
		EuclideanGraph world = new EuclideanGraph(4);
		world.addCity(0, 30, 30);
		world.addCity(1, 10, 80);
		world.addCity(2, 80, 10);
		world.addCity(3, 0, 0);
		EuclideanGraphViewer f = new EuclideanGraphViewer(world);
		f.openViewer();
	}

}
