CREATE TABLE order_address (
    id UUID PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(255) NOT NULL,
    neighborhood VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    zip_code VARCHAR(255) NOT NULL,
    additional_info VARCHAR(255)
);

CREATE TABLE order_table (
    id UUID PRIMARY KEY,
    customer UUID NOT NULL,
    order_address UUID NOT NULL,
    total_price DECIMAL(19, 2) NOT NULL,
    status VARCHAR(255) NOT NULL,
    FOREIGN KEY (customer) REFERENCES customer (id),
    FOREIGN KEY (order_address) REFERENCES order_address (id)
);

CREATE TABLE order_item (
    id UUID PRIMARY KEY,
    order_table UUID NOT NULL,
    product UUID NOT NULL,
    quantity INTEGER NOT NULL,
    price_at_purchase DECIMAL(19, 2) NOT NULL,
    total_price DECIMAL(19, 2) NOT NULL,
    FOREIGN KEY (order_table) REFERENCES order_table (id),
    FOREIGN KEY (product) REFERENCES product (id)
);
