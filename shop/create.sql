create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
create table item (id integer not null, code varchar(255), info varchar(255), is_deleted varchar(255), name varchar(255), stock integer, primary key (id)) engine=InnoDB
create table shopping_cart (id integer not null, inserted_date datetime, payment_method varchar(255), payment_status bit, id_user integer not null, primary key (id)) engine=InnoDB
create table shopping_cart_detail (id integer not null, id_shoppingcart integer, is_deleted bit, id_item integer not null, primary key (id)) engine=InnoDB
create table user (id integer not null, is_active bit, password varchar(255), role varchar(255), username varchar(255), primary key (id)) engine=InnoDB
alter table shopping_cart add constraint FKt2se07mu1msfnd7gyfxnhpoi foreign key (id_user) references user (id)
alter table shopping_cart_detail add constraint FKkdrven1laaja7ndh1huhmqtu9 foreign key (id_item) references item (id)
