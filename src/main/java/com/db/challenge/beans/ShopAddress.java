package com.db.challenge.beans;

import javax.validation.constraints.NotNull;

/**
 * @author Francisco San Roman
 *
 */
public class ShopAddress {

	// Number is created as String as we can get number mixed such as 56-57
	@NotNull
	private String number;

	// Added to be consistent with google API
	@NotNull
	private String street;

	@NotNull
	private String postCode;

	public ShopAddress(String number, String street, String postCode) {
		this.number = number;
		this.street = street;
		this.postCode = postCode;
	}

	public ShopAddress() {
		super();
	}

	public String getNumber() {
		return number;
	}

	public String getStreet() {
		return street;
	}

	public String getPostCode() {
		return postCode;
	}

	@Override
	public String toString() {
		return "ShopAddress [number=" + number + ", street=" + street + ", postCode=" + postCode + "]";
	}
}
