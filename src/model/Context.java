package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {
	private static Map<String, Object> data = new HashMap<String, Object>();
	
	public static int getIntData(String key) {					
		int int_data = ( data.get(key) instanceof Integer )? (int) data.get(key) : -1;
				
		removeData(key);
		return int_data;
	}
	
	public static Long getLongData(String key) {
		Long long_data = ( data.get(key) instanceof Long )? (Long) data.get(key) : null;
		
		removeData(key);
		return long_data;
	}
	
	public static boolean getBooleanData(String key) {					
		boolean bool_data = ( data.get(key) instanceof Boolean )? (boolean) data.get(key) : false;
				
		removeData(key);
		return bool_data;
	}
	
	public static <T> List<T> getListData(String key) {
		@SuppressWarnings("unchecked")
		List<T> list_data = ( data.get(key) instanceof List )? (List<T>) data.get(key) : null;
		
		removeData(key);
		return list_data;
	}
	
	private static void removeData(String key) {
		data.remove(key);
	}
	
	public static void setData(String key, Object new_data) {
		removeData(key);
		addData(key, new_data);
	}
	
	public static void addData( String key, Object new_data ) {
		if( data.containsKey(key) ) removeData(key);
		
		data.put(key, new_data);
	}
}
