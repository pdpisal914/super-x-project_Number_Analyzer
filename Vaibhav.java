import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.Duration;

public class Vaibhav extends Application {
    private TextArea textArea = new TextArea();
    BorderPane root = new BorderPane();
    private Text[] numberTexts = new Text[100];
    private Rectangle[] numberRectangles = new Rectangle[100];
    private GridPane numberGrid = new GridPane();
    private Timeline blinkTimeline;
    private boolean isBlinking = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Number Highlighter");

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1366, 768);
        
        Image backgroundImage = new Image("Blueback.jpg");

        // Create a Pane as the root container

        // Create a BackgroundImage with your loaded image
        BackgroundImage backgroundImg = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT
        );

        // Create a Background with the BackgroundImage
        Background background = new Background(backgroundImg);
        Label nameLabel = new Label("NUMBER HIGHLIGHTER");
        GridPane.setConstraints(nameLabel, 20, 2);

        // Set the background of the root Pane
        root.setBackground(background);
        // Create a GridPane for numbers with larger boxes
        numberGrid.setPadding(new Insets(10));
        numberGrid.setHgap(40);
        numberGrid.setVgap(40);

        for (int i = 0; i <= 99; i++) {
            numberTexts[i] = new Text(Integer.toString(i+1));
            numberTexts[i].setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            numberRectangles[i] = new Rectangle(60, 60, Color.WHITE);
            numberRectangles[i].setStroke(Color.BLACK);
            numberRectangles[i].setStrokeWidth(1);

            // Create a StackPane to center the number in the rectangle
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.getChildren().addAll(numberRectangles[i], numberTexts[i]);

            numberGrid.add(stackPane, i % 10, i / 10);
        }

        root.setCenter(numberGrid);
      

        // Create a VBox for buttons at the top
        BorderPane.setAlignment(numberGrid, javafx.geometry.Pos.CENTER);
        root.setCenter(numberGrid);

        BorderPane.setAlignment(numberGrid, javafx.geometry.Pos.TOP_CENTER);
        BorderPane buttonPane = createButtonPane();
        root.setTop(buttonPane);
    
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createButtonPane() {
        BorderPane buttonPane = new BorderPane();
        buttonPane.setPadding(new Insets(10,10,10,10));
        

        Button evenButton = createGlowButton("Highlight Even", Color.BLUE);
        Button oddButton = createGlowButton("Highlight Odd", Color.RED);
        Button primeButton = createGlowButton("Highlight Prime", Color.GREEN);
        Button armstrongButton = createGlowButton("Highlight Armstrong", Color.ORANGE);
        Button perfectButton = createGlowButton("Highlight Perfect", Color.PURPLE);

        evenButton.setOnAction(e -> highlightEvenNumbers(evenButton));

        
        oddButton.setOnAction(e -> highlightOddNumbers(oddButton));
        primeButton.setOnAction(e -> highlightPrimeNumbers(primeButton));
        armstrongButton.setOnAction(e -> highlightArmstrongNumbers(armstrongButton));
        perfectButton.setOnAction(e -> highlightPerfectNumbers(perfectButton));

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setPadding(new Insets(10, 0, 0, 0));

        buttonGrid.add(evenButton, 0, 0);
        buttonGrid.add(oddButton, 1, 0);
        buttonGrid.add(primeButton, 2, 0);
        buttonGrid.add(armstrongButton, 3, 0);
        buttonGrid.add(perfectButton, 4, 0);

        buttonPane.setCenter(buttonGrid);

        return buttonPane;
    }

    private Button createGlowButton(String text, Color glowColor) {
        Button button = new Button(text);
        button.setStyle("-fx-base: " + toHex(glowColor) + ";");
        button.setEffect(new DropShadow());

        button.setOnMouseEntered(e -> {
            Glow glow = new Glow(0.7);
            glow.setInput(button.getEffect());
            button.setEffect(glow);
        });

        button.setOnMouseExited(e -> {
            button.setEffect(new DropShadow());
        });

        return button;
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

   
    private void highlightEvenNumbers(Button button) {
        stopBlinking();
        
        for (int i = 0; i < numberTexts.length; i++) {
            int number = Integer.parseInt(numberTexts[i].getText());

            if (number % 2 == 0) {
                startBlinking(numberRectangles[i], Color.BLUE);
            } else {
                resetHighlight(numberRectangles[i]);
            }
        }
        startButtonBlinking(button);
    }

    private void highlightOddNumbers(Button button) {
        stopBlinking();
        for (int i = 0; i < numberTexts.length; i++) {
            int number = Integer.parseInt(numberTexts[i].getText());

            if (number % 2 != 0) {
                startBlinking(numberRectangles[i], Color.RED);
            } else {
                resetHighlight(numberRectangles[i]);
            }
        }
        startButtonBlinking(button);
    }

    private void highlightPrimeNumbers(Button button) {
        stopBlinking();
        for (int i = 0; i < numberTexts.length; i++) {
            int number = Integer.parseInt(numberTexts[i].getText());

            if (isPrime(number)) {
                startBlinking(numberRectangles[i], Color.GREEN);
            } else {
                resetHighlight(numberRectangles[i]);
            }
        }
        startButtonBlinking(button);
    }

    private void highlightArmstrongNumbers(Button button) {
        stopBlinking();
        for (int i = 0; i < numberTexts.length; i++) {
            int number = Integer.parseInt(numberTexts[i].getText());

            if (isArmstrong(number)) {
                startBlinking(numberRectangles[i], Color.ORANGE);
            } else {
                resetHighlight(numberRectangles[i]);
            }
        }
        startButtonBlinking(button);
    }

    private void highlightPerfectNumbers(Button button) {
        stopBlinking();
        for (int i = 0; i < numberTexts.length; i++) {
            int number = Integer.parseInt(numberTexts[i].getText());

            if (isPerfect(number)) {
                startBlinking(numberRectangles[i], Color.PURPLE);
            } else {
                resetHighlight(numberRectangles[i]);
            }
        }
        startButtonBlinking(button);
    }

    private void startBlinking(Rectangle rectangle, Color color) {
        stopBlinking();
        resetHighlight(rectangle);
        Glow glow = new Glow(0.7);
        glow.setInput(rectangle.getEffect());
        rectangle.setFill(color);
        rectangle.setEffect(glow);

        // Create a blinking effect
        KeyValue keyValue = new KeyValue(rectangle.opacityProperty(), 0.3);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
        blinkTimeline = new Timeline(keyFrame);
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);
        blinkTimeline.setAutoReverse(true);
        blinkTimeline.play();
    }

    private void startButtonBlinking(Button button) {
        stopButtonBlinking();
        Glow glow = new Glow(0.7);
        glow.setInput(button.getEffect());
        button.setEffect(glow);

        // Create a blinking effect for the button
        KeyValue keyValue = new KeyValue(button.opacityProperty(), 0.3);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
        blinkTimeline = new Timeline(keyFrame);
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);
        blinkTimeline.setAutoReverse(true);
        blinkTimeline.play();
    }

    private void resetHighlight(Rectangle rectangle) {
        rectangle.setFill(Color.WHITE);
        rectangle.setEffect(null);
        if (blinkTimeline != null) {
            blinkTimeline.stop();
        }
        rectangle.setOpacity(1.0);
    }

    private void stopBlinking() {
        if (isBlinking) {
            if (blinkTimeline != null) {
                blinkTimeline.stop();
                isBlinking = false;
            }
        }
    }

    private void stopButtonBlinking() {
        if (isBlinking) {
            if (blinkTimeline != null) {
                blinkTimeline.stop();
                isBlinking = false;
            }
        }
    }

    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i * i <= number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isArmstrong(int number) {
        int originalNumber = number;
        int result = 0;
        int n = String.valueOf(number).length();

        while (number != 0) {
            int digit = number % 10;
            result += Math.pow(digit, n);
            number /= 10;
        }

        return result == originalNumber;
    }

    private boolean isPerfect(int number) {
        int sum = 1;
        for (int i = 2; i * i <= number; i++) {
            if (number % i == 0) {
                sum += i;
                if (i != number / i) {
                    sum += number / i;
                }
            }
        }
        return sum == number && number != 1;
    }

    public static void main(String[] args) {
        launch(args);
    }
}