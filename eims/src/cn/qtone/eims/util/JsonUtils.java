package cn.qtone.eims.util;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json实用类
 */
public class JsonUtils {


	static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
	}

	public static <T> T fromJson(String jsonString, TypeReference<T> valueTypeRef ) {
		if (jsonString==null || "".equals(jsonString)) {
			return null;
		}
		try {
			return mapper.readValue(jsonString, valueTypeRef);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T fromJson(String jsonString, Class<T> clazz) {
		if (jsonString==null || "".equals(jsonString)) {
			return null;
		}
		try {
			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {	
			e.printStackTrace();
			return null;
		}
	}

	/** 
	 * 如果对象为Null,返回"null". 
	 * 如果集合为空集合,返回"[]". 
	 */
	public static String toJson(Object object) {

		try {
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
