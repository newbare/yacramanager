/**
 * 
 */
package fr.wati.yacramanager.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

/**
 * @author Rachid Ouattara
 * 
 */
public abstract class Filter {

	private FilterType type;
	private String field;

	/**
	 * @return the type
	 */
	public FilterType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(FilterType type) {
		this.type = type;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	
	public static class FilterComparatorValue{
		
		private BigDecimal value;
		private BigDecimal startValue;
		private BigDecimal endValue;
		public BigDecimal getValue() {
			return value;
		}
		public void setValue(BigDecimal value) {
			this.value = value;
		}
		public BigDecimal getStartValue() {
			return startValue;
		}
		public void setStartValue(BigDecimal startValue) {
			this.startValue = startValue;
		}
		public BigDecimal getEndValue() {
			return endValue;
		}
		public void setEndValue(BigDecimal endValue) {
			this.endValue = endValue;
		}
		
		
	}

	public static class FilterDateValue {

		private Date date;;
		private Date start;
		private Date end;

		
		
		/**
		 * @param start
		 * @param end
		 */
		public FilterDateValue(Date start, Date end) {
			super();
			this.start = start;
			this.end = end;
		}
		
		public FilterDateValue(Date date) {
			super();
			this.date = date;
		}

		/**
		 * @return the start
		 */
		public Date getStart() {
			return start;
		}

		/**
		 * @param start
		 *            the start to set
		 */
		public void setStart(Date start) {
			this.start = start;
		}

		/**
		 * @return the end
		 */
		public Date getEnd() {
			return end;
		}

		/**
		 * @param end
		 *            the end to set
		 */
		public void setEnd(Date end) {
			this.end = end;
		}

		/**
		 * @return the date
		 */
		public Date getDate() {
			return date;
		}

		/**
		 * @param date the date to set
		 */
		public void setDate(Date date) {
			this.date = date;
		}

	}

	public static class FilterArrayValue{
		private String name;
		private String label;
		private boolean ticked;
		
		
		/**
		 * 
		 */
		public FilterArrayValue() {
			super();
		}
		
		
		/**
		 * @param name
		 * @param label
		 * @param ticked
		 */
		public FilterArrayValue(String name, String label, boolean ticked) {
			super();
			this.name = name;
			this.label = label;
			this.ticked = ticked;
		}


		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
		/**
		 * @param label the label to set
		 */
		public void setLabel(String label) {
			this.label = label;
		}
		/**
		 * @return the ticked
		 */
		public boolean isTicked() {
			return ticked;
		}
		/**
		 * @param ticked the ticked to set
		 */
		public void setTicked(boolean ticked) {
			this.ticked = ticked;
		}
		
		
	}
	
	public static enum FilterType {
		ARRAY, TEXT, DATE,DATE_RANGE, BOOLEAN,COMPARATOR_EQUALS,COMPARATOR_BETWEEN,COMPARATOR_LESSTHAN,COMPARATOR_GREATERTHAN;

		private FilterType() {
		}

		
	}
	
	public static class FilterDate extends Filter{
		
		private FilterDateValue value;
		private boolean rangedDate;

		/**
		 * @return the value
		 */
		public FilterDateValue getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(FilterDateValue value) {
			this.value = value;
		}

		/**
		 * @return the rangedDate
		 */
		public boolean isRangedDate() {
			return rangedDate;
		}

		/**
		 * @param rangedDate the rangedDate to set
		 */
		public void setRangedDate(boolean rangedDate) {
			this.rangedDate = rangedDate;
		}
		
	}
	
	public static enum Comparator{
		EQUALS,GREATERTHAN,LESSTHAN,BETWEEN;
	}
	
	public static class FilterComparator extends Filter{
		
		private Comparator comparator;
		
		private FilterComparatorValue value;

		/**
		 * @return the value
		 */
		public FilterComparatorValue getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(FilterComparatorValue value) {
			this.value = value;
		}

		public Comparator getComparator() {
			return comparator;
		}

		public void setComparator(Comparator comparator) {
			this.comparator = comparator;
		}
		
		
		
	}
	
	public static class FilterBoolean extends Filter{
		private boolean value;

		/**
		 * @return the value
		 */
		public boolean isValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(boolean value) {
			this.value = value;
		}
		
	}
	public static class FilterText extends Filter{
		private String value;

		
		
		/**
		 * @param value
		 */
		public FilterText(String value) {
			super();
			this.value = value;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
		
	}
	public static class FilterWrapper{
		private Filter[] filter;

		/**
		 * @return the filters
		 */
		public Filter[] getFilter() {
			return filter;
		}

		/**
		 * @param filters the filters to set
		 */
		public void setFilter(Filter[] filter) {
			this.filter = filter;
		}
		
		
	}
	public static class FilterArray extends Filter{
		private List<FilterArrayValue> value=new ArrayList<>();

		/**
		 * @return the value
		 */
		public List<FilterArrayValue> getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(List<FilterArrayValue> value) {
			this.value = value;
		}
	}
	public static class FilterBuilder{
		private static ObjectMapper objectMapper=new ObjectMapper();
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public static List parse(String json) throws Exception{
			List filters=new ArrayList<>();
			ISO8601DateFormat iso8601DateFormat=new ISO8601DateFormat();
			JsonNode filterNode = objectMapper.readTree(json);
			ArrayNode filtersNode=(ArrayNode) filterNode.get("filter");
			Iterator<JsonNode> iterator = filtersNode.iterator();
			while (iterator.hasNext()) {
				JsonNode jsonNode = (JsonNode) iterator.next();
				FilterType filterType= FilterType.valueOf(((TextNode)jsonNode.get("type")).asText());
				switch (filterType) {
				case ARRAY:
					FilterArray filterArray=new FilterArray();
					filterArray.setType(filterType);
					filterArray.setField(((TextNode)jsonNode.get("field")).asText());
					List<FilterArrayValue> filterArrayValues=new ArrayList<Filter.FilterArrayValue>();
					ArrayNode valueArrayNode= (ArrayNode) jsonNode.get("value");
					Iterator<JsonNode> valueIterator = valueArrayNode.iterator();
					while (valueIterator.hasNext()) {
						JsonNode jsonNode2 = (JsonNode) valueIterator.next();
						FilterArrayValue filterArrayValue=new FilterArrayValue();
						filterArrayValue.setLabel(((TextNode)jsonNode2.get("label")).asText());
						filterArrayValue.setName(((TextNode)jsonNode2.get("name")).asText());
						filterArrayValues.add(filterArrayValue);
					}
					filterArray.setValue(filterArrayValues);
					filters.add(filterArray);
					break;
				case BOOLEAN:
					FilterBoolean filterBoolean=new FilterBoolean();
					filterBoolean.setField(((TextNode)jsonNode.get("field")).asText());
					filterBoolean.setType(filterType);
					filterBoolean.setValue(jsonNode.get("value").asBoolean());
					filters.add(filterBoolean);
					break;
				case DATE_RANGE:
					FilterDate filterRangeDate=new FilterDate();
					filterRangeDate.setField(((TextNode)jsonNode.get("field")).asText());
					filterRangeDate.setType(filterType);
					FilterDateValue filterRangeDateValue=new FilterDateValue(iso8601DateFormat.parse(jsonNode.get("value").get("start").asText()), iso8601DateFormat.parse(jsonNode.get("value").get("end").asText()));
					filterRangeDate.setValue(filterRangeDateValue);
					filterRangeDate.setRangedDate(true);
					filters.add(filterRangeDate);
					break;
				case DATE:
					FilterDate filterDate=new FilterDate();
					filterDate.setField(((TextNode)jsonNode.get("field")).asText());
					filterDate.setType(filterType);
					FilterDateValue filterDateValue=new FilterDateValue(iso8601DateFormat.parse(jsonNode.get("value").asText()));
					filterDate.setValue(filterDateValue);
					filters.add(filterDate);
					break;
				case TEXT:
					FilterText filterText=new FilterText(jsonNode.get("value").asText());
					filterText.setType(filterType);
					filterText.setField(((TextNode)jsonNode.get("field")).asText());
					filters.add(filterText);
					break;
				case COMPARATOR_EQUALS:
				case COMPARATOR_BETWEEN:
				case COMPARATOR_GREATERTHAN:
				case COMPARATOR_LESSTHAN:
					FilterComparator filterComparator=new FilterComparator();
					filterComparator.setType(filterType);
					filterComparator.setField(((TextNode)jsonNode.get("field")).asText());
					filterComparator.setComparator(Comparator.valueOf(filterType.toString().split("_")[1]));
					FilterComparatorValue comparatorValue=new FilterComparatorValue();
					switch (filterType) {
						case COMPARATOR_BETWEEN:
							comparatorValue.setStartValue(BigDecimal.valueOf(Long.valueOf(jsonNode.get("value").get("start").asText())));
							comparatorValue.setEndValue(BigDecimal.valueOf(Long.valueOf(jsonNode.get("value").get("end").asText())));
							break;
						default:
							comparatorValue.setValue(BigDecimal.valueOf(Double.valueOf(jsonNode.get("value").asText())));
							break;
					}
					filterComparator.setValue(comparatorValue);
					filters.add(filterComparator);
					break;
				}
			}
			return filters;
		}
	}
}
