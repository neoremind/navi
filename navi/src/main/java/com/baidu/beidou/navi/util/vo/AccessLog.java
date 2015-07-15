package com.baidu.beidou.navi.util.vo;

import com.baidu.beidou.navi.server.vo.RequestDTO;
import com.baidu.beidou.navi.server.vo.ResponseDTO;

public class AccessLog {

	private String fromIp;
	private String protocol;
	private String serviceIntfName;
	private RequestDTO request;
	private int requestByteSize;
	private ResponseDTO response;
	private long startTime;
	private long endTime;
	
	public AccessLog() {
		super();
	}

	public String getFromIp() {
		return fromIp;
	}

	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getServiceIntfName() {
		return serviceIntfName;
	}

	public void setServiceIntfName(String serviceIntfName) {
		this.serviceIntfName = serviceIntfName;
	}

	public RequestDTO getRequest() {
		return request;
	}

	public void setRequest(RequestDTO request) {
		this.request = request;
	}

	public int getRequestByteSize() {
		return requestByteSize;
	}

	public void setRequestByteSize(int requestByteSize) {
		this.requestByteSize = requestByteSize;
	}

	public ResponseDTO getResponse() {
		return response;
	}

	public void setResponse(ResponseDTO response) {
		this.response = response;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
}
