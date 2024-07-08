package com.iti.aspect;

import com.iti.annotation.My;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component//注册到
@Aspect
public class MyAspect {
    /**
     * 定义切入点
     * 任意返回值 com.iti.controller包下
     */
    @Pointcut("execution(* com.iti.controller.*Controller.*(..))")
    public void myPointCut(){

    }
    @Pointcut("@annotation(com.iti.annotation.My)")
    public void MyAnnotated(){

    }

//    前置通知
    @Before(value = "myPointCut()")
    public void myAdvice(JoinPoint joinPoint)

    {
        //切入点
        System.out.println("进入before");
        MethodSignature methodSignature=(MethodSignature) joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        My annotation=method.getAnnotation(My.class);
        //注解中的属性value
        String value=annotation.value();
        System.out.println("This is"+value);

        Object[] args=joinPoint.getArgs();
        Object arg=args[0];
        System.out.println("收到参数值为：" +arg);
        System.out.println(method.getName());

    }
    @After(value = "myPointCut()")
    public void afterAdvice(JoinPoint joinPoint){
        System.out.println("进入before");
        System.out.println(joinPoint.getSignature());
    }
}
