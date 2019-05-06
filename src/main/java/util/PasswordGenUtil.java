package util;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to generate a random password and hash it.
 * <p>
 * Utility classes are final, cannot be instantiated and have static methods.
 *
 * @author Avik Sarkar
 */
public class PasswordGenUtil {

    /**
     * Private default constructor so that no other class can create an instance of this class.
     */
    private PasswordGenUtil() {
    }

    /**
     * This method generates a random password , hashes it and send them to the caller method.
     *
     * @return A hashMap containing the plain text password and the hashed password.
     */
    public static Map<String, String> generateNewPassword() {

        SecureRandom secureRandom = new SecureRandom();

        //length of the password
        int passwordLength = 8;

        /*list of characters from which the password of 8 length will be generated*/
        String smallChars = "abcdefghijklmnopqrstuvwxyz";
        String capitalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String spclChars = "!@#$%^&*_=+-/.?<>)";

        String finalString = smallChars + capitalChars + digits + spclChars;

        //store the password here
        char[] password = new char[passwordLength];

        for (int i = 0; i < passwordLength; i++) {

            /*
            Generate a random no between 0 to (finalString.length() - 1), and store the character at that index in
            password array.
             */
            password[i] = finalString.charAt(secureRandom.nextInt(finalString.length()));
        }

        //convert char array to String
        String pass = new String(password);

        //get the hashed password
        String hashedPass = BCrypt.hashpw(pass, BCrypt.gensalt());

        /*Create a HashMap with the structure :
        Key : password , Value : The plain text password ;
        Key : hashedPassword, Value : The hashed password.
         */
        Map<String, String> map = new HashMap<>();
        map.put("password", pass);
        map.put("hashedPassword", hashedPass);

        System.out.println(pass);
        return map;
    }
}
