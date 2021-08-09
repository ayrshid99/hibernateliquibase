package com.example.hibernateddl;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StopWatch;

import javax.xml.bind.SchemaOutputResolver;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanUtilsExample {


    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {


        StopWatch stopWatch=new StopWatch();
        stopWatch.start("table");
        Map<String,Object> map=new HashMap<>();
        final Sam sam = new Sam();
        map.put("property1","25");
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeNanos());
        stopWatch.start("table1");


        BeanUtils.copyProperties(sam,map);
        System.out.println(sam.property1);

    stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeNanos());

    }





}


