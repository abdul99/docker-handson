package br.com.alexandreesl.handson.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ProductRest {

	private static List<Product> products = new ArrayList<Product>();

	static {

		Product product1 = new Product();
		product1.setId(1);
		product1.setSku("abcd1");
		product1.setDescription("Produto1");

		Product product2 = new Product();
		product2.setId(2);
		product2.setSku("abcd2");
		product2.setDescription("Produto2");

		Product product3 = new Product();
		product3.setId(3);
		product3.setSku("abcd3");
		product3.setDescription("Produto3");

		Product product4 = new Product();
		product4.setId(4);
		product4.setSku("abcd4");
		product4.setDescription("Produto4");

		products.add(product1);
		products.add(product2);
		products.add(product3);
		products.add(product4);

	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> getProdutos() {
		return products;
	}

	@RequestMapping(value = "product/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Product getProduto(@PathVariable long id) {

		Product prod = null;

		for (Product p : products) {

			if (p.getId() == id)
				prod = p;

		}

		return prod;
	}

}
