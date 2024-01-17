package com.yosoro25252.engine.framework;

import com.yosoro25252.engine.framework.pojo.Context;
import com.yosoro25252.engine.framework.processors.DAGContainerProcessor;
import com.yosoro25252.engine.framework.processors.DAGNodeProcessor;
import com.yosoro25252.engine.framework.processors.IProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        Context context = new Context();
//        IProcessor processor = (IProcessor) applicationContext.getBean("simpleFlowProcessor");
//        processor.doProcess(context);
//        processor.fallback(context);

//        DAGNodeProcessor dagNodeProcessor1 = (DAGNodeProcessor) applicationContext.getBean("simpleDAGProcessorV1");
//        dagNodeProcessor1.doProcess(context);
//        dagNodeProcessor1.fallback(context);
//        DAGNodeProcessor dagNodeProcessor2 = dagNodeProcessor1.getDownstreamNodeList().get(0);
//        dagNodeProcessor2.doProcess(context);
//        dagNodeProcessor2.fallback(context);

        DAGContainerProcessor graphContainerProcessor = (DAGContainerProcessor) applicationContext.getBean("simpleGraph");
        graphContainerProcessor.process(context);
        System.out.println("finish");
    }
}