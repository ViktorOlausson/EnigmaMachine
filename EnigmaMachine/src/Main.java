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
            "NZVAYIEÖLWDKCHTGBÅQMOSURFXÄPJ", // rotor I
            "UÄMRKGBJSPÖLVWQEIAXTYCÅHNDFZO", // rotor II
            "HTOBRZCÄPMQVGÄXYDFLOJÅUNIKSEW", // rotor III
            "FRAÖUDBSTVKHMCYÅGNWZPLXEQJIÄO", // rotor IV
            "WKPVDLQHZMJSAYÅBNFUÖCXTIGREÄO", // rotor V
            "GTXZBHKQOAÄIMNRCFJDULWEPÖVSÅY", // rotor VI
            "MLÅÖAVWUTPHEBQKCRDYNJXIGZFSÄO", // rotor VII
            "CNÖYKUWAQGFZPLVMJDHRBÅIXOESTÄ"  // rotor VIII
    );
    static final List<String> POSSIBLE_REFLECTOR_COMBINATIONS = Arrays.asList(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ",
            //"AYBRCSDTEUFGVHWIXJZKÅLÄMNÖOP", // Reflector B (P is self-mapped)
            "AKBNCOEDFPGRHQISJTLUMVWXÅÄÖYZ", // Reflector C (Z is self-mapped)
            "ÄÅBVDXEWCUGTHSRIQPOJLMNÖYKZFA", // Reflector D (A is self-mapped)
            "ABCPDEFGHIJKLMNOQRSTUVWXÖÅYZÄ", // Reflector E (Ä is self-mapped)
            "LZKÄYÜONWXPQIJMHGÖRBCSTUVÅDEA", // Reflector F (A is self-mapped)
            "BCAEDFGHIJKLÅMNOPQRÖSTUVWXÄYZ"  // Reflector G (Z is self-mapped)
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

    public static Character runPlugboard(Character letter, Map<Character, Character> plugboard, Boolean isReverse){
        char newLetter;
        if(!isReverse){
            return plugboard.get(letter);
        }else {
            //newLetter = '?';
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

    public static void main(String[] args) {

        int nrRotors = 3; //could be a random number
        int[] rotorRotations = new int[nrRotors];

        EnigmaSetup machine = createEnigmaSetup(nrRotors);

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
                    Character letter = input.charAt(0);
                    letter = encodedLetter(letter ,machine.plugboard, machine.rotors, machine.reflector);
                    machine.rotors = updateRotors(machine.rotors, rotorRotations);
                    System.out.println("encoded letter: " + letter + "\n");
                }
            }catch (IOException e){
                System.out.println("something went wrong");
            }

        }
    }
}