package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/*自动填充切面类*/
@Component
@Aspect
@Slf4j
public class AutoFillAspect {
    /*定义切入点@AutoFill注解
     * execution(* com.sky.mapper.*.*(..)) && */
    @Pointcut("@annotation(com.sky.annotation.AutoFill)")
    public void  autoFillPointCut() {
    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        /*获取参数
        获取操作类型
        * 通过参数获取set方法
        * 获取当前系统时间，当前用户id
        * 调用方法*/
        Object[] args=joinPoint.getArgs();
        for (Object arg : args) {
            Class<?> cls=arg.getClass();
            String pkgName= cls.getPackage().getName();
            if ("com.sky.entity".equals(pkgName)) {
//                System.out.println("com.sky.entity:"+cls);
                MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
                Method method=methodSignature.getMethod();
                AutoFill autoFill1= method.getAnnotation(AutoFill.class);
                OperationType operationType = autoFill1.value();
//                System.out.println(operationType);
//                获取
                try{
                    Method setCreateTime = cls.getDeclaredMethod("setCreateTime", LocalDateTime.class);
                    Method setUpdateTime = cls.getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                    Method setCreateUser = cls.getDeclaredMethod("setCreateUser", Long.class);
                    Method setUpdateUser = cls.getDeclaredMethod("setUpdateUser", Long.class);

                    LocalDateTime now=LocalDateTime.now();
                    Long loginUserId = BaseContext.getCurrentId();
                    switch (operationType) {
                        case INSERT:
                            setCreateTime.invoke(arg,now);
                            setUpdateTime.invoke(arg,now);
                            setCreateUser.invoke(arg,loginUserId);
                            setUpdateUser.invoke(arg,loginUserId);
                            break;
                        case UPDATE:
                            setUpdateTime.invoke(arg,now);
                            setUpdateUser.invoke(arg,loginUserId);
                            break;
                    }
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

