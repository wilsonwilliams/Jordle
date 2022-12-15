import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.Random;

/**
 * @author Wilson Williams
 * @version 11.0.13
 */
public class Jordle extends Application {
    /**
     * Main method for the class.
     * @param args Default parameter for method.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Generates random word for the game.
     * @return String representing the random word.
     */
    public static String word() {
        Random rand = new Random();
        int num = rand.nextInt(Words.list.size());

        String word = Words.list.get(num);
        return word;
    }

    private int row = 0;
    /**
     * Getter method for row number.
     * @return Int representing row.
     */
    public int getRow() {
        return row;
    }
    /**
     * Setter method for row number.
     * Increments row after each guess.
     */
    public void setRow() {
        row++;
    }

    /**
     * Method stating whether or not all guesses are used up.
     * @return Boolean saying if 6 guesses have been used.
     */
    public boolean toContinue() {
        if (row == 6) {
            return false;
        }
        return true;
    }

    private boolean done = false;
    /**
     * Getter method for done. If the user guessed the word correctly.
     * @return Whether or not the user guessed the word.
     */
    public boolean getDone() {
        return done;
    }
    /**
     * Setter method for done. Will run if user guesses the word correctly.
     */
    public void setDone() {
        done = true;
    }

    /**
     * Start method for the game.
     * @param primaryStage The main stage that the game takes place on.
     */
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JORDLE");

        BorderPane bpane = new BorderPane();
        HBox buttons = new HBox(500);
        Button instruct = new Button("Instructions");
        Button restart = new Button("Restart");
        Label tip = new Label("Try guessing another word!");
        buttons.getChildren().addAll(tip, instruct, restart);
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.CENTER);
        bpane.setBottom(buttons);

        HBox title = new HBox(500);
        Label label = new Label("JORDLE");
        label.setFont(Font.font("times new roman", FontWeight.BOLD, 80));
        title.getChildren().add(label);
        title.setAlignment(Pos.CENTER);
        bpane.setTop(title);

        Scene scene = new Scene(bpane, 1000, 750);
        primaryStage.setScene(scene);

        instruct.setOnMouseClicked(e -> {
            Label message = new Label("You have 6 guesses to guess a 5 letter word. After each guess, each tile"
                + " will be green, yellow, or gray. If gray, the letter is not in the word. If yellow, the letter is in"
                    + " the word but in the incorrect spot. If green, the letter is in the word and the correct spot."
                        + " Type a 5 letter word into the GUESS box and hit ENTER. The word will then appear "
                            + "on the game board. Good Luck!");
            message.setWrapText(true);
            message.setPadding(new Insets(0, 15, 0, 15));

            StackPane secondary = new StackPane();
            secondary.getChildren().add(message);
            Scene second = new Scene(secondary, 300, 200);

            Stage newWindow = new Stage();
            newWindow.setTitle("INSTRUCTIONS");
            newWindow.setScene(second);
            newWindow.show();
        });

        restart.setOnMouseClicked(e -> {
            row = 0;
            start(primaryStage);
        });

        GridPane gridpane = new GridPane();
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 5; ++j) {
                Rectangle rect = new Rectangle(50, 50);
                rect.setStyle("-fx-fill: white; -fx-stroke: solid; -fx-stroke: 30; -fx-stroke: black;");
                gridpane.add(rect, j, i);
                gridpane.setHgap(10);
                gridpane.setVgap(20);
            }
        }
        gridpane.setAlignment(Pos.CENTER);

        HBox input = new HBox(300);
        TextField textfield = new TextField();
        Label guess = new Label("GUESS: ");

        input.getChildren().addAll(guess, textfield);
        input.setSpacing(10);
        input.setAlignment(Pos.BOTTOM_CENTER);

        VBox game = new VBox(500);
        game.getChildren().addAll(gridpane, input);
        game.setSpacing(25);

        bpane.setCenter(game);
        bpane.setMargin(title, new Insets(50, 0, 0, 0));
        bpane.setMargin(game, new Insets(50, 0, 0, 0));
        bpane.setMargin(buttons, new Insets(0, 0, 5, 0));

        String word = word();

        textfield.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (toContinue() && !getDone()) {
                    String guess = textfield.getText();
                    guess = guess.toLowerCase();

                    if (word.equals(guess)) {
                        tip.setText("Congratulations! You guessed the word!");
                        setDone();
                    }

                    if (guess.matches("[a-zA-Z]+")) {
                        if (guess.length() != 5) {
                            StackPane tertiary = new StackPane();
                            Label message = new Label("Your guess must be 5 letters long!");
                            message.setWrapText(true);
                            message.setPadding(new Insets(0, 15, 0, 15));

                            tertiary.getChildren().add(message);
                            Scene third = new Scene(tertiary, 300, 200);

                            Stage newWindow = new Stage();
                            newWindow.setTitle("ERROR MESSAGE");
                            newWindow.setScene(third);

                            newWindow.show();
                        } else {
                            int rows = getRow();
                            setRow();

                            for (int i = 0; i < 5; ++i) {
                                Rectangle rect = new Rectangle(50, 50);

                                char letter = guess.charAt(i);

                                HBox answer = new HBox(10);
                                Label label = new Label(Character.toString(letter).toUpperCase());
                                answer.getChildren().add(label);

                                if (word.indexOf(letter) < 0) {
                                    rect.setStyle("-fx-fill: gray; -fx-stroke: solid; -fx-stroke: 30;"
                                        + " -fx-stroke: black;");
                                } else if (word.indexOf(letter) == i) {
                                    rect.setStyle("-fx-fill: chartreuse; -fx-stroke: solid;"
                                        + " -fx-stroke: 30; -fx-stroke: black;");
                                } else {
                                    rect.setStyle("-fx-fill: yellow; -fx-stroke: solid;"
                                        + " -fx-stroke: 30; -fx-stroke: black;");
                                }

                                gridpane.add(rect, i, rows);
                                gridpane.setHgap(10);
                                gridpane.setVgap(20);

                                HBox result = new HBox(50);
                                Text text = new Text(Character.toString(letter).toUpperCase());
                                text.setFont(Font.font("times new roman", FontWeight.BOLD, 40));
                                result.getChildren().add(text);
                                result.setAlignment(Pos.CENTER);
                                gridpane.add(result, i, rows);
                            }
                        }
                    }
                    if (!toContinue() && !getDone()) {
                        tip.setText("Game Over! The word was " + word + ".");
                    }
                    textfield.clear();
                }
            }
        });
        primaryStage.show();
    }
}