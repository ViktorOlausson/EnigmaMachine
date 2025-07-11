import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static class EnigmaSetup{
        public Map<Character, Character> plugboard;
        public List<Map<Character, Character>> rotors;
        public Map<Character, Character> reflector;

        public EnigmaSetup(Map<Character, Character> _plugboard, List<Map<Character, Character>> _rotors, Map<Character, Character> _reflector){
            _plugboard = plugboard;
            _rotors = rotors;
            _reflector = reflector;
        }
    }

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

    static Map<Character, Character> generatePlugboard(){
        Map<Character, Character> plugboard = new HashMap<>();
        List<Integer> freeIndexes = new ArrayList<>();
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

        String randomReflector = POSSIBLE_REFLECTOR_COMBINATIONS.get(rand.nextInt(POSSIBLE_REFLECTOR_COMBINATIONS.size()));

        for(int i = 0; i < randomReflector.length(); i++){
            reflector.put(ALPHABET[i], randomReflector.charAt(i));
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

            Character originalLetter = '?';

            for(Map.Entry<Character, Character> entry : rotor.entrySet()){
                if(entry.getValue().equals(newLetter)){
                    originalLetter = entry.getKey();
                    break;
                }
            }
            newLetter = originalLetter;
        }

        return newLetter;
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