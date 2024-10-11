package com.byls.boat.common;

import lombok.ToString;

/**
 * 构造统一返回信息
 */
@ToString
public class ResponseResult<T> {

	/**
	 * 是否成功
	 */
	private boolean isSuccess;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 消息
	 */
	private String msg;
	/**
	 * 结果体
	 */
	private T data;

	public ResponseResult(T data) {
		super();
		this.data = data;
	}

	public ResponseResult(String code, String mesg) {
		super();
	}

	public ResponseResult(boolean isSuccess, String code, String msg) {
		super();
//		this.isSuccess = isSuccess;
		this.code = code;
		this.msg = msg;
	}

	public ResponseResult(String code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public ResponseResult(boolean isSuccess, String code, String msg, T data) {
		super();
//		this.isSuccess = isSuccess;
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public ResponseResult() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean success) {
		isSuccess = success;
	}
}
