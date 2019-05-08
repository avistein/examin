package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to validate Text Fields.
 * <p>
 * Utility classes are final, cannot be instantiated and have static methods.
 *
 * @author Avik Sarkar
 */
public final class ValidatorUtil {

    /**
     * Private default constructor so that no other class can create an instance of this class.
     */
    private ValidatorUtil() {
    }

    /**
     * This method matches the email with regex provided.
     *
     * @param email the email to be  matched.
     * @return The result of the matching operation i.e. true or false.
     * @see <a href="http://emailregex.com/">Email Regex</a>
     */
    public static boolean validateEmail(String email) {

        String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+" +
                "/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21" +
                "\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\" +
                "x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z" +
                "0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|" +
                "[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9]" +
                "[0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\" +
                "x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-" +
                "\\x7f])+)])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * This method matches the contact no with the regex provided.
     *
     * @param contactNo The contact no. to be matched.
     * @return The result of the matching operation i.e. true or false.
     * @see <a href="https://stackoverflow.com/a/22061081/1943882">Contact No. Regex</a>
     */
    public static boolean validateContactNo(String contactNo) {

        String regex = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[1-9]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contactNo);
        return matcher.matches();
    }

    /**
     * This method matches the regyear with the regex provided and also checks if the regYEAR is within the Batch Name
     * range.
     *
     * @param batchName The batch with whom the reg year would be checked to see if the regyear is in the range.
     * @param regYear   The regYear to be matched with the regex provided.
     * @return The result i.e. true or false.
     */
    public static boolean validateRegYear(String batchName, String regYear) {

        String regex = "^(20)[0-9][0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(regYear);
        if (matcher.matches()) {
            String[] years = batchName.split("-");
            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);
            for (int i = startYear; i <= endYear; i++) {
                if (i == Integer.parseInt(regYear))
                    return true;
            }
        }
        return false;
    }

    /**
     * This method matches the batchName with the regex provided.
     *
     * @param batchName The batchName to be matched.
     * @return The result of the operation i.e. true or false.
     */
    public static boolean validateBatchName(String batchName) {

        String regex = "^(20)[0-9]{2}-(20)[0-9]{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(batchName);
        return matcher.matches();
    }

    /**
     * This method matches the semester with the regex provided.
     *
     * @param semester The semester to be matched.
     * @return The result of the operation i.e. true or false.
     */
    public static boolean validateSemester(String semester) {

        String regex = "^[1-9][0-9]?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(semester);
        return matcher.matches();
    }

    /**
     * This method matches the date with the regex provided.
     *
     * @param date The date to be matched.
     * @return The result of the operation i.e. true or false.
     */
    public static boolean validateDateFormat(String date) {

        String regex = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    /**
     * This method matches the gender with the regex provided.
     *
     * @param gender The gender to be matched.
     * @return The result of the operation i.e. true or false.
     */
    public static boolean validateGender(String gender) {

        String regex = "^Male|Female|Others$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(gender);
        return matcher.matches();
    }

    /**
     * This method matches an Academic Item such as Degree,Discipline,Building Name,Dept name etc. with regex provided.
     *
     * @param item The item to be matched.
     * @return The result of the operation i.e. true or false.
     */
    public static boolean validateAcademicItem(String item) {

        String regex = "^([a-zA-z0-9]+[ '.\\-]*(\\([a-zA-z0-9]+[ '.\\-]*[a-zA-z0-9]+\\))?)+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(item);
        return matcher.matches();

    }

    /**
     * This method matches the time format with the regex provided.
     *
     * @param time The time format to be matched.
     * @return The result of the operation i.e. true or false.
     */
    public static boolean validateTimeFormat(String time) {

        String regex = "^([0-9]{2}:[0-9]{2}:[0-9]{2})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    /**
     * This method matches a number with the regex provided.
     *
     * @param number The number to be matched.
     * @return The result of the operation i.e. true or false.
     */
    public static boolean validateNumber(String number) {

        String regex = "^\\d+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
}
