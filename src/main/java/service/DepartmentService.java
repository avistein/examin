package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Department;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Service class to get department, add department, edit department, delete department, get total no of departments.
 *
 * @author Avik Sarkar
 */
public class DepartmentService {

    /*--------------------------------Declaration of variables---------------------------------------*/

    private DatabaseHelper databaseHelper;

    /*------------------------------------End of Declaration-----------------------------------------*/

    /**
     * Default public constructor to initialize database helper.
     */
    public DepartmentService() {

        databaseHelper = new DatabaseHelper();
    }

    /**
     * This method is used to get a single departmentTask object which is used to get departments details.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_department.
     * @return A departmentTask which can be used to get a list of Department details from the DB in a separate
     * thread.
     */
    public Task<List<Department>> getDepartmentsTask(String additionalQuery, final String... params) {

        final String query = "SELECT v_dept_name, v_building_name  from t_department " + additionalQuery;

        Task<List<Department>> departmentTask = new Task<>() {

            @Override
            protected List<Department> call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query, params);

                //each item in the list is a single Department details
                List<Department> list = new ArrayList<>();

                /*
                v_dept_name is the primary key, total items in the map will always be equal to no of
                v_dept_name retrieved
                 */
                for (int i = 0; i < map.get("v_dept_name").size(); i++) {

                    Department department = new Department();

                    department.setDeptName(map.get("v_dept_name").get(i));
                    department.setBuildingName(map.get("v_building_name").get(i));

                    //a single department details is added to the list
                    list.add(department);
                }

                //a list of Department details
                return list;
            }
        };
        return departmentTask;
    }

    /**
     * This method is used to get a addDepartmentToDatabaseTask which is used to add a single department to the DB.
     *
     * @param department The department to be added to the DB.
     * @return A addDepartmentToDatabaseTask instance which is used to add a single department to DB in a separate
     * thread.
     */
    public Task<Integer> getAddDepartmentToDatabaseTask(final Department department) {
        Task<Integer> addDepartmentToDatabaseTask = new Task<>() {
            @Override
            protected Integer call() {

                final String sql = "INSERT INTO t_department(v_dept_name, v_building_name) " +
                        "VALUES(?, ?)";

                //holds the status of insertion of department to the DB, i.e success or failure
                int tDepartmentStatus = databaseHelper.insert(sql, department.getDeptName()
                        , department.getBuildingName());

                /*returns an integer holding the success or failure status*/
                if (tDepartmentStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tDepartmentStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_ALREADY_EXIST_ERROR;
                }

            }
        };
        return addDepartmentToDatabaseTask;
    }

    /**
     * This method is used to get a updateDepartmentTask which is used to edit a single department in the DB.
     *
     * @param department The department to be edited.
     * @return A updateDepartmentTask instance which is used to edit a single department in the DB in a separate
     * thread.
     */
    public Task<Integer> getUpdateDepartmentTask(final Department department) {
        Task<Integer> updateDepartmentTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "UPDATE t_department SET v_building_name=? WHERE v_dept_name=?";

                //holds the status of updation of department in the DB, i.e success or failure
                int tDepartmentStatus = databaseHelper.updateDelete(sql, department.getBuildingName()
                        , department.getDeptName());

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tDepartmentStatus == DATABASE_ERROR) {
                    return DATABASE_ERROR;
                } else if (tDepartmentStatus == SUCCESS) {
                    return SUCCESS;
                } else {
                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return updateDepartmentTask;
    }

    /**
     * This method is used to get a deleteDepartmentTask which is used to delete a single department in the DB.
     *
     * @param param Dept name of the department to be deleted.
     * @return A deleteDepartmentTask instance which is used to delete a single department in the DB in a separate
     * thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getDeleteDepartmentTask(final String param) {

        Task<Integer> deleteDepartmentTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "DELETE FROM t_department WHERE v_dept_name=?";

                //holds the status of deletion of department in the DB, i.e success or failure
                int tDepartmentStatus = databaseHelper.updateDelete(sql, param);

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tDepartmentStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tDepartmentStatus == SUCCESS) {

                    return SUCCESS;
                } else if (tDepartmentStatus == DATA_DEPENDENCY_ERROR) {

                    return DATA_DEPENDENCY_ERROR;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return deleteDepartmentTask;
    }

    /**
     * This method is used to get a single departmentsCountTask object which is used to get total no of
     * Departments in the DB.
     *
     * @return A departmentsCountTask object which is used to get the total no. of departments in the DB in a separate
     * thread.
     */
    public Task<Integer> getDepartmentsCountTask(String additionalQuery, String... params) {

        final String query = "SELECT v_dept_name FROM t_department " + additionalQuery;

        Task<Integer> departmentsCountTask = new Task<>() {

            @Override
            protected Integer call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query, params);

                /*
                v_dept_name is the primary key, so total count will always be equal to the no of v_dept_name
                retrieved
                 */
                return map.get("v_dept_name").size();
            }
        };
        return departmentsCountTask;
    }
}
