package com.example.impl;

import com.yosoro25252.engine.framework.services.IMonitorService;

/**
 * @author：yosoro25252
 * @date：2024/1/18 15:35
 * @desc:
 */
public class SimpleMonitorService implements IMonitorService {
    @Override
    public void logEvent(String type, String subType, boolean success) {
        System.out.println(type + " " + subType + " " + success);
    }

    @Override
    public void logCost(String type, String subType, long cost) {
        System.out.println(type + " " + subType + " " + cost);
    }

    @Override
    public void logException(String type, Exception e) {
        System.out.println(type + " " + e.getMessage());
    }
}
