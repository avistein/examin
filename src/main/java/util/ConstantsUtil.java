package util;

public final class ConstantsUtil {

    private ConstantsUtil(){}

    public static final int ADMIN_GID = 1;

    public static final int EXAM_CELL_MEMBER_GID = 2;

    public static final int PROFESSOR_HOD_GID = 3;

    public static final int PROFESSOR_GID = 4;

    public static final int SUCCESS = 0;

    public static final int DATABASE_ERROR = -1;

    public static final int LOGIN_ERROR = -2;

    public static final int DATA_ALREADY_EXIST_ERROR = -3;

    public static final int DATA_INEXISTENT_ERROR = -4;

    public static final int DATA_DEPENDENCY_ERROR = -5;

    public static final int ADD_CHOICE = 1;

    public static final int EDIT_CHOICE = 2;

    public static final String USER_HOME = System.getProperty("user.home");

    public static  final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static  final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static  final String PROJECT_NAME = "examin - Examination Management Tool";

    public static  final String ROOT_DIR = "examin";

    public static  final String CONFIG_DIR = "configs";

    public static  final String CSV_DIR = "csv";

    public static  final String SETTINGS_DIR = "settings";
}
