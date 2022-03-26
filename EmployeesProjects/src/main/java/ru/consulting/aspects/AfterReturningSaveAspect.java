package ru.consulting.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Aspect
public class AfterReturningSaveAspect {

    @AfterReturning(pointcut = "(ru.consulting.aspects.point_cuts.MyPointCut.afterSaveEntity() || " +
            "ru.consulting.aspects.point_cuts.MyPointCut.afterAddEntity()) && " +
            "!ru.consulting.aspects.point_cuts.MyPointCut.positionSaveNewOrUpdate()")
    public void afterSaveEntity(JoinPoint joinPoint) {
        final Object arg = joinPoint.getArgs()[0];

        if (arg instanceof List) {
            methodForList(arg);
        } else {
            String simpleName = arg.getClass().getSimpleName();
            if (simpleName.endsWith("Dto")) {
                simpleName = simpleName.substring(0, simpleName.length() - 3);
            }
            System.out.println("Добавлен новый entity: " + simpleName);
        }
    }


    @AfterReturning(pointcut = "ru.consulting.aspects.point_cuts.MyPointCut.positionSaveNewOrUpdate()",
            returning = "result")
    public void methodForPositionDto(ResponseEntity<Void> result) {
        if (result.getStatusCodeValue() == 201) {
            System.out.println("Добавлен новый entity: " + "Position");
        }
    }

    public void methodForList(Object o) {
        List<Object> objectList = (List<Object>) o;
        String simpleName = objectList.get(0).getClass().getSimpleName();
        if (simpleName.endsWith("Dto")) {
            simpleName = simpleName.substring(0, simpleName.length() - 3);
        }
        System.out.println("Добавлены " + objectList.size() + " новых entity " + simpleName);
    }
}
