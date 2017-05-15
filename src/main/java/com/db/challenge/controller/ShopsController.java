package com.db.challenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.db.challenge.beans.Shop;
import com.db.challenge.expections.ShopNotFoundException;
import com.db.challenge.services.ShopManagerService;

/**
 * @author Francisco San Roman
 *
 */
@RestController
@RequestMapping("/shops")
public class ShopsController {

	public static final Logger logger = LoggerFactory.getLogger(ShopsController.class);

	@Autowired
	private ShopManagerService shopManagerService;

	@RequestMapping(method = RequestMethod.POST)
	public Shop saveStore(@Validated @RequestBody Shop shop) throws ShopNotFoundException {
		if (logger.isInfoEnabled())
			logger.info("Saving store: {}", shop);
		
		return enrichHyperLink(shopManagerService.saveShop(shop));
	}

	@RequestMapping(method = RequestMethod.GET)
	public Shop getClosestStore(@Validated @RequestParam(value = "longitude") double longitude,
			@RequestParam(value = "latitude") double latitude) throws ShopNotFoundException {
		if (logger.isInfoEnabled())
			logger.info("Calling getClosestStore with longitude: {} and latitude{}", longitude, latitude);
		
		return enrichHyperLink(shopManagerService.getClosestShop(longitude, latitude));
	}
	
	@RequestMapping(method = RequestMethod.GET , path = "/{shopName}")
	public Shop getShop(@Validated @PathVariable (value = "shopName") String shopName) throws ShopNotFoundException {
		if (logger.isInfoEnabled())
			logger.info("Calling getShop with shopName: {}", shopName);
		
		return enrichHyperLink(shopManagerService.getShop(shopName));
	}

	private Shop enrichHyperLink(Shop shop) throws ShopNotFoundException {
		shop.removeLinks();
		shop.add(linkTo(methodOn(ShopsController.class, shop.getShopName()).getShop(shop.getShopName()))
                .withSelfRel());
		return shop;
	}
	
}
