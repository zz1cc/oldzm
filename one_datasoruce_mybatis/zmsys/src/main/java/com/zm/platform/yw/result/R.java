package com.zm.platform.yw.result;


import com.zm.platform.common.constant.StatusCode;

/**
 * 返回值
 * @author Administrator
 */
public class R<T> {
	//返回数据的提示
	private String desc;
	//返回数据的值
	private T data;
	//返回数据的状态
	private int status;

	private R(){

	}

	private R(int status, String desc, T data){
		this.status = status;
		this.desc = desc;
		this.data = data;
	}

	public static R error() {
		return new R(StatusCode.ERROR, null, null);
	}

	public static R error(String desc) {
		return new R(StatusCode.ERROR, desc, null);
	}

	public static R error(String desc, Object data) {
		return new R(StatusCode.ERROR, desc, data);
	}

	public static R success() {
		return new R(StatusCode.SUCCESS, null, null);
	}

	public static R success(String desc) {
		return new R(StatusCode.SUCCESS, desc, null);
	}

	public static R success(Object data) {
		return new R(StatusCode.SUCCESS, null, data);
	}

	public static R success(String desc, Object data) {
		return new R(StatusCode.SUCCESS, desc, data);
	}

	public static R result(int status, String desc, Object data) {
		return new R(status, desc, data);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "R{" +
				"desc='" + desc + '\'' +
				", data=" + data +
				", status=" + status +
				'}';
	}
}
