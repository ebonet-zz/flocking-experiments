package viewer;

import javax.swing.JFrame;

public class FlockingGraphViewer{

	// FlockingGraph mGraph;
	// JFrame mFrame;
	
	private int [][]  mGraph;
	private int mGoal;
	private int mStart;
	private int mHeight=600;
	private int mWidth=600;
	private int [][] mCenters;
	private int mNumberOfNodes;
	
	private FlockinGraphViewerFrame mFrame;
	
	public FlockingGraphViewer(){
		// TODO: change to receive a flocking graph as parameter
		createGraph1();
		computeCenters();
		
		mFrame = new FlockinGraphViewerFrame(mHeight, mWidth, mGraph, mCenters, mNumberOfNodes, mGoal, mStart);
		openViewer();
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

	public void openViewer(){
		mFrame.setVisible(true);
	}
	
	public void closeViewer(){
		mFrame.setVisible(false);
	}
	
	public void updateViewer(){
		mFrame.repaint();
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
