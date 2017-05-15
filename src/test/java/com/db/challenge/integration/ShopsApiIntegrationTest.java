package com.db.challenge.integration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.db.challenge.expections.ShopNotFoundException;
import com.db.challenge.repository.ShopsRepository;
import com.google.maps.model.LatLng;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopsApiApplication.class)
@WebAppConfiguration
public class ShopsApiIntegrationTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
	
	private MockMvc mockController;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	
	@Autowired 
	ShopsRepository repository;
	
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	@Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
	
	@Before
    public void setup() throws Exception {
        this.mockController = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createShop() throws Exception {
    	mockController.perform(post("/shops/")
        		.content(this.json(new Shop("Simply Food 1","323-324","High Holborn", "WC1V 7PUT")))
                .contentType(contentType)).andDo(print())
                .andExpect(status().isOk());
    }
    
    public void createNShops() throws Exception {
    	mockController.perform(post("/shops/").content(this.json(new Shop("Zara 1","12","Farringdon Road", "EC1N")))
                .contentType(contentType))
                .andExpect(status().isOk());
        
    	mockController.perform(post("/shops/")
        		.content(this.json(new Shop("Zara 4","12","Westbourne Terrace", "W2")))
                .contentType(contentType))
                .andExpect(status().isOk());
    	
    	mockController.perform(post("/shops/")
        		.content(this.json(new Shop("Simply Food 1","323-324","High Holborn", "WC1V 7PUT")))
                .contentType(contentType))
                .andExpect(status().isOk());
    }
    @Test()
    public void shopNotFound() throws Exception {
    	mockController.perform(get("/shops?latitude=51.52156840000001&longitude=-0.1077042")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void retrieveClosestShopFail() throws Exception {
    	mockController.perform(get("/shop?latitude=0L&longitude=0L")
                .contentType(contentType))
                .andExpect(status().is4xxClientError());
    }

	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
