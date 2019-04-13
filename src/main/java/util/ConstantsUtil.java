package util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

/**
 * Utility class to store constants only.
 * <p>
 * Utility classes are final, cannot be instantiated and have static methods.
 *
 * @author Avik Sarkar
 */
public final class ConstantsUtil {

    public static final int ADMIN_GID = 1;

    public static final int EXAM_CELL_MEMBER_GID = 2;

    public static final int PROFESSOR_HOD_GID = 3;

    public static final int PROFESSOR_GID = 4;

    public static final int SUCCESS = 200;

    public static final int DATABASE_ERROR = -1;

    public static final int LOGIN_ERROR = -2;

    public static final int PROMOTION_ERROR = -6;

    public static final int DATA_ALREADY_EXIST_ERROR = -3;

    public static final int DATA_INEXISTENT_ERROR = -4;

    public static final int DATA_DEPENDENCY_ERROR = -5;

    public static final int ADD_CHOICE = 1;

    public static final int EDIT_CHOICE = 2;

    public static final String USER_HOME = System.getProperty("user.home");

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static final String PROJECT_NAME = "examin";

    public static final String ROOT_DIR = "examin";

    public static final String CONFIG_DIR = "configs";

    public static final String CSV_DIR = "csv";

    public static final String PDF_DIR = "pdf";

    public static final Font TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD, new BaseColor
            (4, 38, 91));

    public static final Font RED_SUBTITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD, BaseColor.RED);

    public static final Font BLUE_SUBTITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD, new BaseColor
            (3, 13, 163));

    public static final Font CONTENT_FONT1 = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.BLACK);

    public static final Font CONTENT_FONT2 = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.NORMAL, BaseColor.BLACK);

    public static final Font HEADING_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.BOLD | Font.UNDERLINE
            , new BaseColor(3, 13, 163));

    /**
     * Private default constructor so that no other class can create an instance of this class.
     */
    private ConstantsUtil() {
    }
}
