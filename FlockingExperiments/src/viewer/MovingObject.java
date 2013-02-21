package viewer;

import graph.Position;

import java.awt.Color;

public class MovingObject {

	private Color mColor;
	private Position mPosition;

	public MovingObject(Position position, Color color) {
		this.mColor = color;
		this.mPosition = position.clone();
	}

	public MovingObject(MovingObject otherObject) {
		this.mColor = otherObject.getColor();
		this.mPosition = otherObject.getPosition();
	}

	public Color getColor() {
		return this.mColor;
	}

	public void setColor(Color color) {
		this.mColor = color;
	}

	public Position getPosition() {
		return this.mPosition;
	}

	public void setmPosition(Position position) {
		this.mPosition = position.clone();
	}

}
