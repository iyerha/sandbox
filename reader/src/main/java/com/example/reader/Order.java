package com.example.reader;

import java.util.Date;

public class Order {
	private int id;
	private String products;
	private Date orderDt;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProducts() {
		return products;
	}
	public void setProducts(String products) {
		this.products = products;
	}
	public Date getOrderDt() {
		return orderDt;
	}
	public void setOrderDt(Date orderDt) {
		this.orderDt = orderDt;
	}
	@Override
	public String toString() {
		return id+" "+products+" "+orderDt;
	}

}
