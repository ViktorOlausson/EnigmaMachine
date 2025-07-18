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

import java.util.*;

public class EnigmaController {
    @FXML private GridPane keyboard;
    @FXML private TextArea paper;

    public static class EnigmaSetup{
        public Map<Character, Character> plugboard;
        public List<Map<Character, Character>> rotors;
        public Map<Character, Character> reflector;

        public EnigmaSetup(Map<Character, Character> _plugboard, List<Map<Character, Character>> _rotors, Map<Character, Character> _reflector){
            plugboard = _plugboard;
            rotors = _rotors;
            reflector = _reflector;
        }
    }

    public static Map<Character, Character> shiftValues(Map<Character, Character> rotor){
        Map<Character, Character> shifted = new LinkedHashMap<>();

        List<Character> values = new ArrayList<>(rotor.values());
        Collections.rotate(values, -1);

        int i = 0;
        for(Character key : rotor.keySet()){
            shifted.put(key, values.get(i));
            i++;
        }

        return shifted;
    }

    static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ".toCharArray();
    static final String STRING_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ";
    static final List<String> POSSIBLE_ROTOR_COMBINATIONS = Arrays.asList(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ",
            "NZVAYIEÖLWDKCHTGBÅQMOSURFXÄPJ",
            "HTOBRZCÄPMQVGÄXYDFLOJÅUNIKSEW",
            "UÄMRKGBJSPÖLVWQEIAXTYCÅHNDFZO",
            "FRAÖUDBSTVKHMCYÅGNWZPLXEQJIÄO",
            "WKPVDLQHZMJSAYÅBNFUÖCXTIGREÄO",
            "GTXZBHKQOAÄIMNRCFJDULWEPÖVSÅY",
            "MLÅÖAVWUTPHEBQKCRDYNJXIGZFSÄO",
            "CNÖYKUWAQGFZPLVMJDHRBÅIXOESTÄ"
    );
    static final List<String> POSSIBLE_REFLECTOR_COMBINATIONS = Arrays.asList(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ",
            "AKBNCOEDFPGRHQISJTLUMVWXÅÄÖYZ",
            "ÄÅBVDXEWCUGTHSRIQPOJLMNÖYKZFA",
            "ABCPDEFGHIJKLMNOQRSTUVWXÖÅYZÄ",
            "LZKÄYÜONWXPQIJMHGÖRBCSTUVÅDEA",
            "BCAEDFGHIJKLÅMNOPQRÖSTUVWXÄYZ"
    );
    public static final Random rand = new Random();

    static Map<Character, Character> generatePlugboard(){
        Map<Character, Character> plugboard = new HashMap<>();
        List<Character> avalibleLetters = new ArrayList<>();

        for(char C : ALPHABET){
            avalibleLetters.add(C);
        }

        Collections.shuffle(avalibleLetters);
        int pairs = ALPHABET.length / 2;

        for(int i = 0; i < pairs *2; i += 2){
            char a = avalibleLetters.get(i);
            char b = avalibleLetters.get(i + 1);

            plugboard.put(a,b);
            plugboard.put(b,a);
        }
        if (ALPHABET.length % 2 != 0){
            char leftover = avalibleLetters.get(ALPHABET.length - 1);
            plugboard.put(leftover, leftover);
        }

        return plugboard;
    }

    public static Character runPlugboard(Character letter, Map<Character, Character> plugboard, Boolean isReverse){
        if(!isReverse){
            return plugboard.get(letter);
        }else {
            for(Map.Entry<Character, Character> entry : plugboard.entrySet()){
                if(entry.getValue().equals(letter)){
                    return entry.getKey();
                }
            }
        }
        return '?';
    };

    public static List<Map<Character, Character>> createRotors(int nrRotors){
        List<Map<Character, Character>> rotors = new ArrayList<>();

        for(int i = 0; i < nrRotors; i++){
            Map<Character, Character> rotor = new HashMap<>();

            String randomRotor = POSSIBLE_ROTOR_COMBINATIONS.get(rand.nextInt(POSSIBLE_ROTOR_COMBINATIONS.size()));

            for(int j = 0; j < randomRotor.length(); j++){
                rotor.put(ALPHABET[j], randomRotor.charAt(j));
            }

            rotors.add(rotor);
        }

        return rotors;
    }

    public static Map<Character, Character> createReflector(){
        Map<Character, Character> reflector = new HashMap<>();
        List<Character> letters = new ArrayList<>();

        for(char c : ALPHABET){
            letters.add(c);
        }

        Collections.shuffle(letters);

        for(int i = 0; i + 1 < letters.size(); i += 2){
            char a = letters.get(i);
            char b = letters.get(i + 1);
            reflector.put(a, b);
            reflector.put(b, a);
        }

        if (letters.size() % 2 != 0) {
            char last = letters.get(letters.size() - 1);
            reflector.put(last, last);
        }

        return reflector;
    }

