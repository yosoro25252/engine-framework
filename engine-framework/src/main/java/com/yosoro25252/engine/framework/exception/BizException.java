package com.yosoro25252.engine.framework.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author：yosoro25252
 * @date：2024/1/19 3:44
 * @desc:
 */
@Data
@AllArgsConstructor
public class BizException extends RuntimeException {

    private int errorCode;

    private Exception exception;

    private String errorMessage;

    private String showMessage;

}
