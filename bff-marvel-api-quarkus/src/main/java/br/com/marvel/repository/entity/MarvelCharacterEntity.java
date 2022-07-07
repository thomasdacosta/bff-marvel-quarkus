package br.com.marvel.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="marvel_character")
public class MarvelCharacterEntity extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "dateModified")
	private Date dateModified;

	@Column(name = "dateCreated")
	private Date dateCreated;

	@OneToOne
	@JoinColumn(name = "id_thumbnail_character")
	private ThumbnailCharacterEntity thumbnail;

	@OneToMany
	@JoinColumn(name = "id_marvel_character")
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

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

}
