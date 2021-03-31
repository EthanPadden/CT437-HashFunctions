package CT437;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * @author Michael Schukat
 */
public class CT437_HashFunction1 {

    public static void main(String[] args) {
        int res = 0;
        String s = "abb";
        // THIS CONSIDERS ALL PERMUTATIONS - NO NEED FOR OTHER FUNCTION
//        ArrayList<String> test =  new ArrayList<String>();
//        checkAllPermutationsOfLength("abcd", "", 3, 3, test);
//        System.out.println("FOUND: " + test.size());

//        getAllPermutations(s, "", null, 3, new ArrayList<String>());

        if (args != null && args.length > 0) { // Check for <input> value
            res = hashF1(args[0]); // call hash function with <input>
            if (res < 0) { // Error
                System.out.println("Error: <input> must be 1 to 64 characters long.");

            }
            else {
                System.out.println("input = " + args[0] + " : Hash = " + res);
                System.out.println("Start searching for collisions");
                /** Problem 1: Looking for collisions */
                // Brute force using the pattern:
                //      "a", "b", "c", ... "9"
                //      "aa", "ab", "ac", ... "99"
                //      ...
                //      "aa...a", "aa...b", "aa...c", ... "99...9" (length 64)
                // Over the characters: "abc...zABC...Z012...9"
//
                String alphabet = "abcdefghijklmnopqrstuvwxyABCDEFGHIJKLMNOPQURTUVWXYZ0123456789";
                ArrayList<String> collisions = new ArrayList<String>();
                int targetHash = hashF1(args[0]);
//                getAllPermutations(alphabet, "");
                System.out.println(hashF1(args[0]));
                for(int length = 1; length <= alphabet.length(); length++) {
                    System.out.println("Checking all strings of length: " + length);
                    checkAllPermutationsOfLength(alphabet, "", alphabet.length(), length, collisions, targetHash);
                    System.out.println("Collisions found so far: " + collisions.size());
                }


//                for(int len = 1; len <= 64; len++) {
//                    // Initial combination is "aa...a" of length len
//                    char[] currentCombination = Strings.repeat(alphabet.charAt(0), len).toCharArray();
//
//                    // Point to the end of the string - this will cycle the character through the alphabet
//                    int currentRotator = currentCombination.length-1;
//
//                    // Iterating from the end of the string to the start
//                    while (currentRotator >= 0) {
//                        // Iterate over the alphabet at that point
//                        currentRotator--;
//                    }
//                }
            }
        }
        else { // No <input> 
            System.out.println("Use: CT437_HashFunction1 <Input>");
        } 
    }

    private static void checkAllPermutationsOfLength(String alphabet, String newStringHolder, int intitialAlphabetLength, int length, ArrayList<String> collisions, int targetHash)
    {
        if (length == 0) {
            if(hashF1(newStringHolder) == targetHash){
                System.out.println("Collision found: " + newStringHolder);
                collisions.add(newStringHolder);
            }
            return;
        }
        for (int i = 0; i < intitialAlphabetLength; i++) {
            String newSequence;
            newSequence = newStringHolder + alphabet.charAt(i);
            checkAllPermutationsOfLength(alphabet, newSequence, intitialAlphabetLength, length - 1, collisions, targetHash);
        }
    }
//   private static void getAllPermutations(String left, String right, String target, int targetHash, ArrayList<String> collisions)
//    {
//        // Recursive termination
//        if (left.length() == 0) {
//            if(hashF1(right) == targetHash && right.toString().compareTo(target) != 0) {
//                collisions.add(right);
//            }
//            return;
//        }
//
//        for (int i = 0; i < left.length(); i++) {
//            char currentCharacter = left.charAt(i);
//
//            // Remainder of string
//            String remainder = left.substring(0, i) + left.substring(i + 1);
//
//            getAllPermutations(remainder, right + currentCharacter, target, targetHash, collisions);
//        }
//    }

        
    private static int hashF1(String s){
        int ret = -1, i;
        int[] hashA = new int[]{1, 1, 1, 1};
        
        String filler, sIn;
        
        filler = new String("ABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGH");
        
        if ((s.length() > 64) || (s.length() < 1)) { // String does not have required length
            ret = -1;
        }
        else {
            sIn = s + filler; // Add characters, now have "<input>HABCDEF..."
            sIn = sIn.substring(0, 64); // // Limit string to first 64 characters
            // System.out.println(sIn); // FYI
            for (i = 0; i < sIn.length(); i++){
                char byPos = sIn.charAt(i); // get i'th character
                hashA[0] += (byPos * 17); // Note: A += B means A = A + B
                hashA[1] += (byPos * 31);
                hashA[2] += (byPos * 101);
                hashA[3] += (byPos * 79);
            } 
            
            hashA[0] %= 255;  // % is the modulus operation, i.e. division with rest
            hashA[1] %= 255;
            hashA[2] %= 255;
            hashA[3] %= 255;
            
            ret = hashA[0] + (hashA[1] * 256) + (hashA[2] * 256 * 256) + (hashA[3] * 256 * 256 * 256);
            if (ret < 0) ret *= -1;
        }
        return ret;
    }    
}