package org.percepta.mgrankvi.floorplanner.gwt.client.room;

import java.util.LinkedList;
import java.util.List;

import org.percepta.mgrankvi.floorplanner.gwt.client.VisualItem;
import org.percepta.mgrankvi.floorplanner.gwt.client.geometry.GeometryUtil;
import org.percepta.mgrankvi.floorplanner.gwt.client.geometry.Line;
import org.percepta.mgrankvi.floorplanner.gwt.client.geometry.Point;
import org.percepta.mgrankvi.floorplanner.gwt.client.item.CLabel;
import org.percepta.mgrankvi.floorplanner.gwt.client.item.stairs.CStair;
import org.percepta.mgrankvi.floorplanner.gwt.client.paint.ItemUtils;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.VConsole;

public class CRoom extends VisualItem {

    private boolean selected = false;
    private final List<VisualItem> roomItems = new LinkedList<VisualItem>();
    private CLabel roomLabel;
    private String fillColor = "PEACHPUFF";

    public CRoom() {
        // dummy element
        setElement(Document.get().createDivElement());
    }

    public CRoom(final String id, final List<Point> points, final Point position) {
        this.points.addAll(points);
        this.position = position;
        this.id = id;
    }

    @Override
    public void setName(final String name) {
        super.setName(name);
        if (!name.isEmpty()) {
            roomLabel = new CLabel(name, getCenter());
        }
    }

    @Override
    public void setPoints(final List<Point> points) {
        this.points.addAll(points);
    }

    public void removeRoomItem(final VisualItem item) {
        roomItems.remove(item);
    }

    public List<VisualItem> getRoomItems() {
        return new LinkedList<VisualItem>(roomItems);
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public void setPosition(final Point position) {
        this.position = position;
        VConsole.log("Start position: " + position.toString());
    }

    @Override
    public void movePosition(final int x, final int y) {
        super.movePosition(x, y);
    }

    @Override
    public void scale(final double scale) {
        super.scale(scale);
        for (final Point p : points) {
            p.setX((int) Math.ceil(p.getX() * scale));
            p.setY((int) Math.ceil(p.getY() * scale));
        }
        position.setX((int) Math.ceil(position.getX() * scale));
        position.setY((int) Math.ceil(position.getY() * scale));

        for (final VisualItem item : roomItems) {
            item.scale(scale);
            final Point position = item.getPosition();
            position.setX((int) Math.ceil(position.getX() * scale));
            position.setY((int) Math.ceil(position.getY() * scale));
            item.setPosition(position);
            for (final Point p : item.getPoints()) {
                p.setX((int) Math.ceil(p.getX() * scale));
                p.setY((int) Math.ceil(p.getY() * scale));
            }
            item.pointMoved();
        }
        if (roomLabel != null) {
            roomLabel.scale(scale);
            final Point position = roomLabel.getPosition();
            position.setX((int) Math.ceil(position.getX() * scale));
            position.setY((int) Math.ceil(position.getY() * scale));
            roomLabel.setPosition(position);
        }
    }

    @Override
    public void reset() {
        super.reset();
        for (final VisualItem item : roomItems) {
            item.reset();
        }
        if (roomLabel != null) {
            roomLabel.reset();
        }
    }

    public void setSelection(final boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void paintLabel(final Context2d context) {
        if (roomLabel != null) {
            roomLabel.paint(context, position);
        }
    }

    @Override
    public void paint(final Context2d context) {
        final int width = context.getCanvas().getWidth();
        final int height = context.getCanvas().getHeight();
        paint(context, position);
    }

    @Override
    public void paint(final Context2d context, final Point offset) {
        if (selected) {
            ItemUtils.paintPointToPoint(context, points, offset, CssColor.make("GREEN"));
            ItemUtils.paintPointSelections(context, points, offset, CssColor.make("PURPLE"));
            context.setStrokeStyle(CssColor.make("GREEN"));
        } else {
            ItemUtils.paintPointToPoint(context, points, offset, CssColor.make("BLACK"));
        }
        if (fillColor != null) {
            context.save();
            context.setGlobalAlpha(0.2);
            ItemUtils.fillPointToPoint(context, points, offset, CssColor.make(fillColor));
            context.restore();
        }
        for (final VisualItem item : roomItems) {
            item.paint(context, offset);
        }
    }

    @Override
    public boolean pointInObject(final double x, final double y) {
        if (x < (position.getX() + minX()) || x > (position.getX() + maxX()) || y < (position.getY() + minY()) || y > (position.getY() + maxY())) {
            return false;
        }

        final Point target = new Point(x, y);
        // First point clearly outside shape.
        final Line targetLine = new Line(new Point(position.getX() + minX() - 50, position.getY() + minY() - 50), target);
        int intercepts = 0;
        for (int i = 0; i + 2 <= points.size(); i++) {
            final Line line = new Line(combine(position, points.get(i)), combine(position, points.get(i + 1)));
            if ((line.start.getX() > x && line.end.getX() > x) || (line.start.getY() > y && line.end.getY() > y)) {
                continue;
            }
            if (lineSegmentsIntersect(line, targetLine)) {
                intercepts++;
            }
        }
        final Line line = new Line(combine(position, points.getFirst()), combine(position, points.getLast()));
        if (lineSegmentsIntersect(line, targetLine)) {
            intercepts++;
        }

        for (final VisualItem item : roomItems) {
            if (item.pointInObject(x, y)) {
                item.setHovering(true);
            } else {
                item.setHovering(false);
            }
        }
        return intercepts % 2 == 1;
    }

    @Override
    public void clicked(final double x, final double y) {
        if (pointInObject(x, y)) {
            for (final VisualItem item : roomItems) {
                item.clicked(x, y);
            }
        }

    }

    public Point selectedPoint(final double x, final double y) {
        for (final Point point : points) {
            final double pointXMin = position.getX() + point.getX() - 4;
            final double pointYMin = position.getY() + point.getY() - 4;
            if (x > pointXMin && x < pointXMin + 8 && y > pointYMin && y < pointYMin + 8) {
                return point;
            }
        }
        return null;
    }

    private Point combine(final Point p1, final Point p2) {
        return GeometryUtil.combine(p1, p2);
    }

    public CRoom cloneRoom() {
        final List<Point> pointClone = new LinkedList<Point>();
        copyPoints(points, pointClone);
        final CRoom clone = new CRoom(id, pointClone, new Point(position.getX(), position.getY()));
        // for (final VisualItem item : roomItems) {
        // clone.addRoomItem(item);
        // }
        return clone;
    }

    public void add(final Widget widget) {
        if (widget instanceof VisualItem) {
            roomItems.add((VisualItem) widget);
        }
        if (widget instanceof CStair) {
            ((CStair) widget).setMoveYourself(false);
        }
    }

    public void setColor(final String color) {
        fillColor = color;
    }
}
