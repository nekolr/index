package com.nekolr.index.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 控制层异常处理类（也可以自定义切面类来处理）
 *
 * @author nekolr
 */
@ControllerAdvice
@Slf4j
@ResponseBody
public class ControllerExceptionHandler {

    /**
     * 不要使用最大的异常来处理，要细化，这是错误的处理方式
     */
    @ExceptionHandler(Throwable.class)
    public ResultBean<?> handleException(Throwable e) {
        return new ResultBean<>(e);
    }
}
