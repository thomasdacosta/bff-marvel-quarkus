package br.com.marvel.resource.dto.characters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class MarvelCharacter {

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonProperty("id")
	@NotNull
	private BigDecimal id;

	@NotEmpty @NotBlank
	@Size(max = 255)
	@JsonProperty("name")
	private String name;

	@NotEmpty @NotBlank
	@Size(max = 255)
	@JsonProperty("description")
	private String description;

	@JsonProperty("modified")
	private String modified;

	@Valid
	@NotNull
	@JsonProperty("thumbnail")
	private ThumbnailCharacter thumbnail;

	@Valid
	@NotEmpty
	@JsonProperty("urls")
	private List<UrlCharacter> urlCharacters;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public ThumbnailCharacter getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(ThumbnailCharacter thumbnail) {
		this.thumbnail = thumbnail;
	}

	public List<UrlCharacter> getUrlCharacters() {
		return urlCharacters;
	}

	public void setUrlCharacters(List<UrlCharacter> urlCharacters) {
		this.urlCharacters = urlCharacters;
	}

}
