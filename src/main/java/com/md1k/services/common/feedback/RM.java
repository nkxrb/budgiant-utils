package com.md1k.services.common.feedback;

import java.io.Serializable;

/**
 * 提示消息统一返回封装类
 *
 * @author vvk
 * @since 2019/8/21
 */
public class RM implements Serializable {


    /**
     * 成功
     */
    static final int YES = 1;

    /**
     * 失败
     */
    static final int NO = 0;

    /**
     * 错误
     */
    static final int ERROR = -1;

    /**
     * 错误码
     */
    private int code;
    /**
     * 提示信息
     */
    private String msg;

    /**
     * 私有化无参构造，！！注意，jackson的反序列化需要无参构造函数，因此必须有无参构造函数
     */
    private RM() {
    }

    private RM(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 校验是否成功
     *
     * @return boolean
     */
    public boolean checkYes() {
        return code == YES;
    }

    public static RM yes(String msg) {
        return new RM(YES, msg);
    }

    public static RM no(String msg) {
        return new RM(NO, msg);
    }

    public static RM error(String msg) {
        return new RM(ERROR, msg);
    }

    public static RM error(int errorCode, String msg) {
        return new RM(errorCode, msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "code:" + code + ",msg:" + msg;
    }
}
