package br.com.marvel.adapters.outbound.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="tb_character")
public class CharacterEntity extends PanacheEntityBase {

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
	@Column(name = "name")
	private String name;

	@NotEmpty @NotBlank
	@Size(max = 255)
	@Column(name = "description")
	private String description;

	@NotNull
	@Column(name = "dateCreated")
	private Date dateCreated;

	@NotNull
	@Column(name = "dateModified")
	private Date dateModified;

	@Valid
	@NotNull
	@OneToOne
	@JoinColumn(name = "id_thumbnail_character")
	private ThumbnailCharacterEntity thumbnail;

	@Valid
	@NotEmpty
	@OneToMany
	@JoinColumn(name = "id_character")
	private Set<UrlCharacterEntity> urlCharacterEntities;

	public Set<UrlCharacterEntity> getUrlCharacterEntities() {
		return urlCharacterEntities;
	}

	public void setUrlCharacterEntities(Set<UrlCharacterEntity> urlCharacterEntities) {
		this.urlCharacterEntities = urlCharacterEntities;
	}

	public ThumbnailCharacterEntity getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(ThumbnailCharacterEntity thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

}
