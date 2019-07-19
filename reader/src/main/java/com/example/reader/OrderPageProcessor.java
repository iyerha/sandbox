package com.example.reader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class OrderPageProcessor implements PageProcessor<Order> {
	private NamedParameterJdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public void process(List<Order> page) {
		Map<Integer, List<String>> products = new HashMap<>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", page.stream().map(o -> o.getId()).collect(Collectors.toList()));
		jdbcTemplate.query("select product_id, order_id from order_item where order_id in (:ids) order by order_id", 
				parameters,
				new ProductExtractor(products));
		page.stream().forEach(o -> o.setProducts(
				products.get(o.getId()).stream().collect(Collectors.joining(","))));
	}
	
	private class ProductExtractor implements ResultSetExtractor<Void> {
		private Map<Integer, List<String>> products;
		public ProductExtractor(Map<Integer, List<String>> products) {
			this.products = products;
		}
		@Override
		public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
			int groupId = 0;
			List<String> productIds = null;
			while(rs.next()) {
				if (groupId != rs.getInt("order_id")) {
					if (groupId != 0) {
						products.put(groupId, productIds);
					}
					productIds = new ArrayList<>();
					groupId = rs.getInt("order_id");
				}
				productIds.add(rs.getString("product_id"));
			}
			if (groupId != 0) {
				products.put(groupId, productIds);
			}
			return null;
		}
	}

}
