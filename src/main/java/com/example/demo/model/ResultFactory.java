package com.example.demo.model;


/**
 * 返回结果工具类  用于简化代码 用法示例：return ResultFactory.success();
 *                                        return ResultFactory.success(data);
 *                                        return ResultFactory.success(ResultEnum.HTTP_SUCCESS,data);
 *                                        return ResultFactory.error();
 *                                        return ResultFactory.error(ResultEnum.FILE_NOT_FOUND);
 *
 *@author 李挺 【fengkuangdejava@outlook.com】
 *@date 2018/10/8 10:22
 */
public class ResultFactory {

    public static Result success(){
        Result result = new Result();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
		result.setData(null);
        return result;
    }

    public static Result success(Object data){
        Result result = new Result();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }
    public static Result success(ResultEnum resultEnum,Object data){
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        result.setData(data);
        return result;
    }
    public static Result error(){
        Result result = new Result();
        result.setCode(ResultEnum.UNKNOWN_ERROR.getCode());
        result.setMsg(ResultEnum.UNKNOWN_ERROR.getMsg());
        return result;
    }
    public static Result error(int code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    public static Result error(int code,String msg,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
    public static Result error(ResultEnum resultEnum){
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        result.setData(null);
        return result;
    }

    public static Result info(ResultEnum resultEnum,Object object){
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        result.setData(object);
        return result;
    }

    public static Result info(ResultEnum resultEnum){
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        result.setData(null);
        return result;
    }
}
