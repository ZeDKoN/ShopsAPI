package com.db.challenge.services;

import com.db.challenge.beans.Shop;
import com.db.challenge.expections.ShopNotFoundException;

public interface ShopManagerService {

	/**
	 * Service that query Google Maps API to retrieve geo location given an
	 * address, retrieve coordinates, and store them with the shop object with
	 * the enriched coordinates.
	 * 
	 * @param shop
	 * @return
	 */
	Shop saveShop(Shop shop);

	/**
	 * Service that return the closest shop stored given a geolocation
	 * coordinates
	 * 
	 * @param longitud
	 * @param latitude
	 * @return
	 */
	Shop getClosestShop(double longitud, double latitude)  throws ShopNotFoundException;
	
	
	
	/**
	 * Return a shop given the shopName
	 * @param shopName
	 * @return
	 */
	Shop getShop(String shopName)  throws ShopNotFoundException;

}