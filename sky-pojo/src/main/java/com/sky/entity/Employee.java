package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //%03d 3代表输出数字的位数，0位数不足时补0，%d数字
        //字符串格式化输出
//        System.out.println(String.format("employee%03d",222));
        Employee employee=new Employee();

        Method[] declareMethods=employee.getClass().getDeclaredMethods();
        for(Method method:declareMethods){
            System.out.println(method.getName());
        }
        //获取setCreateTime方法
        Method setCreateTime=employee.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
        //获取当前系统时间
        LocalDateTime now=LocalDateTime.now();
        //
        setCreateTime.invoke(employee,now);
        System.out.println(employee);

        //调用getCreateTime()方法
        Method getCreateTime=employee.getClass().getDeclaredMethod("getCreateTime");

//        "class Student{private String name;private int age;}"

        Object result=getCreateTime.invoke(employee,null);
        System.out.println(result);
    }



}
