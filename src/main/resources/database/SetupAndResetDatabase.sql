 DROP SCHEMA bacchux; -- Drops the old database
 CREATE DATABASE bacchux; -- Creates the new database
 CREATE USER 'userBacchUx'@'%' IDENTIFIED BY 'pwUserBacchUx'; -- Creates the user
 GRANT ALL ON bacchux.* TO 'userBacchUx'@'%'; -- Gives all privileges to the new user on the newly created database
 USE bacchux; -- Uses the new database

-- Insert categories
INSERT INTO `bacchux`.`category` (`category_id`, `name`) VALUES ('1', 'Alcohol');
INSERT INTO `bacchux`.`category` (`category_id`, `name`) VALUES ('2', 'Softdrink');
INSERT INTO `bacchux`.`category` (`category_id`, `name`) VALUES ('3', 'Food');

-- Insert products
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('1','Heineken',350,'1');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('2','Amstel',350,'1');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('4','Wijn',270,'1');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('3','Fris',130,'2');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('5','Borrelnootjes',300,'3');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('6','Tosti Kaas',200,'3');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('7','Tosti Ham & Kaas',250,'3');
