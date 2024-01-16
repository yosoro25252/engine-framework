package com.yosoro25252.engine.framework.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ColorEnum {

    WHITE(0, "white", "白色-未访问"),
    GRAY(1, "gray", "灰色-访问中"),
    BLACK(2, "black", "黑色-已访问");

    private int code;

    private String name;

    private String desc;

}
