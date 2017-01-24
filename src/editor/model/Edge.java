package editor.model;


import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Created by vilddjur on 1/24/17.
 */
public class Edge extends Line{
    private Circle startCircle;
    private Circle endCircle;

    /**
     * Takes a start circle and an end circle, draws a line between the center of the two.
     * @param startCircle
     * @param endCircle
     * @throws Exception
     */
    public Edge (Circle startCircle, Circle endCircle) throws Exception {
        this.setStartCircle(startCircle);
        if(endCircle != null){
            this.setEndCircle(endCircle);
        }
    }

    /**
     * Sets the startCircle to the given circle, throws nullpointer exception when given circle is null
     * @param startCircle
     * @throws Exception
     */
    public void setStartCircle(Circle startCircle) throws Exception {
        if(startCircle == null){
            throw new NullPointerException("Edge: start Circle cannot be null");
        }
        this.setStartX(startCircle.getCenterX());
        this.setStartY(startCircle.getCenterY());
        this.startCircle = startCircle;
    }

    public Circle getStartCircle() {
        return startCircle;
    }

    /**
     * Sets the endCircle to the given circle, throws nullpointer exception when given circle is null
     * @param endCircle
     * @throws Exception
     */
    public void setEndCircle(Circle endCircle) throws Exception {
        if(endCircle == null){
            throw new NullPointerException("Edge: end Circle cannot be null");
        }
        this.setEndX(endCircle.getCenterX());
        this.setEndY(endCircle.getCenterY());
        this.endCircle = endCircle;
    }

    public Circle getEndCircle() {
        return endCircle;
    }
}
