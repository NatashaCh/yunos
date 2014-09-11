package com.driverstack.yunos.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
public class Model {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Column
	private String name;

	@Column
	private String description;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "ModelConfigurationItem", joinColumns = @JoinColumn(name = "modelId"), inverseJoinColumns = @JoinColumn(name = "configurationItemId"))
	@MapKey(name = "name")
	private Map<String, ConfigurationItem> configurationItems = new HashMap<String, ConfigurationItem>();

	@Column
	private String locale;

	@JoinColumn(name = "primaryId")
	@ManyToOne(cascade = CascadeType.ALL)
	private Model primary;

	@JoinColumn(name = "deviceClassId")
	@ManyToOne(cascade = CascadeType.ALL)
	private DeviceClass deviceClass;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "primary")
	@MapKey(name = "locale")
	private Map<String, Model> locales = new HashMap<String, Model>();

	@JoinColumn(name = "vendorId")
	@ManyToOne(cascade = CascadeType.ALL)
	private Vendor vendor;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "compatible_models", joinColumns = { @JoinColumn(name = "modelId") }, inverseJoinColumns = { @JoinColumn(name = "compatibleModelId") })
	private Set<Model> compatibleModels = new HashSet<Model>();
	/**
	 * it is a good habit to create a sample configure and save beside the
	 * actual configure. sometime we may want to take a look what it should
	 * contains.
	 */
	@Column
	private String sampleConfigure;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
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

	public String getSampleConfigure() {
		return sampleConfigure;
	}

	public void setSampleConfigure(String sampleConfigure) {
		this.sampleConfigure = sampleConfigure;
	}

	 

	public Map<String, ConfigurationItem> getConfigurationItems() {
		return configurationItems;
	}

	public void setConfigurationItems(
			Map<String, ConfigurationItem> configurationItems) {
		this.configurationItems = configurationItems;
	}

	public Set<Model> getCompatibleModels() {
		return compatibleModels;
	}

	public void setCompatibleModels(Set<Model> compatibleModels) {
		this.compatibleModels = compatibleModels;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Model getPrimary() {
		return primary;
	}

	public void setPrimary(Model primary) {
		this.primary = primary;
	}

	public Map<String, Model> getLocales() {
		return locales;
	}

	public void setLocales(Map<String, Model> locales) {
		this.locales = locales;
	}

	public DeviceClass getDeviceClass() {
		return deviceClass;
	}

	public void setDeviceClass(DeviceClass deviceClass) {
		this.deviceClass = deviceClass;
	}

	/**
	 * after this operation, the entity should not be save to DB again.
	 * 
	 * @param locale
	 */
	private void copyLocaleFields(Model src) {
		this.name = src.getName();
		this.description = src.getDescription();
	}

	public Model get(String locale) {
		Model localeModel = locales.get(locale);
		if (localeModel != null)
			copyLocaleFields(localeModel);

		return this;
	}

	public com.driverstack.yunos.device.Model getVO() {
		com.driverstack.yunos.device.Model m = new com.driverstack.yunos.device.Model(
				vendor.getShortName(), name);
		return m;
	}
	
	public ConfigurationItem getCalculatedConfigurationItem(String name){
		ConfigurationItem item =configurationItems.get(name);
		if(item!=null)
			return item;
		else
			return vendor.getCalculatedConfigurationItem(name);
			
	}

}
