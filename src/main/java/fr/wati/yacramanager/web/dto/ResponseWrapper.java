package fr.wati.yacramanager.web.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ResponseWrapper<DATA> implements Serializable{

	private long totalCount;
	private long startIndex;
	private long endIndex;
	private DATA result;
	
	
	
	public ResponseWrapper(DATA result) {
		this.result = result;
	}
	public ResponseWrapper(DATA result,long totalCount) {
		this.result = result;
		this.totalCount=totalCount;
	}
	
	
	public ResponseWrapper(DATA result,long totalCount, long startIndex, long endIndex) {
		super();
		this.totalCount = totalCount;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.result = result;
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
	public long getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	}
	public long getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(long endIndex) {
		this.endIndex = endIndex;
	}
	
	
}
