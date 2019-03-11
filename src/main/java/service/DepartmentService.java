package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Department;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 *
 * @author Avik Sarkar
 */
public class DepartmentService {

    private DatabaseHelper databaseHelper;

    public DepartmentService(){
        databaseHelper = new DatabaseHelper();
    }

    public Task<List<Department>> getDepartmentsTask(String additionalQuery, final String ...params){

        final String query = "SELECT v_dept_name, v_building_name  from t_department " + additionalQuery;

        Task<List<Department>> departmentTask = new Task<>() {
            @Override
            protected List<Department> call(){
                Map<String, List<String>> map = databaseHelper.execQuery(query, params);
                List<Department> list = new ArrayList<>();
                for(int i = 0; i < map.get("v_dept_name").size(); i ++){

                    Department department = new Department();
                    department.setDeptName( map.get("v_dept_name").get(i));
                    department.setBuildingName(map.get("v_building_name").get(i));

                    list.add(department);
                }
                return list;
            }
        };
        return departmentTask;
    }

    public Task<Integer>  getAddDepartmentToDatabaseTask(final Department department){
        Task<Integer> addDepartmentToDatabaseTask = new Task<>() {
            @Override
            protected Integer call() {

                final String sql = "INSERT INTO t_department(v_dept_name, v_building_name) " +
                        "VALUES(?, ?)";

                int tDepartmentStatus = databaseHelper.insert(sql, department.getDeptName()
                        , department.getBuildingName());

                if(tDepartmentStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;
                else if(tDepartmentStatus == SUCCESS)
                    return SUCCESS;
                else
                    return DATA_ALREADY_EXIST_ERROR;

            }
        };
        return addDepartmentToDatabaseTask;
    }


    public Task<Integer>  getUpdateDepartmentTask(final Department department){
        Task<Integer> updateDepartmentTask = new Task<>() {
            @Override
            protected Integer call() {

                final String sql = "UPDATE t_department SET v_building_name=? WHERE v_dept_name=?";

                int tDepartmentStatus = databaseHelper.updateDelete(sql, department.getBuildingName()
                        , department.getDeptName());

                if(tDepartmentStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;
                else if(tDepartmentStatus == SUCCESS)
                    return SUCCESS;
                else if(tDepartmentStatus == DATA_DEPENDENCY_ERROR)
                    return DATA_DEPENDENCY_ERROR;
                else
                    return DATA_INEXISTENT_ERROR;
            }
        };
        return updateDepartmentTask;
    }

    @SuppressWarnings("Duplicates")
    public Task<Integer>  getDeleteDepartmentTask(final String param){
        Task<Integer> deleteDepartmentTask = new Task<>() {
            @Override
            protected Integer call() {

                final String sql = "DELETE FROM t_department WHERE v_dept_name=?";

                int tDepartmentStatus = databaseHelper.updateDelete(sql, param);

                if(tDepartmentStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;
                else if(tDepartmentStatus == SUCCESS)
                    return SUCCESS;
                else if(tDepartmentStatus == DATA_DEPENDENCY_ERROR)
                    return DATA_DEPENDENCY_ERROR;
                else
                    return DATA_INEXISTENT_ERROR;
            }
        };
        return deleteDepartmentTask;
    }

    public Task<Integer> getDepartmentsCountTask(){

        final String query = "SELECT v_dept_name FROM t_department";

        Task<Integer> departmentsCountTask = new Task<>() {
            @Override
            protected Integer call(){

                Map<String, List<String>> map = databaseHelper.execQuery(query);
                return map.get("v_dept_name").size();
            }
        };
        return departmentsCountTask;
    }

}
