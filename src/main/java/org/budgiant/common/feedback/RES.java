package org.budgiant.common.feedback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 包含数据的统一返回封装类
 *
 * @author nkxrb
 * @since 2019/10/01
 */
public class RES<T> implements Serializable {

    /**
     * 成功
     */
    private static final int SUCCESS = 1;

    /**
     * 失败
     */
    private static final int FAIL = 0;

    /**
     * 错误
     */
    private static final int ERROR = -1;

    /**
     * 错误码
     */
    private int code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 列表数据
     */
    private List<T> rows;

    /**
     * 私有化无参构造，！！注意，jackson的反序列化需要无参构造函数，因此必须有无参构造函数
     */
    private RES() {
    }

    private RES(int code, String msg, long total, List<T> rows) {
        this.code = code;
        this.msg = msg;
        this.total = total;
        this.rows = rows;
    }

    private RES(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 校验是否成功
     *
     * @return boolean
     */
    public boolean isSuccess() {
        return code == SUCCESS;
    }

    /**
     * 返回操作成功信息
     *
     * @return RES对象
     */
    public static RES success() {
        return new RES(SUCCESS, "操作成功");
    }

    /**
     * 返回操作成功信息
     *
     * @param msg 提示信息
     * @return RES对象
     */
    public static RES success(String msg) {
        return new RES(SUCCESS, msg);
    }

    /**
     * 返回操作失败信息
     *
     * @return RES对象
     */
    public static RES fail() {
        return new RES(FAIL, "操作失败");
    }

    /**
     * 返回操作失败信息
     *
     * @param msg 提示信息
     * @return RES对象
     */
    public static RES fail(String msg) {
        return new RES(FAIL, msg);
    }

    /**
     * * 错误返回
     *
     * @param code 错误代码
     * @param msg  提示信息
     * @return RES对象
     */
    public static RES error(int code, String msg) {
        return new RES(0 - code, msg);
    }

    /**
     * 错误返回
     *
     * @param msg 数据列表
     * @return RES对象
     */
    public static RES error(String msg) {
        return new RES(ERROR, msg);
    }

    /**
     * 成功返回数据集合，仅包含一个对象
     *
     * @param item 返回的对象
     * @param <T>  实体类型
     * @return RES对象
     */
    public static <T> RES<T> single(T item) {
        List<T> rows = new ArrayList<>();
        rows.add(item);
        return new RES<T>(SUCCESS, "SUCCESS", rows.size(), rows);
    }

    /**
     * 成功返回数据列表,支持分页
     *
     * @param rows  数据列表
     * @param total 数据总数
     * @param <T>   实体类型
     * @return RES对象
     */
    public static <T> RES<T> listForPage(List<T> rows, long total) {
        return new RES<T>(SUCCESS, "SUCCESS", total, rows);
    }

    /**
     * 成功返回数据列表
     *
     * @param rows 数据列表
     * @param <T>  实体类型
     * @return RES对象
     */
    public static <T> RES<T> list(List<T> rows) {
        return new RES<T>(SUCCESS, "SUCCESS", rows.size(), rows);
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "code:" + code + ",msg:" + msg + ",total:" + total + ",rows:" + rows;
    }

}
