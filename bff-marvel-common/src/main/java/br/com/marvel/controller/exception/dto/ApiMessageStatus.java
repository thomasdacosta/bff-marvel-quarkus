package br.com.marvel.controller.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
public class ApiMessageStatus implements Serializable {

	private static final long serialVersionUID = 557098096679648463L;

	@Getter
	@Setter
	private Integer code;

	@Getter
	@Setter
	private String message;

	@Setter
	private String detail;

	@Getter
	@Setter
	private String traceId = UUID.randomUUID().toString();

	public static ApiMessageStatus badRequest(Exception ex) {
		return ApiMessageStatus.create().code(Response.Status.BAD_REQUEST.getStatusCode()).message("BAD_REQUEST")
				.detail(ex.getMessage());
	}

	public static ApiMessageStatus notFound(Exception ex) {
		return ApiMessageStatus.create().code(Response.Status.NOT_FOUND.getStatusCode()).message("NOT_FOUND").detail(ex.getMessage());
	}

	public static ApiMessageStatus methodNotSupported(Exception ex) {
		return ApiMessageStatus.create().code(Response.Status.METHOD_NOT_ALLOWED.getStatusCode()).message("METHOD_NOT_ALLOWED")
				.detail(ex.getMessage());
	}

	public static ApiMessageStatus methodNotImplementedException(Exception ex) {
		return ApiMessageStatus.create().code(Response.Status.NOT_IMPLEMENTED.getStatusCode()).message("METHOD_NOT_IMPLEMENTED")
				.detail(ex.getMessage());
	}

	public static ApiMessageStatus internalServerError(Exception ex) {
		return ApiMessageStatus.create().code(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).message("INTERNAL_SERVER_ERROR")
				.detail(ex.getMessage());
	}

	public static ApiMessageStatus create() {
		return new ApiMessageStatus();
	}

	public ApiMessageStatus code(Integer code) {
		this.setCode(code);
		return this;
	}

	public ApiMessageStatus message(String message) {
		this.setMessage(message);
		return this;
	}

	public ApiMessageStatus detail(String detail) {
		this.setDetail(detail);
		return this;
	}

	@JsonInclude(Include.NON_NULL)
	public String getDetail() {
		return detail;
	}

}
