package com.nekolr.index.common;

/**
 * 控制器基类
 *
 * @author nekolr
 */
public abstract class BaseController {

    public <T> ResultBean<T> assembleResultOfSuccess() {
        return new ResultBean<>();
    }

    public <T> ResultBean<T> assembleResultOfSuccess(String message) {
        return new ResultBean<>(message);
    }

    public <T> ResultBean<T> assembleResultOfSuccess(T data) {
        return new ResultBean<>(data);
    }

    public <T> ResultBean<T> assembleResultOfSuccess(T data, String message) {
        return new ResultBean<>(data, message);
    }

    public <T> ResultBean<T> assembleResultOfFail(Throwable e) {
        return new ResultBean<>(e);
    }

    public <T> ResultBean<T> assembleResultOfFail(String message) {
        return new ResultBean<>(message, false);
    }
}
