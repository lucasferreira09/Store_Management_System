create type stock_movement_type as enum ('IN', 'OUT', 'ADJUSTMENT');
create type stock_movement_reason as enum ('SALE', 'RESTOCK', 'BROKEN', 'STOLEN', 'REFUNDED', 'ORDER_CANCELLED');
create type payment_provider as enum ('STRIPE', 'PAG_SEGURO');
create type payment_method_type as enum ('CARD', 'PIX', 'BOLETO', 'PAYPAL_BALANCE');
create type payment_status as enum ('APPROVED', 'FAILED', 'PENDING', 'CANCELLED', 'REFUNDED');
create type order_status as enum ('AWAITING_PAYMENT', 'PAID', 'CANCELLED', 'COMPLETED');

create table category (
    id serial primary key,
    name varchar(50) not null unique,
    active boolean not null default true
);

create table product (
    id serial primary key,
    name varchar(100) not null,
    description varchar(300),
    barcode varchar(50) unique not null,
    photo varchar(255),
    sale_price numeric(10,2) constraint chk_sale_price_positive check (sale_price > 0),
    cost_price numeric(10,2) constraint chk_cost_price_positive check (cost_price > 0),
    active boolean not null default true,
    category_id INTEGER not null,
    constraint fk_category foreign key (category_id) references category (id)
);

create table customer (
    id serial primary key,
    name varchar(100) not null,
    cpf varchar(11) unique not null constraint check_length_cpf check (length(cpf)=11),
    phone_number varchar(20) not null,
    email varchar(100) unique not null,
    active boolean not null default true
);

create table address (
     id serial primary key,
     street varchar(100) not null,
     neighbourhood varchar(100) not null,
     complement varchar(20),
     number varchar(10),
     city varchar(50) not null,
     state varchar(50) not null,
     postal_code varchar(20) not null
);

create table customer_address (
    id seriaL primary key,
    customer_id INTEGER not null,
    address_id INTEGER not null,
    constraint fk_customer foreign key (customer_id) references customer (id),
    constraint fk_address foreign key (address_id) references address (id) ON DELETE RESTRICT
);

create table store (
   id serial primary key,
   name varchar(100) not null,
   cnpj varchar(13) unique not null constraint check_length_cnpj check (length(cnpj)=13),
   phone_number varchar(20) not null,
   email varchar(100) unique not null,
   address_id INTEGER null,
   active boolean not null default true,
   constraint fk_address foreign key (address_id) references address (id)
);

create table inventory (
   id serial primary key,
   quantity INTEGER not null constraint check_quantity_positive check(quantity>=0),
   store_id INTEGER not null,
   product_id INTEGER not null,
   active boolean not null default true,
   constraint fk_store foreign key (store_id) references store (id),
   constraint fk_product foreign key (product_id) references product (id),
   constraint unique_store_product unique (store_id, product_id)
);

create table orders (
    id serial primary key,
    status varchar(20) not null,
    created_at TIMESTAMPTZ not null,
    total_amount numeric(10,2) not null,
    street VARCHAR(100) not null,
    number VARCHAR(10),
    city VARCHAR(50) not null,
    state VARCHAR(50) not null,
    postal_code VARCHAR(20) not null,
    checkout_id uuid not null,
    customer_id INTEGER not null,
    store_id INTEGER not null,
    constraint fk_customer foreign key (customer_id) references customer (id),
    constraint fk_store foreign key (store_id) references store (id)
);

create table order_item (
    id serial primary key,
    quantity INTEGER not null check (quantity > 0),
    sale_price numeric(10,2) not null,
    order_id INTEGER not null,
    product_id INTEGER not null,
    store_id INTEGER not null,
    constraint fk_order foreign key (order_id) references orders (id) on delete cascade,
    constraint fk_product foreign key (product_id) references product (id),
    constraint fk_store foreign key (store_id) references store (id),
    constraint unique_order_product unique (order_id, product_id)
);

create table payment (
    id serial primary key,
    amount numeric(10,2) not null,
    currency VARCHAR(5) not null,
    checkout_id uuid null,
    payment_status VARCHAR(15) not null,
    payment_provider VARCHAR(20) not null,
    provider_session_id VARCHAR(255) null,
    created_at TIMESTAMPTZ not null,
    paid_at TIMESTAMPTZ null
);

create table transaction (
    id SERIAL PRIMARY KEY,
    amount NUMERIC(10,2),
    payment_status VARCHAR(15) not null,
    payment_provider VARCHAR(20) not null,
    payment_method_type VARCHAR(15) not null,
    provider_payment_id VARCHAR(255) null,
    provider_session_id VARCHAR(255) not null,
    provider_charge_id VARCHAR(255) null,
    card_brand VARCHAR(10) null,
    las4 VARCHAR(4) null,
    created_at TIMESTAMPTZ not null,
    payment_message VARCHAR(255) null,
    payment_id INTEGER null,
    constraint fk_payment foreign key (payment_id) references payment (id)
);

create table stock_movement_history(
    id serial primary key,
    type stock_movement_type not null,
    reason stock_movement_reason not null,
    quantity INTEGER not null constraint check_valid_quantity check(quantity >= 0),
    order_id INTEGER null,
    created_at TIMESTAMPTZ not null,
    description VARCHAR(255) null,
    inventory_id INTEGER not null,
    constraint fk_inventory foreign key (inventory_id) references inventory (id)
);

