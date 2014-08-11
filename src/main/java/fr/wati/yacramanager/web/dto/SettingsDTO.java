package fr.wati.yacramanager.web.dto;

import fr.wati.yacramanager.beans.Settings;

public class SettingsDTO {

	private Long id;
	private String key;
	private String value;
	private String description;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Settings toSettings(){
		return toSettings(new Settings());
	}
	
	public Settings toSettings(Settings settings){
		settings.setKey(getKey());
		settings.setValue(getValue());
		settings.setDescription(getDescription());
		return settings;
	}
}
