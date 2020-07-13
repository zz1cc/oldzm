package com.zm.platform.yw.result;

import com.zm.platform.common.constant.StatusCode;

/**
 * 返回基类
 * 全部使用 R返回数据
 * @author Administrator
 *
 */
public class BaseResult<T> {

	//返回数据的提示
    private String desc;
	//返回数据的值
    private T data;
	//返回数据的状态
    private int status;
    
    public BaseResult(){
    	this.status = StatusCode.SUCCESS;
    	this.desc = "success";
    }
    
    public BaseResult(T data){
    	this();
    	this.data = data;
    }
    
    public BaseResult(int status, String desc, T data){
    	this.status = status;
    	this.desc = desc;
    	this.data = data;
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
		return "BaseResult [desc=" + desc + ", data=" + data + ", status=" + status + "]";
	}
    
}
