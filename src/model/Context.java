package model;

import java.util.HashMap;
import java.util.Map;

public class Context {
	private static Map<String, Object> data = new HashMap<String, Object>();
	
	public static int getIntData(String key) {					
		int int_data = ( data.get(key) instanceof Integer )? (int) data.get(key) : -1;
				
		removeData(key);
		return int_data;
	}
	
	private static void removeData(String key) {
		data.remove(key);
	}
	
	public static void setData(String key, Object new_data) {
		removeData(key);
		addData(key, new_data);
	}
	
	public static void addData( String key, Object new_data ) {
		data.put(key, new_data);
	}
}
