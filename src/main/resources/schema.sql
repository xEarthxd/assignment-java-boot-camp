DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id  INTEGER PRIMARY KEY,
    first_name  VARCHAR(64) NOT NULL,
    last_name   VARCHAR(64) NOT NULL,
    address VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);

DROP TABLE IF EXISTS product;

CREATE TABLE product (
    id  INTEGER PRIMARY KEY,
    name  VARCHAR(64) NOT NULL,
    price  FLOAT,
    description VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);

DROP TABLE IF EXISTS cart;

CREATE TABLE cart (
    id  INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id  INTEGER,
    created_at  TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

DROP TABLE IF EXISTS cart_item;

CREATE TABLE cart_item (
    id  INTEGER AUTO_INCREMENT PRIMARY KEY,
    cart_id  INTEGER,
    product_id INTEGER,
    quantity  INTEGER,
    created_at  TIMESTAMP,
    modified_at  TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES cart(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

DROP TABLE IF EXISTS `order`;

CREATE TABLE `order` (
    id  INTEGER PRIMARY KEY,
    user_id  INTEGER,
    status  INTEGER, -- Will be replaced with enum when implementing order flow
    created_at  TIMESTAMP,
    modified_at  TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

DROP TABLE IF EXISTS order_item;

CREATE TABLE order_item (
    id  INTEGER PRIMARY KEY,
    order_id INTEGER,
    product_id  INTEGER,
    quantity  INTEGER,
    created_at  TIMESTAMP,
    modified_at  TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);