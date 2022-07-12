package br.com.marvel.controller.dto.characters;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UrlCharacter {

	@NotEmpty @NotBlank
	@Size(max = 255)
    @JsonProperty("detail")
    private String type;

	@NotEmpty @NotBlank
	@Size(max = 255)
    @JsonProperty("url")
    private String url;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
