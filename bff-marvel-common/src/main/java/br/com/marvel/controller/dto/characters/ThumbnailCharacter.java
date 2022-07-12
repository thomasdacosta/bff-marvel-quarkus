package br.com.marvel.controller.dto.characters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ThumbnailCharacter {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("id")
	private BigDecimal id;

	@NotEmpty @NotBlank
	@Size(max = 255)
	@JsonProperty("url")
	private String url;

	@NotEmpty @NotBlank
	@Size(max = 255)
	@JsonProperty("extension")
	private String extension;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

}