    public static EnigmaSetup createEnigmaSetup(int nrRotors){
        Map<Character, Character> plugboard = generatePlugboard();

        List<Map<Character, Character>> rotors = createRotors(nrRotors);

        Map<Character, Character> reflector = createReflector();

        return new EnigmaSetup(plugboard, rotors, reflector);
    }

    public static Character runRotors(Character letter, List<Map<Character, Character>> rotors, Map<Character, Character> reflector){
        Character newLetter = letter;

        for (Map<Character, Character> rotor : rotors) {

            newLetter = rotor.get(newLetter);
        }

        newLetter = reflector.get(newLetter);

        for(int i = rotors.size() - 1; i >= 0; i--){
            Map<Character, Character> rotor = rotors.get(i);

            Character finalNewLetter = newLetter;

            newLetter = rotor.entrySet()
                    .stream()
                    .filter(e -> e.getValue().equals(finalNewLetter))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse('?');
        }

        return newLetter;
    }

    public static List<Map<Character, Character>> updateRotors(List<Map<Character, Character>> rotors, int[] rotorsRotation){
        //rotate first rotor
        rotorsRotation[0]++;
        rotors.set(0, shiftValues(rotors.get(0)));

        //rotate the other if needed
        for(int i = 0; i < rotorsRotation.length; i++){
            if(rotorsRotation[i] % ALPHABET.length == 0 && rotorsRotation[i] != 0){
                rotorsRotation[i] = 0;
                if(i + 1 < rotors.size()){
                    rotorsRotation[i + 1]++;
                    rotors.set(i + 1, shiftValues(rotors.get(i + 1)));
                }
            }
        }
        return rotors;
    }

    public static Character encodedLetter(Character Letter, Map<Character, Character> plugboard, List<Map<Character, Character>> rotors, Map<Character, Character> reflector){
        Character encodedLetter = Letter;

        encodedLetter = runPlugboard(Letter, plugboard, false);
        encodedLetter = runRotors(encodedLetter, rotors, reflector);
        encodedLetter = runPlugboard(encodedLetter, plugboard, true);

        return encodedLetter;
    }


    private final Map<String, Button> keyMap = new HashMap<>();
    private boolean onCooldown = false;

    int nrRotors = 3;
    int[] rotorRotations = new int[nrRotors];
    EnigmaSetup machine = createEnigmaSetup(nrRotors);

    public Character runEnigmaMachine(Character letter){
        Character returnValue = letter;
        returnValue = encodedLetter(returnValue ,machine.plugboard, machine.rotors, machine.reflector);
        machine.rotors = updateRotors(machine.rotors, rotorRotations);

        return returnValue;
    }

    public void initialize(){
        String[][] keyRows = {
                {"Q","W","E","R","T","Y","U","I","O","P","Å"},
                {"A","S","D","F","G","H","J","K","L","Ö","Ä"},
                {"Z","X","C","V","B","N","M",","," + ",".","-"},
                {"SPACE"}}; // fix me: space looking fucky whacky, add following keys: "?"

        for(int row = 0; row < keyRows.length; row++){
            for(int col = 0; col < keyRows[row].length; col++){
                String label = keyRows[row][col];
                Button key = new Button(label.equals("SPACE") ? " " : label);
                key.setMinSize(40,40);
                key.getStyleClass().add("keys");
                keyboard.add(key, col, row);
                if(label.equals("SPACE")){
                    keyMap.put(" ", key);
                }
                else{
                    keyMap.put(label.trim(), key);
                }
            }
        }

        keyboard.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if(newScene != null){
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::findPressedKey);

                keyboard.requestFocus();
            }
        });
    }

    public boolean containsLetter(String charToCheck){
        boolean result = true;

        switch (charToCheck){
            case ",", ".", "+", "-", " " -> result = false;
            default -> result = true;
        }

        return result;
    }

    public void findPressedKey(KeyEvent e){
        String keyValue = e.getText();

        if(containsLetter(keyValue)){
            Character encodedKeyValue = runEnigmaMachine(keyValue.toUpperCase().charAt(0));
            Button b = keyMap.get(encodedKeyValue.toString());
            if (b != null){
                keyLightUp(b);
            }
        }

        if(keyValue.isEmpty()){
            keyValue = switch (e.getCode()){
                case COMMA   -> ",";
                case PERIOD  -> ".";
                case PLUS    -> "+";
                case MINUS   -> "-";
                case SPACE   -> " ";
                default      -> " ";
            };
        }
        Button b = keyMap.get(keyValue);
        if (b != null){
            keyLightUp(b);
        }
    }

    public void keyLightUp(Button b){
        String btnChar = b.getText();
        if(onCooldown) return;
        if(!b.getStyleClass().contains("lightUp")){
            b.getStyleClass().add("lightUp");
        }
        onCooldown = true;

        paper.appendText(btnChar);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.25));
        pause.setOnFinished(evt -> b.getStyleClass().remove("lightUp"));
        pause.play();

        PauseTransition coolDown = new PauseTransition(Duration.seconds(0.5));
        coolDown.setOnFinished(evt -> onCooldown = false);
        coolDown.play();
    }

}
