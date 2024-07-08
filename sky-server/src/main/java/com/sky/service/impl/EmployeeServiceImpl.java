package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.BaseException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sun.plugin2.main.server.LiveConnectSupport.getResult;

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

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO dto){
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        Page<Employee> page=employeeMapper.selectPage(dto);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 启用或禁用员工账号
     *
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(int status, long id) {
//        业务处理
        Employee updatePojo=new Employee();
        updatePojo.setId(id);
        updatePojo.setStatus(status);
        updatePojo.setUpdateTime(LocalDateTime.now()) ;
        updatePojo.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(updatePojo);

    }

    /**
     * 通过id查询员工信息
     *
     * @param id
     * @return
     */
    @Override
    public Employee getById(int id) {
        //调用mapper来查询
        Employee employee=employeeMapper.selectById(id);

        if (null==employee){
            throw new BaseException("员工信息不存在！");
        }
        //设置密码为null,脱敏处理
        employee.setPassword(null);
        return employee;

    }

    @Override
    public void update(EmployeeDTO dto){
        Employee updatePojo=new Employee();
        BeanUtils.copyProperties(dto,updatePojo);
        updatePojo.setUpdateTime(LocalDateTime.now());
        // 修改者Id
        updatePojo.setUpdateUser(BaseContext.getCurrentId());
        // 执行更新
        employeeMapper.update(updatePojo);
    }

//    @Override
//    public PageResult pageQuery(EmployeePageQueryDTO dto) {
//        if(dto.getName() !=null&& dto.getName().trim().length()>0)
//        PageResult pageResult=new PageResult(0,new ArrayList());
//        if(ObjectUtils.isNotEmpty(dto.getName())) {
//            dto.setName("%"+dto.getName()+"%");
//        }else {
//            dto.setName(null);
//        }
//        long total =employeeMapper.countTotal(dto);
//        pageResult.setTotal(total);
//        int skip=(dto.getPage()-1)*dto.getPageSize();
//        if(total>skip){
//            List<Employee> employeeList=employeeMapper.pageQuery(dto);
//
//            pageResult.setRecords(employeeList);
//        }
//            return pageResult;
//
//    }

}
