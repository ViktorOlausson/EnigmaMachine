package com.enigmamachine.enigmawithgui;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class EnigmaController {
    @FXML private GridPane keyboard;
    @FXML private TextArea paper;

    private final Map<String, Button> keyMap = new HashMap<>();
    private boolean onCooldown = false;

    public void initialize(){
        String[][] keyRows = {
                {"Q","W","E","R","T","Y","U","I","O","P","Å"},
                {"A","S","D","F","G","H","J","K","L","Ö","Ä"},
                {"Z","X","C","V","B","N","M",","," + ",".","-"}};

        for(int row = 0; row < keyRows.length; row++){
            for(int col = 0; col < keyRows[row].length; col++){
                String label = keyRows[row][col];
                Button key = new Button(label);
                key.setMinSize(40,40);
                key.getStyleClass().add("keys");
                //key.setOnAction(e -> keyLightUp((ActionEvent)e, key));//add function that lights up a key when it is pressed on the keyboard
                keyboard.add(key, col, row);
                keyMap.put(label.trim(), key);
            }
        }

        keyboard.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if(newScene != null){
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::findPressedKey);

                //newScene.addEventHandler(KeyEvent.KEY_RELEASED, this::findRealesedKey);

                keyboard.requestFocus();
            }
        });
    }

    public void findPressedKey(KeyEvent e){
        String keyValue = e.getText().toUpperCase();

        if(keyValue.isEmpty()){
            keyValue = switch (e.getCode()){
                case COMMA   -> ",";
                case PERIOD  -> ".";
                case PLUS    -> "+";
                case MINUS   -> "-";
                case SPACE   -> "";
                default      -> "";
            };
        }
        Button b = keyMap.get(keyValue);
        if (b != null){
            keyLightUp(new ActionEvent(e.getSource(), e.getTarget()), b);
        }
    }

    public void findRealesedKey(KeyEvent e){
        String keyValue = e.getText().toUpperCase();

        if(keyValue.isEmpty()){
            keyValue = switch (e.getCode()){
                case COMMA   -> ",";
                case PERIOD  -> ".";
                case PLUS    -> "+";
                case MINUS   -> "-";
                case SPACE   -> "";
                default      -> "";
            };
        }
        Button b = keyMap.get(keyValue);
        if (b != null){
            //keyLightUp(new ActionEvent(e.getSource(), e.getTarget()), b);
            b.getStyleClass().remove("lightUp");
        }
    }

    public void keyLightUp(ActionEvent e, Button b){
        if(onCooldown) return;
        if(!onCooldown && !b.getStyleClass().contains("lightUp")){
            b.getStyleClass().add("lightUp");
        }
        onCooldown = true;

        paper.appendText(b.getText());
        //System.out.println(b.getText());// returns the content of the button(use for enigma integration)
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(evt -> b.getStyleClass().remove("lightUp"));
        pause.play();

        PauseTransition coolDown = new PauseTransition(Duration.seconds(0.5));
        coolDown.setOnFinished(evt -> onCooldown = false);
        coolDown.play();
    }

}
