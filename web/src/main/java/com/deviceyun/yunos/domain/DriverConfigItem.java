package com.deviceyun.yunos.domain;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

/**
 * it is a entity.
 * 
 * @author jack
 * 
 */
@javax.persistence.Entity
public class DriverConfigItem {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Column
	private String name;

	@Column
	private String description;

	@Column
	private String type;	

	@Column
	private String constraints;	

	@Column
	private String locale;

	@JoinColumn(name = "primaryId")
	@ManyToOne(cascade = CascadeType.ALL)
	private DriverConfigItem primary;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "primary")
	@MapKey(name = "locale")
	private Map<String, DriverConfigItem> locales = new HashMap<String, DriverConfigItem>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public DriverConfigItem getPrimary() {
		return primary;
	}

	public void setPrimary(DriverConfigItem primary) {
		this.primary = primary;
	}

	public Map<String, DriverConfigItem> getLocales() {
		return locales;
	}

	public void setLocales(Map<String, DriverConfigItem> locales) {
		this.locales = locales;
	}

	@Override
	public String toString() {
		return name;
	}

	public DriverConfigItem get(String locale) {
		DriverConfigItem lb = locales.get(locale);
		if (lb != null)
			return lb;
		else
			return this;
	}
}
