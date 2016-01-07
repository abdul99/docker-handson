package br.com.alexandreesl.handson.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.discovery.DiscoveryClient;

@RestController
@RequestMapping("/")
public class OrderRest {

	private static long id = 1;

	// Created automatically by Spring Cloud
	@Autowired
	@LoadBalanced
	private RestTemplate restTemplate;

	@Autowired
	private DiscoveryClient discoveryClient;

	private Logger logger = Logger.getLogger(OrderRest.class);

	@RequestMapping(value = "order/{idCustomer}/{idProduct}/{amount}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Order submitOrder(@PathVariable long idCustomer, @PathVariable long idProduct, @PathVariable long amount) {

		Order order = new Order();

		Map map = new HashMap();

		map.put("id", idCustomer);

		ResponseEntity<Customer> customer = restTemplate.exchange("http://CUSTOMERSERVICE/customer/{id}",
				HttpMethod.GET, new HttpEntity(idCustomer), Customer.class, map);

		map = new HashMap();

		map.put("id", idProduct);

		ResponseEntity<Product> product = restTemplate.exchange("http://PRODUCTSERVICE/product/{id}", HttpMethod.GET,
				new HttpEntity(idProduct), Product.class, map);

		order.setCustomer(customer.getBody());
		order.setProduct(product.getBody());
		order.setId(id);
		order.setAmount(amount);
		order.setOrderDate(new Date());

		logger.warn("The order " + id + " for the client " + customer.getBody().getName() + " with the product "
				+ product.getBody().getSku() + " with the amount " + amount + " was created!");

		id++;

		return order;
	}
}
