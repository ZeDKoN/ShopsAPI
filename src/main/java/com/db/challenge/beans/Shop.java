package com.db.challenge.beans;

import javax.validation.constraints.NotNull;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.maps.model.LatLng;

/**
 * @author Francisco San Roman
 *
 */
public class Shop extends ResourceSupport {

	@NotNull
	private String shopName;
	@NotNull
	private ShopAddress shopAdress;

	private LatLng geoPosition;

	@JsonIgnore
	private Long version;

	public Shop() {
		super();
	}

	public Shop(String shopName, String number, String street, String postCode) {
		this.shopName = shopName;
		this.shopAdress = new ShopAddress(number, street, postCode);
	}

	public String getShopName() {
		return shopName;
	}

	public ShopAddress getShopAdress() {
		return shopAdress;
	}

	public LatLng getGeoPosition() {
		return geoPosition;
	}

	public void setGeoPosition(LatLng geoPosition) {
		this.geoPosition = geoPosition;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Shop [shopName=" + shopName + ", shopAdress=" + shopAdress + ", geoPosition=" + geoPosition + "]";
	}
}
