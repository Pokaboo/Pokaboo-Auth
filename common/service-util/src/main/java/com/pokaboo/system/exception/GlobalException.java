package com.pokaboo.system.exception;

import com.pokaboo.common.result.ResultCodeEnum;

/**
 * 全局异常处理
 */
public class GlobalException extends RuntimeException {
    private Integer code;
    private String message;

    public GlobalException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public GlobalException(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "GuiguException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
