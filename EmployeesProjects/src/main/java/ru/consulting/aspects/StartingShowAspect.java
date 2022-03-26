package ru.consulting.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class StartingShowAspect {

    @Before("ru.consulting.aspects.point_cuts.MyPointCut.allShowMethods()")
    public void beforeShowMethods(JoinPoint joinPoint) {
        System.out.println("Вызван метод: " + joinPoint.getSignature().getName());
    }
}
