package com.example.demo.exception;


import com.example.demo.model.Result;
import com.example.demo.model.ResultEnum;
import com.example.demo.model.ResultFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author liting fengkuangdejava@outlook.com
 * @Description  @ControllerAdvice  声明此类用于捕获异常
 */
@ControllerAdvice
public class DefaultExceptionHandler {

	public static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);


	/**
	 * @Author liting fengkuangdejava@outlook.com
	 * @Description 异常判断及业务处理的逻辑方法 @ExceptionHandler(value=Exception.class)指定要捕获的异常类型  @ResponseBody因为要给接口请求方信息 所以加个这个 以JSON格式返回给请求发起方
	 */
	@ExceptionHandler(value=Exception.class)
	@ResponseBody
	public Result handlerException(Exception e){
		logger.error("exception={}",e.getMessage());
		if(e instanceof DefaultException){
			DefaultException defaultException = (DefaultException)e;
			return  ResultFactory.error(defaultException.getCode(),defaultException.getMessage());
		}if(e instanceof HttpRequestMethodNotSupportedException){
			return  ResultFactory.error(ResultEnum.REQUST_METHOD_ERROR);
		}if(e instanceof MissingServletRequestParameterException){
			return  ResultFactory.error(ResultEnum.PARAM_VALIDATE_ERROR);
		}else{
			return  ResultFactory.error();
		}
	}

}
