package com.byls.boat.enums;

/**
 * 
 * &#064;Description:  系统消息
 *
 */
public enum MessageEnum implements MessageType {

	SYSTEM_ERROR("-1", "系统异常"), SUCCESS("0", "操作成功"),FAIL("-99","操作失败");

	/**
	 * 错误类型码
	 */
	private String code;
	/**
	 * 错误类型描述信息
	 */
	private String message;

	/**
	 * 构造
	 *
	 */
	MessageEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
