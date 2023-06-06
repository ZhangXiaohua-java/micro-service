package io.element.vo;

import java.io.Serializable;
import java.util.HashMap;

public class Result extends HashMap<String, Object> implements Serializable {


    private static final String SUCCESS_CODE = "200";

    private static final String FAIL_CODE = "500";


    private static final String SUCCESS_MESSAGE = "OK";

    private static final String FAIL_MESSAGE = "error";


    public Result() {
    }


    public static Result success() {
        Result result = new Result();
        result.put("code", SUCCESS_CODE);
        result.put("msg", SUCCESS_MESSAGE);
        return result;
    }

    public static Result success(String code, String msg) {
        Result result = new Result();
        result.put("code", code);
        result.put("msg", msg);
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.put("code", FAIL_CODE);
        result.put("msg", FAIL_MESSAGE);
        return result;
    }

    public static Result error(String code, String msg) {
        Result result = new Result();
        result.put("code", code);
        result.put("msg", msg);
        return result;
    }


    public Result put(String key, Object val) {
        super.put(key, val);
        return this;
    }





}
