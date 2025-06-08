package com.enigmamachine.enigmawithgui;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class EnigmaController {
    @FXML private GridPane keyboard;

    public void initialize(){
        String[][] keyRows = {
                {"Q","W","E","R","T","Y","U","I","O","P","Å"},
                {"A","S","D","F","G","H","J","K","L","Ö","Ä"},
                {"Z","X","C","V","B","N","M",","," + ",".","-"}};

        for(int row = 0; row < keyRows.length; row++){
            for(int col = 0; col < keyRows[row].length; col++){
                Button key = new Button(keyRows[row][col]);
                key.setMinSize(40,40);
                key.getStyleClass().add("keys");
                key.setOnAction(e -> keyLightUp((ActionEvent)e, key));//add function that lights up a key when it is pressed on the keyboard
                keyboard.add(key, col, row);
            }
        }
    }

    public void keyLightUp(ActionEvent e, Button b){
        b.getStyleClass().add("lightUp");
        //System.out.println(b.getText());// returns the content of the button(use for enigma integration)
        PauseTransition pause = new PauseTransition(Duration.seconds(0.15));
        pause.setOnFinished(evt -> b.getStyleClass().remove("lightUp"));
        pause.play();
    }


}
