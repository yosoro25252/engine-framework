package com.example;

import com.example.impl.SimpleRequest;
import com.example.impl.SimpleResponse;
import com.yosoro25252.engine.framework.flowcontrol.FlowContainer;
import com.yosoro25252.engine.framework.pojo.Context;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        Context context = new Context();
        FlowContainer<SimpleRequest, SimpleResponse> flowContainer = (FlowContainer) applicationContext.getBean("flowContainer");
        SimpleRequest request = new SimpleRequest();
        request.setStrategy("abc");
        request.setUuid("xyz");
        request.setUid("123");
        SimpleResponse response = flowContainer.process(request);
        System.out.println(response.getCode());
        System.out.println(response.getMessage());
        System.out.println(response.getScore());
        System.out.println("finish");
    }
}