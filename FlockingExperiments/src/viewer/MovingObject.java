package viewer;

import graph.Position;

import java.awt.Color;

public class MovingObject {

	private Color mColor;
	private Position mPosition;
	
	public MovingObject(Position position,Color color){
		mColor = color;
		mPosition = position.clone();
	}
	
	public MovingObject(MovingObject otherObject){
		mColor = otherObject.getColor();
		mPosition = otherObject.getPosition();
	}

	public Color getColor() {
		return mColor;
	}

	public void setColor(Color color) {
		this.mColor = color;
	}

	public Position getPosition() {
		return mPosition;
	}

	public void setmPosition(Position position) {
		this.mPosition = position.clone();
	}
	
	
}
