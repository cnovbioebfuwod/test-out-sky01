package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO dto);

    /**
     * 员工分页查询
     * @param dto
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO dto);

    /**
     * 启用或禁用员工账号
     * @param status
     * @param id
     */

    void startOrStop(int status, long id);

    /**
     * 通过id查询员工信息
     * @param id
     * @return
     */
    Employee getById(int id);

    /**
     * 更新员工信息
     * @param dto
     */
    void update(EmployeeDTO dto);

    /**
     * 新增员工
     * @param dto
     */

}
