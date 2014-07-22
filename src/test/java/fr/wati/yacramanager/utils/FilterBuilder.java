/**
 * 
 */
package fr.wati.yacramanager.utils;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * @author Rachid Ouattara
 *
 */
public class FilterBuilder {

	@Test
	public void testMapFilterString() throws Exception{
	List<? extends Filter> parse = fr.wati.yacramanager.utils.Filter.FilterBuilder.parse(IOUtils.toString(FilterBuilder.class.getClassLoader().getResourceAsStream("filter-json.txt")));
	System.out.println(parse);
	}
}
