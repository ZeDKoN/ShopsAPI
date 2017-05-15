package com.db.challenge.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.db.challenge.ShopsApiApplication;
import com.db.challenge.beans.Shop;
import com.db.challenge.utils.LocationTools;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopsApiApplication.class)
@WebAppConfiguration
public class ShopsApiUnitTests {

	public static final Logger logger = LoggerFactory.getLogger(ShopsApiUnitTests.class);

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockController;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
		this.mockController = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void saveShopWithNull() throws Exception {
		mockController.perform(post("/shops/").content(this.json(new Shop())).contentType(contentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void saveShopAdressIsNull() throws Exception {
		mockController
				.perform(post("/shops/").content(this.json(new Shop(null, null, null, null))).contentType(contentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void storeIsNull() throws Exception {
		mockController.perform(get("/shops/").content(this.json(new Shop())).contentType(contentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void addressIsNull() throws Exception {
		mockController
				.perform(get("/shops/").content(this.json(new Shop(null, null, null, null))).contentType(contentType))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void serviceNotFound() throws Exception {
		mockController.perform(post("/xxxx/").content(this.json(new Shop())).contentType(contentType))
				.andExpect(status().isNotFound());
	}

	@Test
	public void locationToolsTest() throws Exception {
		assertEquals("The distance between the two coorditates is: ",
				LocationTools.distanceBetween(51.52156840000001, -0.1077042, 51.5134745, -0.1237534),
				1428.2922850748187, 0);
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}
