package com.yosoro25252.engine.framework.processors;

import com.yosoro25252.engine.framework.services.IMonitorService;
import lombok.Setter;

/**
 * @author：yosoro25252
 * @date：2024/1/18 17:21
 * @desc: 基础processor，所有processor的父类
 */
public abstract class BaseProcessor implements IProcessor {

    @Setter
    protected IMonitorService monitorService;

}
