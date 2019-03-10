package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Avik Sarkar
 */
public final class ValidatorUtil {

    private ValidatorUtil() {
    }

    /**
     * This method matches the email with regex provided.
     *
     * @param email the email to be  matched.
     * @return The status of the matching operation.
     * @link http://emailregex.com/
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

    public static boolean validateContactNo(String contactNo) {

        String regex = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[6-9]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contactNo);
        return matcher.matches();
    }

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

    public static boolean validateBatchName(String batchName) {

        String regex = "^(20)[0-9]{2}-(20)[0-9]{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(batchName);
        return matcher.matches();
    }


    public static boolean validateSemester(String semester) {

        String regex = "^[1-9][0-9]?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(semester);
        return matcher.matches();
    }

    public static boolean validateDateFormat(String date) {

        String regex = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    public static boolean validateGender(String gender) {

        String regex = "^male|female|others$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(gender);
        return matcher.matches();
    }


    public static boolean validateName(String name) {

        String regex = "^[a-zA-z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean validateAcademicItem(String item) {

        String regex = "^([a-zA-z0-9]+[ '.\\-]*(\\([a-zA-z0-9]+[ '.\\-]*[a-zA-z0-9]+\\))?)+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(item);
        return matcher.matches();

    }

    public static boolean validateId(String id) {

        String regex = "^[a-zA-z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(id);
        return matcher.matches();

    }

}
