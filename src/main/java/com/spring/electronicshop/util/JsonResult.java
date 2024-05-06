package com.spring.electronicshop.util;


import lombok.Data;
import lombok.Getter;

@Data
public class JsonResult<T> {
	  private Integer status;
	  private String message;
	  private T payload;

	  public static <T> JsonResult<T> result(Integer status, String message, T payload) {
	    JsonResult<T> result = new JsonResult<>();
	    result.status = status;
	    result.message = message;
	    result.payload = payload;
	    return result;
	  }
	  
	

	  public static <T> JsonResult<T> result(Integer status, String message) {
	    return result(status, message, null);
	  }

	  public static <T> JsonResult<T> success(T payload) {
	    return success(HttpStatus.SUCCESS.value, payload);
	  }

	  public static <T> JsonResult<T> success(String message) {
	    return result(HttpStatus.SUCCESS.status, message, null);
	  }

	  public static <T> JsonResult<T> success(String message, T payload) {
	    return result(HttpStatus.SUCCESS.status, message, payload);
	  }

	  public static <T> JsonResult<T> fail(String message) {
	    return fail(message, null);
	  }

	  public static <T> JsonResult<T> fail(String message, T payload) {
	    return result(HttpStatus.FAIL.status, message, payload);
	  }

	  public static <T> JsonResult<T> notFound(String message) {
	    return result(HttpStatus.NOT_FOUND.status, message, null);
	  }

	  @Getter
	  public enum HttpStatus {
	    SUCCESS(200, "SUCCESS"), FAIL(100, "FAIL"), BAD_REQUEST(400, "BAD_REQUEST"), NOT_FOUND(404,
	        "NOT_FOUND"), UNAUTHORIZED(401, "UNAUTHORIZED"), CONFLICT(409,
	            "CONFLICT"), INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR");

	    private Integer status;
	    private String value;

	    HttpStatus(Integer status, String value) {
	      this.status = status;
	      this.value = value;
	    }
	  }
}
