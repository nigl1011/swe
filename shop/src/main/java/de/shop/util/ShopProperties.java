package de.shop.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.google.common.base.Splitter;

@ApplicationScoped
public class ShopProperties implements Serializable {
	private static final long serialVersionUID = 3916523726340426731L;

	private static final String PROPS_FILE = "shop.properties";
	
	private transient Map<String, String> mapString;
	private transient Map<String, List<String>> mapListString;

	@PostConstruct
	private void init() {
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final Properties props = new Properties();
		try (InputStream inputStream = classLoader.getResourceAsStream(PROPS_FILE)) {
			props.load(inputStream);
		}
		catch (IOException e) {
			throw new IllegalStateException(e);
		}
		
		mapString = new HashMap<>();
		mapListString = new HashMap<>();
		
		final Set<Entry<Object, Object>> propsEntries = props.entrySet();
		for (Entry<Object, Object> e : propsEntries) {
			final String key = (String) e.getKey();
			
			String value = (String) e.getValue();
			if (value != null && value.startsWith("[")  && value.endsWith("]")) {
				// [und ] am Anfang und Ende entfernen
				value = value.substring(1, value.length() - 1);
				
				// Konvertierung in eine Liste
				final Iterable<String> tokens = Splitter.on(',')
                                                        .trimResults()
                                                        .omitEmptyStrings()
                                                        .split(value);
				final List<String> valueList = new ArrayList<>();
				for (String s : tokens) {
					valueList.add(s);
				}
				
				mapListString.put((String) key, valueList);
			}
			else {
				mapString.put((String) key, value);
			}
		}
	}
	
	public String getString(String key) {
		return mapString.get(key);
	}
	
	public Integer getInteger(String key) {
		return Integer.valueOf(mapString.get(key));
	}
	
	public List<String> getStringList(String key) {
		return mapListString.get(key);
	}
}
