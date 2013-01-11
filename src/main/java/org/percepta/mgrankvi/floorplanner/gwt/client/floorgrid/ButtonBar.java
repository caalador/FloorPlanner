package org.percepta.mgrankvi.floorplanner.gwt.client.floorgrid;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vaadin.client.VConsole;

public class ButtonBar implements ClickHandler {

	private final int START_BUBBLE_SIZE = 20;
	private final int BAR_HEIGHT = 300;
	private final int BAR_WIDTH = 15;

	boolean visible = false;
	boolean animate = false;

	int x = 0;
	int y = 0;
	int cornerX;
	int cornerY;
	CFloorGrid grid;

	public ButtonBar(final CFloorGrid grid) {
		this.grid = grid;

		grid.addDomHandler(this, ClickEvent.getType());
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setAnimate(final boolean animate) {
		this.animate = animate;
	}

	public void paint(final Context2d context) {
		x = context.getCanvas().getWidth();
		y = context.getCanvas().getHeight();
		cornerX = context.getCanvas().getWidth();
		cornerY = context.getCanvas().getHeight();
		if (visible) {
			if (animate) {
				grid.setAnimating(true);
				animate = false;

				final Animation elongate = new Animation() {

					@Override
					protected void onUpdate(final double progress) {
						final double offset = progress * 15;
						final double height = progress * BAR_HEIGHT;
						context.save();

						context.setFillStyle("LAVENDER");
						context.beginPath();

						context.arc(x - offset, y - height, START_BUBBLE_SIZE, Math.PI, Math.PI * 1.5, false);

						context.closePath();
						context.fill();

						context.beginPath();

						context.moveTo(x - offset, y - height - START_BUBBLE_SIZE);
						context.lineTo(x - offset, y - height);
						context.lineTo(x - offset - START_BUBBLE_SIZE, y - height);

						context.fillRect(x - offset, y - START_BUBBLE_SIZE - height, offset, START_BUBBLE_SIZE);
						context.fillRect(x - offset - START_BUBBLE_SIZE, y - height, START_BUBBLE_SIZE + offset, height);

						context.closePath();
						context.fill();

						context.restore();
					}

					@Override
					protected void onComplete() {
						super.onComplete();
						grid.setAnimating(false);
					}
				};
				elongate.run(500);
			} else {
				context.save();
				context.setFillStyle("LAVENDER");
				context.beginPath();

				context.arc(x - 15, y - BAR_HEIGHT, START_BUBBLE_SIZE, Math.PI, Math.PI * 1.5, false);

				context.closePath();
				context.fill();

				context.beginPath();

				context.moveTo(x - 15, y - BAR_HEIGHT - START_BUBBLE_SIZE);
				context.lineTo(x - 15, y - BAR_HEIGHT);
				context.lineTo(x - 15 - START_BUBBLE_SIZE, y - BAR_HEIGHT);

				context.fillRect(x - 15, y - START_BUBBLE_SIZE - BAR_HEIGHT, 15, START_BUBBLE_SIZE);
				context.fillRect(x - 15 - START_BUBBLE_SIZE, y - BAR_HEIGHT, START_BUBBLE_SIZE + 15, BAR_HEIGHT);

				context.closePath();
				context.fill();

				context.restore();
			}
		} else if (animate) {
			grid.setAnimating(true);
			animate = false;

			final Animation animator = new Animation() {

				@Override
				protected void onUpdate(final double progress) {
					context.clearRect(x - 40, y - 325 - START_BUBBLE_SIZE, 40, 325);
					final double offset = progress * BAR_WIDTH;
					final double height = progress * BAR_HEIGHT;
					context.save();

					context.setFillStyle("LAVENDER");
					context.beginPath();

					context.arc(x - BAR_WIDTH + offset, y - BAR_HEIGHT + height, START_BUBBLE_SIZE, Math.PI, Math.PI * 1.5, false);

					context.closePath();
					context.fill();

					context.beginPath();

					context.moveTo(x - BAR_WIDTH + offset, y - BAR_HEIGHT + height - START_BUBBLE_SIZE);
					context.lineTo(x - BAR_WIDTH + offset, y - BAR_HEIGHT + height);
					context.lineTo(x - BAR_WIDTH + offset - START_BUBBLE_SIZE, y - BAR_HEIGHT + height);

					context.fillRect(x - BAR_WIDTH + offset, y - START_BUBBLE_SIZE - BAR_HEIGHT + height, BAR_WIDTH - offset, START_BUBBLE_SIZE);
					context.fillRect(x - 35 + offset, y - BAR_HEIGHT + height, START_BUBBLE_SIZE + BAR_WIDTH - offset, BAR_HEIGHT - height);
					// context.fillRect(x + offset, y - START_BUBBLE_SIZE +
					// height, offset, START_BUBBLE_SIZE);
					// context.fillRect(x + offset - START_BUBBLE_SIZE, y +
					// height, START_BUBBLE_SIZE - offset, height);

					context.closePath();
					context.fill();

					context.restore();
				}

				@Override
				protected void onComplete() {
					super.onComplete();
					grid.setAnimating(false);
				}
			};
			animator.run(2000);
		} else {
			context.save();
			context.setFillStyle("LAVENDER");
			context.beginPath();

			context.arc(x, y, START_BUBBLE_SIZE, Math.PI, Math.PI * 1.5, false);

			context.closePath();
			context.fill();

			context.beginPath();

			context.moveTo(x, y - START_BUBBLE_SIZE);
			context.lineTo(x, y);
			context.lineTo(x - START_BUBBLE_SIZE, y);

			context.closePath();
			context.fill();

			context.restore();
		}
	}

	public boolean mouseOver(final int clientX, final int clientY) {
		VConsole.log("Visible: " + visible + "clientx: " + clientX + " clienty: " + clientY + " min x: " + (cornerX - START_BUBBLE_SIZE - BAR_WIDTH)
				+ " min y: " + (cornerY - BAR_HEIGHT - START_BUBBLE_SIZE));
		if (visible && clientX > (cornerX - START_BUBBLE_SIZE - BAR_WIDTH) && clientY > (cornerY - BAR_HEIGHT - START_BUBBLE_SIZE)) {
			return true;
		} else if (clientX > (cornerX - START_BUBBLE_SIZE) && clientY > (cornerY - START_BUBBLE_SIZE)) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(final ClickEvent event) {
		// TODO Auto-generated method stub

	}
}
