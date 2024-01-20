package com.example;

import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.processors.DAGContainerProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        Context context = new Context();
//        IProcessor processor = (IProcessor) applicationContext.getBean("simpleFlowProcessor");
//        processor.doProcess(context);

        DAGContainerProcessor graphContainerProcessor = (DAGContainerProcessor) applicationContext.getBean("simpleGraph");
        graphContainerProcessor.process(context);
        System.out.println("finish");
    }
}