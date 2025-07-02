import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ".toCharArray();

    static Map<Character, Character> plugboard(int nrPairs){
        Map<Character, Character> plugboard = new HashMap<>();
        List<Integer> freeIndexes = new ArrayList<>();

        for(int i = 0; i < ALPHABET.length; i++){
            freeIndexes.add(i);
        }

        char[] finalCombinations = new char[ALPHABET.length];
        for

        return plugboard;
    }
    public static void main(String[] args) {

        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it

        while(true){

            try{
                System.out.print("input a letter to encode: ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                String letter = reader.readLine();
                System.out.println();
                if(letter.equals("")){
                    break;
                }
                else{
                    letter = letter.toUpperCase();
                    //letter = encodeLetter(letter, plugboard, rotors, reflector); //encode letter
                    //rotor = updateRotors(rotors, rotorsRotation); // update rotor position
                    System.out.println("encoded letter: " + letter + "\n");
                }
            }catch (IOException e){
                System.out.println("something went wrong");
            }

        }
    }
}