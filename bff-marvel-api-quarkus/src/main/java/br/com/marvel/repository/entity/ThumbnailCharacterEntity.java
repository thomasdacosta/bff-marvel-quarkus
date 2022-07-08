package br.com.marvel.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="thumbnail_character")
public class ThumbnailCharacterEntity extends PanacheEntityBase {

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
	private Long id;

	@Column(name = "url")
	private String url;

	@Column(name = "extension")
	private String extension;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
