package com.db.challenge.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.db.challenge.controller.ShopsController;
import com.db.challenge.expections.ShopNotFoundException;
import com.db.challenge.expections.ShopsExceptionHandler;

@RunWith(MockitoJUnitRunner.class)
public class ShopsApiExceptionHandlerTest {

	public static final Logger logger = LoggerFactory.getLogger(ShopsApiUnitTests.class);

	private MockMvc mockController;

	@Mock
	ShopsController shopsController;

	@Before
	public void setup() throws Exception {

		mockController = MockMvcBuilders.standaloneSetup(shopsController)
				.setControllerAdvice(new ShopsExceptionHandler()).build();
	}

	@Test
	public void checkIllegalArgumentException() throws Exception {
		Mockito.when(shopsController.getClosestStore(Mockito.anyDouble(), Mockito.anyDouble()))
				.thenThrow(new IllegalArgumentException("Custom message"));
		mockController.perform(get("/shops?latitude=51.52156840000001&longitude=-0.1077042"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("message", equalTo("Illegal Argument in  Rest Shops API: Custom message")));
	}
	
	@Test

	public void checkShopNotFoundException() throws Exception {
		Mockito.when(shopsController.getShop(Mockito.anyString()))
				.thenThrow(new ShopNotFoundException());
		mockController.perform(get("/shops/Simplyyyy%20Food%202"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message", equalTo("Shop not found")));
	}

	@Test
	public void checkRuntimeException() throws Exception {
		Mockito.when(shopsController.getClosestStore(Mockito.anyDouble(), Mockito.anyDouble()))
				.thenThrow(new RuntimeException("Custom message"));
		mockController.perform(get("/shops?latitude=51.52156840000001&longitude=-0.1077042"))
				.andExpect(status().isServiceUnavailable())
				.andExpect(jsonPath("message", equalTo("Error in  Rest Shops API: Custom message")));
	}
	
	@Test
	public void checkOtherException() throws Exception {
		Mockito.when(shopsController.getClosestStore(Mockito.anyDouble(), Mockito.anyDouble()))
				.thenThrow(new NullPointerException("Custom message"));
		mockController.perform(get("/shops?latitude=51.52156840000001&longitude=-0.1077042"))
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("message", equalTo("Error in  Rest Shops API: Custom message")));
	}
}
