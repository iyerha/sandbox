DROP TABLE if exists order_item;
DROP TABLE if exists orders;

CREATE TABLE orders (
	id INT PRIMARY KEY,
	order_dt TIMESTAMP NOT NULL
);

CREATE TABLE order_item (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	order_id INT NOT NULL,
	quantity INT NOT NULL,
	product_id VARCHAR(50) NOT NULL,
	cost NUMERIC(8,2) NOT NULL,
	foreign key (order_id) references orders(id)
);

INSERT INTO orders (id, order_dt) values(1, CURRENT_TIMESTAMP);
INSERT INTO order_item (order_id, product_id, quantity, cost) values (1, 'pen', 10, 5.0);
INSERT INTO order_item (order_id, product_id, quantity, cost) values (1, 'pencil', 5, 3.0);
INSERT INTO order_item (order_id, product_id, quantity, cost) values (1, 'eraser', 2, 10.0);
INSERT INTO orders (id, order_dt) values(2, CURRENT_TIMESTAMP);
INSERT INTO order_item (order_id, product_id, quantity, cost) values (2, 'pen', 25, 12.0);
INSERT INTO order_item (order_id, product_id, quantity, cost) values (2, 'staple', 50, 20.0);
INSERT INTO orders (id, order_dt) values(3, CURRENT_TIMESTAMP);
INSERT INTO order_item (order_id, product_id, quantity, cost) values (3, 'pencil', 10, 15.0);
INSERT INTO order_item (order_id, product_id, quantity, cost) values (3, 'eraser', 5, 20.0);
INSERT INTO orders (id, order_dt) values(4, CURRENT_TIMESTAMP);
INSERT INTO order_item (order_id, product_id, quantity, cost) values (4, 'pen', 25, 12.0);