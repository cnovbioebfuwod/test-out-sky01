package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        //把前端传过来的密码加密，前端传过来是明文，数据库是密文
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param dto
     */
    @Override
    public void save(EmployeeDTO dto){
//        操作数据库要用实体类
        Employee pojo=new Employee();
//        复制属性的值，属性名
//        p1:源把pi复制给p2
        BeanUtils.copyProperties(dto,pojo);

        //业务处理
//        手机号码是否为非法的11位
//        身份证号码为合法的18位
//        账号username必须为唯一
//        默认密码,得先使用mds加密
        String encryptedPwd=DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());
        pojo.setPassword(encryptedPwd);
        pojo.setStatus(StatusConstant.ENABLE);  //魔鬼数字
//        辅助字段的值
//        pojo.setCreateTime(LocalDateTime.now());
//        pojo.setUpdateTime(LocalDateTime.now());

        LocalDateTime now=LocalDateTime.now();
        pojo.setCreateTime(now);
        pojo.setUpdateTime(now);

//        数字后l long类型，数字后d double,f:float类型
//        Long loginUserId= BaseContext.getCurrentId();
//        log.info("EmployyeeServiceImpol：当前登录用户的id为")
        pojo.setCreateUser(10l);//TODO 后续修改为当前登录用户id
        pojo.setUpdateUser(10l);//TODO 后续修改为当前登录用户id
        //调用mapper存入数据库表中
        employeeMapper.insert(pojo);


    }

}
