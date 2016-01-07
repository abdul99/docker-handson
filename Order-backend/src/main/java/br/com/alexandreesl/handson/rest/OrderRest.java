package br.com.alexandreesl.handson.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.netflix.appinfo.InstanceInfo;
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

	@RequestMapping(value = "order/{idCustomer}/{idProduct}/{amount}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Order submitOrder(@PathVariable long idCustomer, @PathVariable long idProduct, @PathVariable long amount) {

		List<InstanceInfo> instances = discoveryClient.getInstancesById("CUSTOMERSERVICE");

		for (InstanceInfo info : instances) {
			
			System.out.println(info.getIPAddr());
			System.out.println(info.getPort());

		}

		Order order = new Order();

		Map map = new HashMap();

		map.put("id", idCustomer);

		ResponseEntity<Customer> customer = restTemplate.exchange("http://CUSTOMERSERVICE/customer/{id}",
				HttpMethod.GET, new HttpEntity(idCustomer), Customer.class, map);

		Product product = restTemplate.getForObject("http://localhost:8082/product?id={id}", Product.class, idProduct);

		order.setCustomer(customer.getBody());
		order.setProduct(product);
		order.setId(id);
		order.setAmount(amount);
		order.setOrderDate(new Date());

		id++;

		return order;
	}
}
