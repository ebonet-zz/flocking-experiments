package viewer;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public class FlockinGraphViewerFrame extends JFrame {

	private int mHeight,mWidth;
	private int [][] mCenters;
	private int [][] mGraph;
	private int mNumberOfNodes;
	private int mGoal,mStart;


	public FlockinGraphViewerFrame(int height,int width,
			int [][] graph,int [][] centers,
			int numberOfNodes,int goal,int start){

		mHeight = height;
		mWidth = width;
		mGraph = graph;
		mCenters = centers;
		mNumberOfNodes = numberOfNodes;
		mGoal = goal;
		mStart = start;

		initializeComponents();
	}

	private void initializeComponents() {
		this.setSize(mWidth, mHeight+30);
		this.setResizable(false);
		this.setTitle("Flocking Graph Viewer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void drawBoid(Graphics g, int from, int to, double position){
		
		int radius =14;
		
		double relativePosition = position/mGraph[from][to];
		
		double x0 = mCenters[from][1];
		double y0 = mCenters[from][0];
		
		double x1 = mCenters[to][1];
		double y1 = mCenters[to][0];
		
		double theta = -1*Math.atan((y1-y0)/(x1-x0)) - ((x0>x1)? Math.PI:0)-((y0==y1)?Math.PI/2:0)+((x0==x1)?Math.PI/2.0:0);
		
		int xCenter = (int)(x0+relativePosition*(x1-x0));
		int yCenter = (int)(30+y0+relativePosition*(y1-y0));
		
		int [] xP ={-radius/2,0,radius/2,0};
		int [] yP = {-radius/2,0,-radius/2,radius/2};

		for(int i=0;i<4;i++){
			int oldX = xP[i];
			xP[i] = (int)(oldX*Math.cos(theta)-yP[i]*Math.sin(theta));
			yP[i] = (int)(oldX*Math.sin(theta)+yP[i]*Math.cos(theta));
			
			xP[i] = xP[i]+xCenter;
			yP[i] = yP[i]+yCenter;
		}
		
		g.setColor(Color.green);
		g.fillPolygon(xP,yP, 4);
		g.setColor(Color.black);
		g.drawPolygon(xP,yP, 4);
	}
	
	private void drawNodes(Graphics g){
		for(int i=0;i<mNumberOfNodes;i++){

			int x = mCenters[i][1];
			int y = mCenters[i][0]+30;

			if(i==mStart) drawStartNode(g,x,y);
			else if(i==mGoal) drawGoalNode(g,x,y);
			else drawCommonNode(g,x,y);


			g.setColor(Color.black);
			g.drawString(i+1+"", x-5,y+5);
		}

	}

	private void drawPaths(Graphics g){
		for (int i=0;i<mNumberOfNodes-1;i++){
			for(int j=i+1;j<mNumberOfNodes;j++){
				g.setColor(Color.black);
				g.drawLine(mCenters[i][1], mCenters[i][0]+30, mCenters[j][1], mCenters[j][0]+30);

			}
		}
	}

	private void drawStartNode(Graphics g,int x,int y){
		int radius =30;


		int [] xP ={x-radius/2,x+radius/2,x};
		int [] yP = {y-radius/2,y-radius/2,y+radius/2};

		g.setColor(Color.white);
		g.fillPolygon(xP,yP, 3);

		g.setColor(Color.black);
		g.drawPolygon(xP,yP, 3);
	}

	private void drawGoalNode(Graphics g,int x,int y){

		int radius =30;

		g.setColor(Color.white);
		g.fillRect(x-radius/2, y-radius/2, radius, radius);
		g.setColor(Color.black);
		g.drawRect(x-radius/2, y-radius/2, radius, radius);
	}

	private void drawCommonNode(Graphics g,int x,int y){

		int radius =30;

		g.setColor(Color.white);
		g.fillOval(x-radius/2, y-radius/2, radius, radius);
		g.setColor(Color.black);
		g.drawOval(x-radius/2, y-radius/2, radius, radius);

	}


	
	
	public void paint(Graphics g){
		super.paint(g);

		drawPaths(g);
		drawNodes(g);
		
		drawBoid(g,0,2,1);
		
	}







}
