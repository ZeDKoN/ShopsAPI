package com.db.challenge.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.db.challenge.beans.Shop;

/**
 * @author Francisco San Roman
 * 
 * Implementation of Interface ShopsRepository in which we simulate persistence.
 *
 */
@Component
public class ShopsRepositoryImpl implements ShopsRepository {

	/**
	 * ConcurrentHashMap will allow to perform concurrent modifications of the
	 * Map from several threads without the need to block them
	 */
	private Map<String, Shop> myStoreMap = new ConcurrentHashMap<String, Shop>();

	@Override
	public Shop findShopByName(String shopName) {
		return myStoreMap.get(shopName);
	}

	@Override
	public void saveShop(Shop shop) {
		myStoreMap.put(shop.getShopName(), shop);
	}

	@Override
	public Map<String, Shop> getAllShops() {
		return myStoreMap;
	}
}
