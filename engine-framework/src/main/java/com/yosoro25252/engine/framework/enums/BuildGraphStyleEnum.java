package com.yosoro25252.engine.framework.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BuildGraphStyleEnum {

    FROM_PARAM(0, "from_param", "根据结点出入参构建图"),
    FROM_NEIGHBOURS(1, "from_neighbours", "根据结点上下游关系构建图");

    private int code;

    private String name;

    private String desc;

}
