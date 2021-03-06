package org.percepta.mgrankvi.floorplanner.gwt.client.geometry;

import java.io.Serializable;

public class Link implements Serializable {

    private static final long serialVersionUID = -1563363612591205240L;

    protected Node target;
    protected int weight;

    public Link() {

    }

    public Link(final Node target, final int weight) {
        this.target = target;
        this.weight = weight;
    }

    public Node getTarget() {
        return target;
    }

    public int getWeight() {
        return weight;
    }

}
