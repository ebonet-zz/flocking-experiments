package viewer;

import graph.FlockingGraph;
import graph.Position;

import java.util.List;

/**
 * 
 * @author ewertbe
 *
 * The class {@link FlockingGraphViewer} handles the display of the graph. it does not 
 * handle graphics itself, but has control over the frame.
 *
 */
public class FlockingGraphViewer{

	
	private int [][]  mGraph;
	private int mGoal;
	private int mStart;
	private int mHeight;
	private int mWidth;
	private int [][] mCenters;
	private int mNumberOfNodes;
	
	private FlockinGraphViewerFrame mFrame;
	
	/**
	 * Creates an example of viewer for a 4 node fully connected graph. Window has size (600,600).
	 */
	public FlockingGraphViewer(){
		
		mHeight = 600;
		mWidth = 600;
		
		createGraph1();
		computeCenters();
		
		mFrame = new FlockinGraphViewerFrame(mHeight, mWidth, mGraph, mCenters, mNumberOfNodes, mGoal, mStart);
		openViewer();
	}
	
	/**
	 * Creates a window to display a graph. The window has a default bounding box of (600,600)
	 * 
	 * @param graph - the distance matrix
	 * @param start - the start node (0 indexed)
	 * @param goal - the goal node (0 indexed)
	 * @param numberOfNodes - the number of nodes
	 */
	public FlockingGraphViewer(int [][] graph, int start, int goal, int numberOfNodes){
		mHeight = 600;
		mWidth = 600;
		
		mGraph = graph;
		mStart = start;
		mGoal = goal;
		mNumberOfNodes = numberOfNodes;		
	}
	
	/**
	 * Creates a viewer for an object of the class Flocking Graph, with default (600,600) window size.
	 * 
	 * @param graph - The graph to be displayed
	 */
	public FlockingGraphViewer(FlockingGraph graph){
		
		mHeight = 600;
		mWidth = 600;
		mGraph = graph.distanceMatrix;
		mNumberOfNodes = graph.getNumberOfNodes();
		mGoal = -1;
		mStart = -1;
	}
	
	/**
	 * Sets a new size for the window
	 * 
	 * @param width
	 * @param height
	 */
	public void setWindowSize(int width, int height){
		mWidth = width;
		mHeight = height;
		
		computeCenters();
		
		mFrame.setWindowSize(mWidth,mHeight,mCenters);			
	}
	
	private void computeCenters() {
		mCenters = new int[mNumberOfNodes][2];
		int radius=mHeight/2-30;
		
		double ratio = 2*Math.PI/mNumberOfNodes;
		
		for(int i=0;i<mNumberOfNodes;i++){
			
			mCenters[i][0] = (int)(mHeight/2-radius*Math.cos(ratio*i));
 			mCenters[i][1] = (int)(mWidth/2 +radius*Math.sin(ratio*i));
		}
	}
	
	/**
	 * Opens the frame and display the graph
	 */
	public void openViewer(){
		mFrame.setVisible(true);
	}
	
	/**
	 * Closes the frame
	 */
	public void closeViewer(){
		mFrame.setVisible(false);
	}
	
	/**
	 * Updates the position of the moving object, displaying the new ones in the viewer.
	 * 
	 * @param movingObjects - List of graph.Positions 
	 */
	public void updateViewer(List<Position> movingObjects){
		
		
		mFrame.update(movingObjects);
	}
	
	private void createGraph1(){
		mGraph = new int[4][4];
		
		mGraph[0][0]=0;
		mGraph[0][1]=3;
		mGraph[0][2]=5;
		mGraph[0][3]=8;
		
		mGraph[1][0]=3;
		mGraph[1][1]=0;
		mGraph[1][2]=1;
		mGraph[1][3]=4;
		
		mGraph[2][0]=5;
		mGraph[2][1]=1;
		mGraph[2][2]=0;
		mGraph[2][3]=9;
		
		mGraph[3][0]=8;
		mGraph[3][1]=4;
		mGraph[3][2]=9;
		mGraph[3][3]=0;	
		
		mStart=1;
		mGoal = 3;
		
		mNumberOfNodes = 4;
	}
	
	
	public static void main(String [] args){
		FlockingGraphViewer f = new FlockingGraphViewer();
	}
	
}
