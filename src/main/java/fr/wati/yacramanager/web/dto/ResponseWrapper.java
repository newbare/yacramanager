package fr.wati.yacramanager.web.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ResponseWrapper<DATA> implements Serializable{

	private long totalCount;
	private DATA result;
	
	
	
	public ResponseWrapper(DATA result) {
		this.result = result;
	}
	public ResponseWrapper(DATA result,long totalCount) {
		this.result = result;
		this.totalCount=totalCount;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public DATA getResult() {
		return result;
	}
	public void setResult(DATA result) {
		this.result = result;
	}
	
	
}
