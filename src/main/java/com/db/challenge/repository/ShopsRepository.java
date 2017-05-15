package com.db.challenge.repository;

import java.util.Map;

import com.db.challenge.beans.Shop;

/**
 * @author Francisco San Roman
 * 
 *         Interface with basic CRUD operations, if there was a persistence
 *         layer we could use Spring-Data and with the Annotations @ Repository,
 *         I would also use proper entity Beans to work between layer and define
 *         the Model. Missing delete operations as not needed, update and delete
 *         are covered with the saveShop method.
 *
 */
public interface ShopsRepository {

	Shop findShopByName(String shopName);

	void saveShop(Shop shop);

	Map<String, Shop> getAllShops();

}
