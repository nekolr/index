package com.nekolr.index.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用接口返回实体
 *
 * @param <T> 返回数据类型
 * @author nekolr
 */
@Data
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功的状态
     */
    public static final Boolean SUCCESS_STATUS = true;

    /**
     * 失败的状态
     */
    public static final Boolean FAIL_STATUS = false;

    /**
     * 成功的消息
     */
    public static final String SUCCESS_MESSAGE = "success";

    /**
     * 消息
     */
    private String message = SUCCESS_MESSAGE;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态
     * <p>
     * {@link #SUCCESS_STATUS} or {@link #FAIL_STATUS}
     */
    private Boolean status = SUCCESS_STATUS;

    /**
     * 数据
     */
    private T data;

    public ResultBean() {
    }

    public ResultBean(T data) {
        this.data = data;
    }

    public ResultBean(Integer code) {
        this.code = code;
    }

    public ResultBean(String message) {
        this.message = message;
    }

    public ResultBean(T data, String message) {
        this.message = message;
        this.data = data;
    }

    public ResultBean(T data, String message, Integer code) {
        this(data, message);
        this.code = code;
    }

    public ResultBean(Throwable e) {
        this.message = e.getMessage();
        this.status = FAIL_STATUS;
    }

    public ResultBean(Throwable e, Integer code) {
        this.message = e.getMessage();
        this.status = FAIL_STATUS;
        this.code = code;
    }

}
