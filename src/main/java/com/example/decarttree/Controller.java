package com.example.decarttree;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.control.ScrollPane;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    static DecartTree tree = new DecartTree();
    public TextField keyTextField;
    public TextField priorityTextField;
    public Spinner counterElements;
    public TextField messageTextField;
    public List<Integer> keyList = new ArrayList<>();

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
        int key = 0;
        int priority = 0;
        countInsert = 0;
        tree = new DecartTree();
        if ((int)counterElements.getValue() == 0) {
            messageTextField.setText("Количество элементов для построения должно быть больше 0!");
            messageTextField.setStyle("-fx-text-fill: red;");
        } else {
            for (int i = 0; i < (int)counterElements.getValue(); i++) {
                key = (int)(Math.random() * 1000 + 10);
                priority = (int)(Math.random() * 10 + 10);
                tree.insert(key, priority);
                keyList.add(key);
            }

            countInsert += (int)counterElements.getValue();

            GraphicsContext gc = canvas.getGraphicsContext2D();
            scrollPane.setContent(canvas);
            scrollPane.setMinWidth(1169);
            scrollPane.setMinHeight(609);
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawTree(gc, tree.getRoot(), canvas.getWidth() / 2, 40, 25, 15, 140, 0);
            messageTextField.setText(String.format("Построено дерево, состоящее из %d элемента(ов).", (int)counterElements.getValue()));
            messageTextField.setStyle("-fx-text-fill: green;");
        }
    }

    private void drawTree(GraphicsContext gc, Node node, double x, double y, double radius, double angle, double spacing, int depth) {
        if (node != null) {
            if (node.color == Color.GREEN) {
                gc.setLineWidth(3);
                gc.setFill(Color.GREEN);
                gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
                gc.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
                gc.setFill(Color.WHITE);
            } else {
                gc.setLineWidth(3);
                gc.setFill(Color.WHITE);
                gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
                gc.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
                gc.setFill(Color.BLACK);
            }
            gc.setStroke(Color.BLACK);
            gc.fillText("k: " + node.getKey(), x - 14, y - 2);
            gc.fillText("\np: " + node.getPriority(), x - 14, y + 2);

            // увеличение расстояния между узлами и угла линии в зависимости от глубины дерева
            double pow = Math.pow(2, -Math.floor(depth / 4) - 1);
            double childSpacing = 20 + spacing * pow;
            double childAngle = angle * pow;

            if (node.getLeft() != null) {
                double childX = x - childSpacing - 400 * 1/(depth+1);
                double childY = 10 + y + spacing + radius - 50/(depth + 1);
                gc.strokeLine(x, y + radius, childX, childY - radius);

                // рекурсивный вызов с новым значением угла и глубины
                drawTree(gc, node.getLeft(), childX, childY, radius, childAngle, childSpacing, depth + 1);
            }
            if (node.getRight() != null) {
                double childX = x + childSpacing + 400 * 1/(depth+1);
                double childY = 10 + y + spacing + radius - 50/(depth + 1);
                gc.strokeLine(x, y + radius, childX, childY - radius);

                // рекурсивный вызов с новым значением угла и глубины
                drawTree(gc, node.getRight(), childX, childY, radius, childAngle, childSpacing, depth + 1);
            }
        }
    }

    public void btnInsert(ActionEvent actionEvent) {
        if (countInsert >= 15 ){
            messageTextField.setText("Вставлено максимальное количество элементов!");
            messageTextField.setStyle("-fx-text-fill: red;");
        }
        else {
            String keyText = keyTextField.getText();
            String priorityText = priorityTextField.getText();
            if (keyText.isEmpty() || priorityText.isEmpty()) {
                messageTextField.setText("Поля ключа или значения не могут быть пустыми!");
                messageTextField.setStyle("-fx-text-fill: red;");
            }
            else {
                int key = Integer.parseInt(keyText);
                int priority = Integer.parseInt(priorityText);
                if (keyList.contains(key)) {
                    messageTextField.setText("Данный элемент нельзя вставить в дерево, так как элемент с таким ключом уже имеется!");
                    messageTextField.setStyle("-fx-text-fill: red;");
                }
                else {
                    tree.insert(key, priority);
                    keyList.add(key);
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    scrollPane.setContent(canvas);
                    scrollPane.setMinWidth(1169);
                    scrollPane.setMinHeight(609);
                    drawTree(gc, tree.getRoot(), canvas.getWidth() / 2, 40, 25, 15, 140, 0);
                    messageTextField.setText(String.format("Значение с ключом %d и приоритетом %d вставлено успешно.", key, priority));
                    messageTextField.setStyle("-fx-text-fill: green;");
                    countInsert++;
                }
            }
        }
    }

    public void findBtn(ActionEvent actionEvent) {
        String keyText = keyTextField.getText();
        String priorityText = priorityTextField.getText();
        if (keyText.isEmpty() || priorityText.isEmpty()) {
            messageTextField.setText("Поле ключа или значения не может быть пустым!");
            messageTextField.setStyle("-fx-text-fill: red;");
        }
        else {
            int key = Integer.parseInt(keyText);
            int priority = Integer.parseInt(priorityText);
            Node found = tree.find(key, priority);
            if (found == null) {
                messageTextField.setText(String.format("Элемент с ключом %d и приоритетом %d не найден.", key, priority));
                messageTextField.setStyle("-fx-text-fill: red;");
            } else {
                Node node = tree.find(key, priority);
                node.color = Color.GREEN;
                GraphicsContext gc = canvas.getGraphicsContext2D();
                scrollPane.setContent(canvas);
                scrollPane.setMinWidth(1169);
                scrollPane.setMinHeight(609);
                drawTree(gc, tree.getRoot(), canvas.getWidth() / 2, 40, 25, 15, 140, 0);
                messageTextField.setText(String.format("Найден элемент с ключом %d и приоритетом %d.", found.getKey(), found.getPriority()));
                messageTextField.setStyle("-fx-text-fill: green;");
                node.color = Color.BLACK;
            }
        }
    }

    public void btnDelete(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        String keyText = keyTextField.getText();
        String priorityText = priorityTextField.getText();

        if (keyText.isEmpty() || priorityText.isEmpty()) {
            messageTextField.setText("Поле ключа или значения не может быть пустым!");
            messageTextField.setStyle("-fx-text-fill: red;");
        }
        else {
            int key = Integer.parseInt(keyText);
            int priority = Integer.parseInt(priorityText);
            keyList.remove(Integer.valueOf(key));
            Node found = tree.find(key, priority);
            if (found == null) {
                messageTextField.setText(String.format("Элемент с ключом %d и приоритетом %d для удаления не найден!", key, priority));
                messageTextField.setStyle("-fx-text-fill: red;");
            }
            else {
                tree.delete(key, priority);
                countInsert--;
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                drawTree(gc, tree.getRoot(), canvas.getWidth()/2, 40, 25, 15, 140, 0);
                messageTextField.setText(String.format("Элемент с ключом %d и значением %d удален.", key, priority));
                messageTextField.setStyle("-fx-text-fill: green;");
            }
        }
    }

    public void btnDeleteAll(ActionEvent actionEvent) {
        if (countInsert == 0) {
            messageTextField.setText("Элементы дерева отсутствуют!");
            messageTextField.setStyle("-fx-text-fill: red;");
        }
        else {
            tree.deleteAll();
            keyList.clear();
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            messageTextField.setText("Все элементы дерева удалены.");
            messageTextField.setStyle("-fx-text-fill: green;");
            countInsert = 0;
        }
    }

    public void btnBackStepClicked(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        tree.undo();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawTree(gc, tree.getRoot(), canvas.getWidth()/2, 40, 25, 15, 140, 0);
        messageTextField.setText("Выполнен шаг назад.");
        messageTextField.setStyle("-fx-text-fill: green;");
    }

    public void btnNextStepClicked(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        tree.redo();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawTree(gc, tree.getRoot(), canvas.getWidth()/2, 40, 25, 15, 140, 0);
        messageTextField.setText("Выполнен шаг вперед.");
        messageTextField.setStyle("-fx-text-fill: green;");
    }
}