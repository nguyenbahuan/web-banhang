package com.spring.electronicshop.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {
	public static final ObjectMapper MAPPER;

	static {
		MAPPER = new ObjectMapper();
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * Object to String JSON
	 * 
	 * @param object
	 * @return
	 */
	public static String objectToJson(Object object) {
		try {
			String result = MAPPER.writeValueAsString(object);
			return result;
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * JSON to Object
	 * 
	 * @param <T>
	 * @param jsonData
	 * @param valueType
	 * @return
	 */
	public static <T> T jsonToObject(String jsonData, Class<T> valueType) {
		try {
			return MAPPER.readValue(jsonData, valueType);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
}
