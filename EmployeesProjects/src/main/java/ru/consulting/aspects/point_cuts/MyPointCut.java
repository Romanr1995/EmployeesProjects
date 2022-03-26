package ru.consulting.aspects.point_cuts;

import org.aspectj.lang.annotation.Pointcut;

public class MyPointCut {

    @Pointcut("execution( * show*(..))")
    public void allShowMethods() {

    }

    @Pointcut("execution( public * ru.consulting.service.*.save*(*))")
    public void afterSaveEntity() {

    }

    @Pointcut("execution( public * ru.consulting.service.*.add*(*))")
    public void afterAddEntity() {

    }

    @Pointcut("execution( public * ru.consulting.service.PositionService.saveNewOrUpdate(*))")
    public void positionSaveNewOrUpdate() {

    }

}
