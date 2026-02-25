CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('ROLE_USER','ROLE_ADMIN') NOT NULL,
    created_at DATETIME(6) NOT NULL,
    enabled BIT(1) NOT NULL,
    last_login DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE branch (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        address VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
);


CREATE TABLE product (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(255),
                         price DECIMAL(10,2) NOT NULL,
                         sku VARCHAR(255) NOT NULL,
                         PRIMARY KEY (id),
                         UNIQUE KEY uk_product_sku (sku)
);


CREATE TABLE sale (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      user_id BIGINT NOT NULL,
                      created_at DATETIME(6) NOT NULL,
                      closed_at DATETIME(6) DEFAULT NULL,
                      branch_id BIGINT NOT NULL,
                      status ENUM('OPEN','FINISHED','CANCELLED') NOT NULL,
                      PRIMARY KEY (id),
                      CONSTRAINT fk_sale_user
                          FOREIGN KEY (user_id) REFERENCES users(id),
                      CONSTRAINT fk_sale_branch
                          FOREIGN KEY (branch_id) REFERENCES branch(id)
);


CREATE TABLE sale_item (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           sale_id BIGINT NOT NULL,
                           product_id BIGINT NOT NULL,
                           quantity INT NOT NULL,
                           PRIMARY KEY (id),
                           CONSTRAINT fk_sale_item_sale
                               FOREIGN KEY (sale_id) REFERENCES sale(id),
                           CONSTRAINT fk_sale_item_product
                               FOREIGN KEY (product_id) REFERENCES product(id)
);