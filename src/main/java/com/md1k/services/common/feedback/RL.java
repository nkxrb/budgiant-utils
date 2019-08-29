package com.md1k.services.common.feedback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 包含数据的统一返回封装类
 *
 * @author vvk
 * @since 2019/8/21
 */
public class RL<T> implements Serializable {

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
    private RL() {
    }

    private RL(int code, String msg, long total, List<T> rows) {
        this.code = code;
        this.msg = msg;
        this.total = total;
        this.rows = rows;
    }

    /**
     * 校验是否成功
     *
     * @return boolean
     */
    public boolean isSuccess() {
        return null != rows && rows.size() > 0;
    }

    /**
     * 错误返回
     *
     * @param code, String msg 数据列表
     * @param <T>   实体类型
     * @return RL<T>
     */
    public static <T> RL<T> error(int code, String msg) {
        return new RL<T>(code, msg, 0, null);
    }

    /**
     * 错误返回
     *
     * @param msg 数据列表
     * @param <T> 实体类型
     * @return RL<T>
     */
    public static <T> RL<T> error(String msg) {
        return new RL<T>(RM.NO, msg, 0, null);
    }

    /**
     * 成功返回数据列表,支持分页
     *
     * @param rows  数据列表
     * @param total 数据总数
     * @param <T>   实体类型
     * @return RL<T>
     */
    public static <T> RL<T> listForPage(List<T> rows, long total) {
        return new RL<T>(RM.YES, "SUCCESS", total, rows);
    }

    /**
     * 成功返回数据集合，仅包含一个对象
     *
     * @param item 返回的对象
     * @param <T>  实体类型
     * @return RL<T>
     */
    public static <T> RL<T> single(T item) {
        List<T> rows = new ArrayList<>();
        rows.add(item);
        return new RL<T>(RM.YES, "SUCCESS", rows.size(), rows);
    }

    /**
     * 成功返回数据列表
     *
     * @param rows 数据列表
     * @param <T>  实体类型
     * @return RL<T>
     */
    public static <T> RL<T> list(List<T> rows) {
        return new RL<T>(RM.YES, "SUCCESS", rows.size(), rows);
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
