package br.com.marvel.adapters.outbound.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="url_character")
public class UrlCharacterEntity extends PanacheEntityBase {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(
			name = "sequence-generator",
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
					@org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
					@org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
			}
	)
	@NotNull
	private Long id;

	@NotEmpty @NotBlank
	@Size(max = 255)
	@Column(name = "type")
	private String type;

	@NotEmpty @NotBlank
	@Size(max = 255)
	@JsonProperty("url")
	@Column(name = "url")
	private String url;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
