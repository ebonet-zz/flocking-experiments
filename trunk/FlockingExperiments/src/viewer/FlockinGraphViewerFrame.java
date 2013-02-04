package viewer;

import graph.Position;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class FlockinGraphViewerFrame extends JFrame {

	FlockinGraphViewerPanel mPanel;
		
	/**
	 * Creates the frame that will display the graph.
	 * 
	 * @param height - height of the screen
	 * @param width - width of the screen
	 * @param graph - distance matrix
	 * @param centers - center of the nodes
	 * @param numberOfNodes - number of nodes in the graph
	 * @param goal - -1 if there's no goal
	 * @param start - -1 if there's no start
	 */
	public FlockinGraphViewerFrame(int height,int width,
			int [][] graph,int [][] centers,
			int numberOfNodes,int goal,int start){

		mPanel = new FlockinGraphViewerPanel(height, width, graph, centers, numberOfNodes, goal, start);

		initializeComponents(width,height);
	}

	/**
	 * Updates the current list of positions and repaints
	 * 
	 * @param positions - the update positions (graph.Positions)
	 */
	public void update(List<MovingObject> movingObjects) {
		mPanel.update(movingObjects);
	}

	/**
	 * Changes the window size. This method also requires the new centers of the nodes.
	 * 
	 * @param width
	 * @param height
	 * @param centers
	 */
	public void setWindowSize(int width, int height, int[][] centers) {
		
		this.setSize(width, height+30);
		mPanel.setWindowSize(width, height, centers);
	}
	
	
	private void initializeComponents(int width,int height) {
		this.setSize(width, height+30);
		
		mPanel.setBounds(0, 30, width, height);
		this.add(mPanel);
		
		
		
		this.setResizable(false);
		this.setTitle("Flocking Graph Viewer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}

