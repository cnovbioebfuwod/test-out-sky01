package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
//Api代表是一个功能组，tags:加上中文名称
@Api(tags = "员工管理接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        //ctrl+alt+B
        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }
    public static void main(String[] args){
        String password ="123456";
        byte[] bytes=password.getBytes();
        String encrypted= DigestUtils.md5DigestAsHex(bytes);
        System.out.println("encrypted=" + encrypted);
    }

    @PostMapping
    @ApiOperation("新增员工")
    public Result addEmployee(@RequestBody EmployeeDTO dto){
        //打印传过来的参数值
        //sl4j日志规范，{}占位符,{}个数与顺序要保持一致
        log.info("新增员工参数：{}",dto);
        //调用业务层
        employeeService.save(dto);
        //相应结果
        return Result.success();
    }

    @GetMapping("page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO dto){
        log.info("员工分页查询:{}",dto);
        PageResult pageResult=employeeService.pageQuery(dto);
        return Result.success(pageResult);
    }

//启用或禁用员工账号
    @PostMapping("/status/{status}")
    @ApiOperation("启用或禁用员工账号")
    public Result startOrStop(@PathVariable int status,long id){
        log.info("启用或禁用员工账号：status={},id={}",status,id);
//        调用业务层处理
        employeeService.startOrStop(status,id);
//        响应结果
        return Result.success();
    }

    /**
     * 通过id查询员工信息
     * @param id
     * @return
     */
    @ApiOperation("通过id查询员工信息")
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable int id){
        log.info("通过id查询员工信息:{}",id);
        Employee employee=employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 更新员工信息
     * @param dto
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody EmployeeDTO dto){
        //调用业务层更新员工信息
        log.info("修改员工信息：{}",dto);
        employeeService.update(dto);
        return Result.success();
    }

}
