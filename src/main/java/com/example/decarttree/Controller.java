package com.example.decarttree;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Controller {
    @FXML
    Canvas canvas = new Canvas();

    public void btnStartClicked(ActionEvent actionEvent) {
        DecartTree tree = new DecartTree();
        for (int i = 0; i < 10; i++) {
            tree.insert((int) (Math.random() * 10 + 10), (int) (Math.random() * 10 + 10));
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawTree(gc, tree.getRoot(), canvas.getWidth()/2, 25, 25, 20, 100, 0);
    }

    private void drawTree(GraphicsContext gc, Node node, double x, double y, double radius, double angle, double spacing, int depth) {
        if (node != null) {
            gc.setFill(Color.WHITE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);
            gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            gc.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
            gc.setFill(Color.BLACK);
            gc.fillText(Integer.toString(node.getKey()), x - 5, y + 5);
            gc.fillText(Integer.toString(node.getValue()), x, y + 15);
            double childSpacing = spacing * Math.pow(2, -Math.floor(depth/4) - 1);
            if (node.getLeft() != null) {
                double childAngle = -90;
                double childX = x - childSpacing;
                double childY = y + spacing + radius;
                gc.strokeLine(x, y + radius, childX, childY - radius);
                drawTree(gc, node.getLeft(), childX, childY, radius, childAngle, spacing, depth+1);
            }
            if (node.getRight() != null) {
                double childAngle = -90;
                double childX = x + childSpacing;
                double childY = y + spacing + radius;
                gc.strokeLine(x, y + radius, childX, childY - radius);
                drawTree(gc, node.getRight(), childX, childY, radius, childAngle, spacing, depth+1);
            }
        }
    }
}