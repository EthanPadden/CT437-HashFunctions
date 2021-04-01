package CT437;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * @author Michael Schukat
 */
public class CT437_HashFunction1 {
    static String alphabet = "abcdefghijklmnopqrstuvwxyABCDEFGHIJKLMNOPQURTUVWXYZ0123456789";
    static ArrayList<String> collisions;
    static int numChecks;

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
                // Create a list to store the collisions
                collisions = new ArrayList<String>();

                // This keeps track of the number of checks made so we can verify that the enhanced function is better
                numChecks = 0;

                // This is the hash value for which we are trying to find collisions
                int targetHash = hashF1(args[0]);

                // There can be lots of collisions, e.g. for “Bamb0”:
                // #collisions of length 2 = 20
                // #collisions of length 3 = 1962
                // #collisions of length 4 = 121552
                // Out of interest, this can be used to limit the number of collisions of each length
                // If you do not want to use a limit, set this to -1
                // This is used within the recursive function
                int limitPerStringLength = 3;

                // This is the total number of collisions we want to find
                // This is to prevent a memory overflow
                // If you do not want to use a limit, set this to -1
                // This is used in the for loop below
                int totalLimit = 50;

                // Iterate over different string lengths, from 1 to 64
                for(int length = 1; length <= 64; length++) {
                    System.out.println("Checking all strings of length: " + length);
                    checkAllPermutationsOfLength( "",  length,  targetHash, limitPerStringLength*length, totalLimit, true);
//                    System.out.println("Collisions found so far: " + collisions.size());

                    // Preventing the output from repeating
                    if(collisions.size() == totalLimit) {
                        System.out.println("Collisions found: " +collisions.size());
                        System.out.println("Checks: " + numChecks);
                        float collisionPercentage = (((float) collisions.size()) / ((float) numChecks)) * 100;
                        System.out.println("Collision percentage: " + collisionPercentage + "%");
                        break;
                    }
                }

            }
        }
        else { // No <input> 
            System.out.println("Use: CT437_HashFunction1 <Input>");
        } 
    }

    private static void checkAllPermutationsOfLength(String newStringHolder, int length, int targetHash, int collisionLimit, int totalLimit, boolean usingNewFunction)
    {
        /** Extra recursion base case
         * collisionLimit = positive integer:
         *      used if we want a limit to the number of collisions we want to find
         *      assumes that the collisions argument is initially passed to the recursive function as an empty list
         *      the size will eventually reach the limits upon finding sufficient collisions (if they exist)
         * collisionLimit = -1:
         *      used if we do not want to set a limit
         *      collisions.size() can never be -1, so the condition is skipped
         * */
        if(collisions.size() == collisionLimit || collisions.size() == totalLimit) {
            return;
        }
        if (length == 0) {
            // We have reached a base case here
            // Check is this string a collision, and add it to the list if it is
            int hash;
            if(usingNewFunction) hash = hashF2(newStringHolder);
            else hash = hashF1(newStringHolder);

            numChecks++;

            if(hash == targetHash){
                System.out.println("Collision found: " + newStringHolder);
                collisions.add(newStringHolder);
            }
            return;
        }
        for (int i = 0; i < alphabet.length(); i++) {
            String newSequence;
            newSequence = newStringHolder + alphabet.charAt(i);
            checkAllPermutationsOfLength(newSequence, length - 1,  targetHash, collisionLimit, totalLimit, usingNewFunction);
        }
    }

    private static int hashF2(String s){
        int ret = -1, i;
        int[] hashA = new int[]{1, 1, 1, 1};

        // Initialise filler to empty string
        String filler = "", sIn;
        filler = new String("ABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGH");


        if ((s.length() > 64) || (s.length() < 1)) { // String does not have required length
            ret = -1;
        }
        else {
            int targetFillerLength = 64 - s.length();


            int j = 0;
            String fillerToAdd = "";
            while (filler.length() < targetFillerLength) {
                // Get the int
                int byPos = s.charAt(j%s.length()) + filler.charAt(j%filler.length());

                char letterToAdd = alphabet.charAt(byPos);
                fillerToAdd += letterToAdd;
                j++;
            }


            sIn = s + fillerToAdd; // Add characters, now have "<input>HABCDEF..."
//            sIn = sIn.substring(0, 64); // // Limit string to first 64 characters
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