package org.percepta.mgrankvi.floorplanner.gwt.client.item;

import org.percepta.mgrankvi.floorplanner.gwt.client.VisualItem;
import org.percepta.mgrankvi.floorplanner.gwt.client.geometry.Point;

import com.google.gwt.canvas.dom.client.Context2d;

public class CLabel extends VisualItem {

	private int width = 0;

	public CLabel(final String name) {
		this(name, new Point(0, 0));
	}

	public CLabel(final String name, final Point position) {
		setName(name);
		setPosition(position);
		width = name.length() * 6;
		this.position.move(-(width / 2), 0);
	}

	public void setPosition(final Point position) {
		this.position = position;
	}

	@Override
	public void paint(final Context2d context) {
		context.setFillStyle("GREEN");
		context.beginPath();

		context.arc(position.getX(), position.getY() + 10, 10, 0, Math.PI * 2.0, true);
		context.fillRect(position.getX(), position.getY(), width, 20);
		context.arc(position.getX() + width, position.getY() + 10, 10, 0, Math.PI * 2.0, true);

		context.closePath();
		context.fill();

		context.setFillStyle("WHITE");
		context.beginPath();

		context.setFont("bold 10px Courier New");
		context.fillText(getName(), position.getX(), position.getY() + 12);
		context.closePath();
	}

	@Override
	public boolean pointInObject(final int x, final int y) {
		// TODO Auto-generated method stub
		return false;
	}

}