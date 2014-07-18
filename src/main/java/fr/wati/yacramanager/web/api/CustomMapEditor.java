/**
 * 
 */
package fr.wati.yacramanager.web.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Rachid Ouattara
 *
 */
public class CustomMapEditor extends org.springframework.beans.propertyeditors.CustomMapEditor {

	
	/**
	 * @param mapType
	 */
	public CustomMapEditor(Class<? extends Map> mapType) {
		super(mapType);
	}

	
	
	/**
	 * @param mapType
	 * @param nullAsEmptyMap
	 */
	public CustomMapEditor(Class<? extends Map> mapType, boolean nullAsEmptyMap) {
		super(mapType, nullAsEmptyMap);
	}



	/* (non-Javadoc)
	 * @see org.springframework.beans.propertyeditors.CustomMapEditor#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		if(value instanceof String){
			String stringRepresentation=String.valueOf(value);
			if(StringUtils.isNotEmpty(stringRepresentation) && StringUtils.startsWith(stringRepresentation, "{") && StringUtils.endsWith(stringRepresentation, "}") ){
				Map<String, String> map=new HashMap<>();
				String[] keyValuesArray=StringUtils.split(StringUtils.substring(stringRepresentation,1,stringRepresentation.length()-1).replace("\"", ""), ":");
				for (int i = 0; i < keyValuesArray.length-1; i++) {
					map.put(keyValuesArray[i], keyValuesArray[i+1]);
				}
				
				super.setValue(map);
			}else {
				super.getValue();
			}
		}
	}

	
}
