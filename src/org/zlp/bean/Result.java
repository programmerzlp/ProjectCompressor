package org.zlp.bean;

/**
 * Created with eclipse
 * 
 * @Description: 结果集
 * @author: programmer.zlp@qq.com
 * @Date: 2013年8月28日
 * @Time: 上午11:29:14
 * 
 */
public class Result<D> {

	private int code = 0;

	private String msg = null;

	private Exception exception = null;

	private D data = null;

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

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public D getData() {
		return data;
	}

	public void setData(D data) {
		this.data = data;
	}

}