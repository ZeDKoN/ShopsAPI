package com.db.challenge.services;

import java.util.Date;
import java.util.Map.Entry;

import javax.naming.ServiceUnavailableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.db.challenge.beans.Shop;
import com.db.challenge.expections.ShopNotFoundException;
import com.db.challenge.repository.ShopsRepository;
import com.db.challenge.repository.ShopsRepositoryImpl;
import com.db.challenge.utils.LocationTools;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.GeocodingResult;

/**
 * @author Francisco San Roman
 *
 */
@Service
public class ShopManagerServiceImpl implements ShopManagerService {
	
	public static final Logger logger = LoggerFactory.getLogger(ShopManagerServiceImpl.class);
	
	@Autowired
	private ShopsRepository shopRepository;
	
	@Value("${app.server.googleApiKey}")
	private String apiGoogleKey;

	@Override
	public Shop saveShop(Shop shop){
		if (logger.isInfoEnabled())
			logger.info("Updating store : {}", shop);
		
		StringBuilder address = new StringBuilder()
								.append(String.valueOf(shop.getShopAdress().getNumber()))
								.append(" ")
								.append(shop.getShopAdress().getStreet())
								.append(" ")
								.append(shop.getShopAdress().getPostCode());
		
		GeoApiContext context = new GeoApiContext().setApiKey(getApiGoogleKey());
		
		/**
		 * Asyncronous call so we dont need to wait update
		 *  the till we have a responde from Google Maps API
		 **/
		GeocodingApiRequest req = GeocodingApi.newRequest(context).address(address.toString());

		/**
		 *  We don't wait to get to geolocation from google maps API for saving the shop. 
		 *  we will update the geolocation later. We tag the object with a unique specific version, 
		 *  that will allow to compare it with the latest version, 
		 *  just in case another thread updated the shops cache meanwhile.
		 */
		shop.setVersion(new Date().getTime());
		shopRepository.saveShop(shop);
		req.setCallback(new PendingResult.Callback<GeocodingResult[]>() {
			
		  @Override
		  public void onResult(GeocodingResult[] result) {
			  Shop latestShop = shopRepository.findShopByName(shop.getShopName());
			  if(shop.getVersion().equals(latestShop.getVersion())){
				  shop.setGeoPosition(result[0].geometry.location);
				  shopRepository.saveShop(shop);
			  }else{
				  throw new RuntimeException ("Error when updatint geo location, "
				  		+ "found an more recent object version: " + latestShop.getVersion());
			  }
		  }
		  @Override
		  public void onFailure(Throwable e) {
			  logger.error("Error when calling GeocodingApi for shop: " + shop.getShopName());
		  }
		});
		if (logger.isDebugEnabled())
			logger.debug("List of stores in memory : {}", shopRepository.getAllShops());
		return shop;
	}
	
	@Override
	public Shop getClosestShop(double longitud, double latitude) throws ShopNotFoundException{
		
		double closestDistance = -1;
		Shop closestShop = null;
		for (Entry<String, Shop> entry : shopRepository.getAllShops().entrySet()) {
			Shop shop = entry.getValue();
			double distance = LocationTools.distanceBetween(longitud, latitude, 
								shop.getGeoPosition().lat, shop.getGeoPosition().lng);
			if(closestDistance == -1 || closestDistance >= distance){
				closestDistance = distance;
				closestShop = shop;
			}
		}
		
		if (closestShop == null){
			 throw new ShopNotFoundException(); 
		}
		if (logger.isInfoEnabled()){
			logger.info("Closest shop discance: {}", closestDistance);
			logger.info("Closest shop: {}", closestDistance);
		}
		return closestShop;
	}
	
	@Override
	public Shop getShop(String shopName) throws ShopNotFoundException {
		Shop shop = shopRepository.findShopByName(shopName);
		if (shop == null){
			 throw new ShopNotFoundException(); 
		}
		return shop;
	}
	
	public String getApiGoogleKey() {
		return apiGoogleKey;
	}
}