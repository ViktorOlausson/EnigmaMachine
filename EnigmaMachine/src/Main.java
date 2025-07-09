import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ".toCharArray();
    static final String STRING_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ";
    static final List<String> POSSIBLE_ROTOR_COMBINATIONS = Arrays.asList(
            "EKMFLGDQVZNTOWYHXUSPAIBRCJ", // rotor I
            "AJDKSIRUXBLHWTMCQGZNPYFVOE", // rotor II
            "BDFHJLCPRTXVZNYEIWGAKMUSQO"  // rotor III
    );
    static final List<String> POSSIBLE_REFLECTOR_COMBINATIONS = Arrays.asList(
            "YRUHQSLDPXNGOKMIEBFZCWVJAT", // reflector B
            "FVPJIAOYEDRZXWGCTKUQSBNMHL"  // reflector C
    );
    public static final Random rand = new Random();

    static Map<Character, Character> generatePlugboard(int nrPairs){
        Map<Character, Character> plugboard = new HashMap<>();
        List<Integer> freeIndexes = new ArrayList<>();

        for(int i = 0; i < ALPHABET.length; i++){
            freeIndexes.add(i);
        }

        char[] finalCombinations = new char[ALPHABET.length];
        System.arraycopy(ALPHABET, 0, finalCombinations, 0, ALPHABET.length);

        Random rand = new Random();

        while(!freeIndexes.isEmpty() && nrPairs >= 0){
            nrPairs--;

            int index1 = freeIndexes.remove(rand.nextInt(freeIndexes.size()));
            int index2 = freeIndexes.remove(rand.nextInt(freeIndexes.size()));

            char temp = finalCombinations[index1];
            finalCombinations[index1] = finalCombinations[index2];
            finalCombinations[index2] = temp;
        }

        for(int i = 0; i < ALPHABET.length; i++){
            plugboard.put(ALPHABET[i], finalCombinations[i]);
        }

        return plugboard;
    }

    public static char runPlugboard(char letter, Map<Character, Character> plugboard, Boolean isReverse){
        char newLetter;
        if(!isReverse){
            newLetter = plugboard.get(letter);
        }else {
            newLetter = '?';
            for(Map.Entry<Character, Character> entry : plugboard.entrySet()){
                if(entry.getValue() == letter){
                    newLetter = entry.getKey();
                    break;
                }
            }
        }
        return newLetter;
    };

    public static List<Map<Character, Character>> createRotors(int nrRotors){
        List<Map<Character, Character>> rotors = new ArrayList<>();
        //Random rand = new Random();

        //System.out.println("\nUsing the following rotor combinations:");
        for(int i = 0; i < nrRotors; i++){
            Map<Character, Character> rotor = new HashMap<>();

            String randomRotor = POSSIBLE_ROTOR_COMBINATIONS.get(rand.nextInt(POSSIBLE_ROTOR_COMBINATIONS.size()));

            //System.out.println(randomRotor);

            for(int j = 0; j < randomRotor.length(); j++){
                rotor.put(ALPHABET[j], randomRotor.charAt(j));
            }

            rotors.add(rotor);
        }

        return rotors;
    }

    public static Map<Character, Character> createReflector(){
        Map<Character, Character> reflector = new HashMap<>();

        String randomReflector = POSSIBLE_REFLECTOR_COMBINATIONS.get(rand.nextInt(POSSIBLE_REFLECTOR_COMBINATIONS.size()));

        //System.out.println("\nUsing the following reflector:");
        //System.out.println(randomReflector);

        for(int i = 0; i < randomReflector.length(); i++){
            reflector.put(ALPHABET[i], randomReflector.charAt(i));
        }

        return reflector;
    }

    public static void main(String[] args) {

        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it

        while(true){

            try{
                System.out.print("input a letter to encode: ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                String input = reader.readLine().toUpperCase();
                System.out.println();
                if(input.isEmpty()){
                    break;
                }
                else{
                    char letter = input.charAt(0);
                    //letter = letter.toUpperCase();
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