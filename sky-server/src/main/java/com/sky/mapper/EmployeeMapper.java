package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Insert("insert into employee(name,username,password,phone,sex,id_Number,status,create_Time,update_Time,create_User,update_User)"+
            "values(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(OperationType.INSERT)//自动填充careteTime,updateTime,createUser,updateUser
    void insert(Employee pojo);

    long countTotal(EmployeePageQueryDTO dto);

    List<Employee> pageQuery(EmployeePageQueryDTO dto);

    Page<Employee> selectPage(EmployeePageQueryDTO dto);


    /**
     * 更新员工信息
     * @param updatePojo
     */
    @AutoFill(OperationType.UPDATE)
    void update(Employee updatePojo);


    /**
     * 通过id查询员工信息
     * @param id
     * @return
     */
    @Select("select * from employee where id =#{id}")
    Employee selectById(int id);
}












