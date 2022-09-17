package com.example.controller;

import com.example.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntTest
{
    private static final String NEW_NAME = "newName";

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl()
    {
        return "http://localhost:" + port;
    }

    @Test
    public void contextLoads()
    {

    }

    @Test
    public void testGetAllProducts()
    {
        // given
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        // when
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/products",
                        HttpMethod.GET, entity, String.class);

        // then
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetProductById()
    {
        // given

        // when
        Product product = restTemplate.getForObject(getRootUrl() + "/products/1", Product.class);

        // then
        assertNotNull(product);
        assertEquals(1, product.getId());
    }

    @Test
    public void testCreateProduct()
    {
        // given
        Product product = new Product();
        product.setName(NEW_NAME);
        product.setDescription("description");
        product.setPrice(new BigDecimal(100));

        // when
        ResponseEntity<Product> postResponse =
                        restTemplate.postForEntity(getRootUrl() + "/products", product,
                                        Product.class);

        // then
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertEquals(NEW_NAME, postResponse.getBody().getName());
    }

    @Test
    public void testUpdateProduct()
    {
        // given
        int id = 1;
        Product product =
                        restTemplate.getForObject(getRootUrl() + "/products/" + id, Product.class);
        product.setName(NEW_NAME);

        // when
        restTemplate.put(getRootUrl() + "/products/" + id, product);

        // then
        Product updatedProduct =
                        restTemplate.getForObject(getRootUrl() + "/products/" + id, Product.class);
        assertNotNull(updatedProduct);
        assertEquals(NEW_NAME, updatedProduct.getName());
    }

    @Test
    public void testDeleteProduct()
    {
        int id = 2;
        Product product =
                        restTemplate.getForObject(getRootUrl() + "/products/" + id, Product.class);
        assertNotNull(product);

        restTemplate.delete(getRootUrl() + "/prodcuts/" + id);

        try
        {
            product = restTemplate.getForObject(getRootUrl() + "/productszzz/" + id, Product.class);
        }
        catch (final HttpClientErrorException e)
        {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }
}
