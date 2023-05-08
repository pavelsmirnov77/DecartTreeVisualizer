package com.example.decarttree;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.control.ScrollPane;

public class Controller {
    static DecartTree tree = new DecartTree();
    public Label textMessage;
    public TextField keyTextField;
    public TextField priorityTextField;
    public Spinner counterElements;

    int countInsert = 0;
    @FXML
    Canvas canvas = new Canvas();
    @FXML
    ScrollPane scrollPane = new ScrollPane(canvas);
    private double scale = 1.0;

    public void initialize() {
        scrollPane.setContent(canvas);

        canvas.setOnScroll(event -> {
            event.consume();

            if (event.isControlDown()) {
                double delta = event.getDeltaY();
                if (delta < 0) {
                    scale -= 0.1;
                } else {
                    scale += 0.1;
                }
                scale = Math.min(Math.max(0.1, scale), 10.0);
                canvas.setScaleX(scale);
                canvas.setScaleY(scale);
            }
        });
    }

    public void btnStartClicked(ActionEvent actionEvent) {
        countInsert = 0;
        tree = new DecartTree();
        if ((int)counterElements.getValue() == 0) {
            textMessage.setText("Количество элементов для построения должно быть больше 0!");
        } else {
            for (int i = 0; i < (int)counterElements.getValue(); i++) {
                tree.insert((int)(Math.random() * 10 + 10), (int)(Math.random() * 10 + 10));
            }

            countInsert += (int)counterElements.getValue();

            GraphicsContext gc = canvas.getGraphicsContext2D();
            scrollPane.setContent(canvas);
            scrollPane.setMinWidth(1169);
            scrollPane.setMinHeight(609);
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawTree(gc, tree.getRoot(), canvas.getWidth() / 2, 40, 25, 15, 120, 0);
            textMessage.setText(String.format("Построено дерево, состоящее из %d элемента(ов).", (int)counterElements.getValue()));
        }
    }

    private void drawTree(GraphicsContext gc, Node node, double x, double y, double radius, double angle, double spacing, int depth) {
        if (node != null) {
            gc.setFill(Color.WHITE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);
            gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            gc.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
            gc.setFill(Color.BLACK);
            gc.fillText("val: " + node.getKey(), x - 14, y - 2);
            gc.fillText("\npr: " + node.getPriority(), x - 14, y + 2);

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
        textMessage.setText("Декартово дерево построено.");
    }
    public void btnInsert(ActionEvent actionEvent) {
        if (countInsert >= 15 ){
            textMessage.setText("Вставлено максимальное количество элементов!");
        }
        else {
            String keyText = keyTextField.getText();
            String priorityText = priorityTextField.getText();
            if (keyText.isEmpty() || priorityText.isEmpty()) {
                textMessage.setText("Поля ключа или значения не могут быть пустыми!");
            }
            else {
                int key = Integer.parseInt(keyText);
                int priority = Integer.parseInt(priorityText);
                tree.insert(key, priority);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                scrollPane.setContent(canvas);
                scrollPane.setMinWidth(1169);
                scrollPane.setMinHeight(609);
                drawTree(gc, tree.getRoot(), canvas.getWidth() / 2, 40, 25, 15, 120, 0);
                textMessage.setText(String.format("Значение с ключом %d и значением %d вставлено успешно.", key, priority));
                countInsert++;
            }
        }
    }

    public void findBtn(ActionEvent actionEvent) {
        String keyText = keyTextField.getText();
        String priorityText = priorityTextField.getText();
        if (keyText.isEmpty() || priorityText.isEmpty()) {
            textMessage.setText("Поле ключа или значения не может быть пустым!");
        }
        else {
            int key = Integer.parseInt(keyText);
            int priority = Integer.parseInt(priorityText);
            Node found = tree.find(key, priority);
            if (found == null) {
                textMessage.setText(String.format("Элемент с ключом %d и значением %d не найден.", key, priority));
            } else {
                textMessage.setText(String.format("Найден элемент с ключом %d и значением %d.", found.getKey(), found.getPriority()));
            }
        }
    }

    public void btnDelete(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        String keyText = keyTextField.getText();
        String priorityText = priorityTextField.getText();

        if (keyText.isEmpty() || priorityText.isEmpty()) {
            textMessage.setText("Поле ключа или значения не может быть пустым!");
        }
        else {
            int key = Integer.parseInt(keyText);
            int priority = Integer.parseInt(priorityText);
            Node found = tree.find(key, priority);
            if (found == null) {
                textMessage.setText(String.format("Элемент с ключом %d и значением %d для удаления не найден!", key, priority));
            }
            else {
                tree.delete(key, priority);
                countInsert--;
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                drawTree(gc, tree.getRoot(), canvas.getWidth()/2, 40, 25, 15, 120, 0);
                textMessage.setText(String.format("Элемент с ключом %d и значением %d удален.", key, priority));
            }
        }
    }

    public void btnDeleteAll(ActionEvent actionEvent) {
        if (countInsert == 0) {
            textMessage.setText("Элементы дерева отсутствуют!");
        }
        else {
            tree.deleteAll();
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            textMessage.setText("Все элементы дерева удалены.");
            countInsert = 0;
        }
    }

    public void btnBackStepClicked(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        tree.undo();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawTree(gc, tree.getRoot(), canvas.getWidth()/2, 40, 25, 15, 120, 0);
        textMessage.setText("Выполнен шаг назад.");
    }

    public void btnNextStepClicked(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        tree.redo();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawTree(gc, tree.getRoot(), canvas.getWidth()/2, 40, 25, 15, 120, 0);
        textMessage.setText("Выполнен шаг вперед.");
    }
}