package application;

/*

    Hypocycloid.java
    Written by Ben Hadlington
    - Calculates points of hypocycloid to be drawn.

    Parametric Formulae:
        x = (R-r)*cos(t) + O*cos(((R-r)/r)*t)
        y = (R-r)*sin(t) - O*sin(((R-r)/r)*t)

    R: radius of the fixed circle.
    r: radius of the moving circle.
    O: offset of pen point.

*/

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/*

    Class

*/

public class Hypocycloid {
    private static final int maxT = 550;
    private final double R, r, O;
    private ArrayList<int[]> points = new ArrayList<>();
    private final GraphicsContext g;

    /* Constructor */
    public Hypocycloid(GraphicsContext g, int midpoint, double R, double r, double O) {
        this.g = g;
        this.R = R;
        this.r = r;
        this.O = O;
        this.calculatePoints(midpoint);
        this.drawShape();
    }

    /* Calculates a list of points to connect */
    private void calculatePoints(int midpoint) {
        /* Upscale the x and y coordinates for larger canvas resolutions */
        double upscaleFactor = midpoint / 300;
        for (float t = 0; t < maxT; t++) {
            int x = (int) Math.floor(((R - r) * Math.cos(t) + O * Math.cos(((R - r) / r) * t)) * upscaleFactor);
            int y = (int) Math.floor(((R - r) * Math.sin(t) - O * Math.sin(((R - r) / r) * t)) * upscaleFactor);
            x+= midpoint;
            y+= midpoint;

            points.add(new int[]{x, y});
        }
    }

    /* Loops through all points, storing the previous one */
    private void drawShape() {
        int[] lastPoint = null;
        while (!points.isEmpty()) {
            if (lastPoint == null) {
                lastPoint = points.remove(0);
            }
            int[] nextPoint = points.remove(0);
            draw(lastPoint, nextPoint);
            lastPoint = nextPoint;
        }
    }

    /* Connects a pair of x,y coordinates */
    private void draw(int[] point1, int[] point2) {
        Platform.runLater(() -> {
            g.strokeLine(point1[0], point1[1], point2[0], point2[1]);
        });
    }
}