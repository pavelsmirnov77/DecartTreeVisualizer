package com.example.decarttree;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.control.ScrollPane;

public class Controller {
    public Label textMessage;
    @FXML
    Canvas canvas = new Canvas();
    @FXML
    ScrollPane scrollPane = new ScrollPane();


    public void btnStartClicked(ActionEvent actionEvent) {
        DecartTree tree = new DecartTree();
        for (int i = 0; i < 10; i++) {
            tree.insert((int) (Math.random() * 10 + 10), (int) (Math.random() * 10 + 10));
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        scrollPane.setContent(canvas);
        scrollPane.setMinWidth(1169);
        scrollPane.setMinHeight(609);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawTree(gc, tree.getRoot(), canvas.getWidth()/2, 40, 25, 15, 120, 0);
    }

    private void drawTree(GraphicsContext gc, Node node, double x, double y, double radius, double angle, double spacing, int depth) {
        if (node != null) {
            gc.setFill(Color.WHITE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);
            gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            gc.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
            gc.setFill(Color.BLACK);
            gc.fillText(Integer.toString(node.getKey()), x - 14, y + 5);
            gc.fillText(Integer.toString(node.getValue()), x + 4, y + 5);

            // увеличение расстояния между узлами и угла линии в зависимости от глубины дерева
            double pow = Math.pow(2, -Math.floor(depth / 4) - 1);
            double childSpacing = 20 + spacing * pow;
            double childAngle = angle * pow;

            if (node.getLeft() != null) {
                double childX = x - childSpacing - (400 * 1/(depth+1));
                double childY = 10 + y + spacing + radius - 100/(depth + 1);
                gc.strokeLine(x, y + radius, childX, childY - radius);

                // рекурсивный вызов с новым значением угла и глубины
                drawTree(gc, node.getLeft(), childX, childY, radius, childAngle, childSpacing, depth + 1);
            }
            if (node.getRight() != null) {
                double childX = x + childSpacing + 400 * 1/(depth+1);
                double childY = 10 + y + spacing + radius - 100/(depth + 1);
                gc.strokeLine(x, y + radius, childX, childY - radius);

                // рекурсивный вызов с новым значением угла и глубины
                drawTree(gc, node.getRight(), childX, childY, radius, childAngle, childSpacing, depth + 1);
            }
        }
        textMessage.setText("Декартово дерево построено");
    }

    public void btnInsert(ActionEvent actionEvent) {
    }

    public void findBtn(ActionEvent actionEvent) {
    }

    public void btnDelete(ActionEvent actionEvent) {
    }
}