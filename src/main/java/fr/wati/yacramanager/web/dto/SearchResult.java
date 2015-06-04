package fr.wati.yacramanager.web.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class SearchResult {

	private String textToSearch;
	
	private Map<String,List<?>> result;
	
	private long resultCount;
	
	public SearchResult() {
	}

	public String getTextToSearch() {
		return textToSearch;
	}

	public void setTextToSearch(String textToSearch) {
		this.textToSearch = textToSearch;
	}

	public Map<String,List<?>> getResult() {
		if(result==null){
			result=new HashMap<>();
			result.put("employe", Lists.newArrayList());
			result.put("project", Lists.newArrayList());
			result.put("client", Lists.newArrayList());
		}
		return result;
	}

	public void setResult(Map<String,List<?>> result) {
		this.result = result;
	}

	public long getResultCount() {
		return resultCount;
	}

	public void setResultCount(long resultCount) {
		this.resultCount = resultCount;
	}

}
