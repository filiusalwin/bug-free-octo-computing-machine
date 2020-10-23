DROP SCHEMA bacchux;
CREATE DATABASE bacchux;
DROP USER `userBacchUx`;
CREATE USER 'userBacchUx'@'%' IDENTIFIED BY 'pwUserBacchUx';
GRANT ALL ON bacchux.* TO 'userBacchUx'@'%';
-- run spring after this so hibernate can make the tables

-- Insert categories
INSERT INTO `bacchux`.`category` (`category_id`, `name`) VALUES ('1', 'Bier');
INSERT INTO `bacchux`.`category` (`category_id`, `name`) VALUES ('2', 'Wijn');
INSERT INTO `bacchux`.`category` (`category_id`, `name`) VALUES ('3', 'Softdrink');
INSERT INTO `bacchux`.`category` (`category_id`, `name`) VALUES ('4', 'Snacks');
INSERT INTO `bacchux`.`category` (`category_id`, `name`) VALUES ('5', 'Burgers');

-- Insert products
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('1','Grolsch',270,'1');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('2','Amstel',230,'1');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('3','Leffe Tripel',450,'1');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('4','Baxbier Kon Minder Citra',480,'1');

INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('5','Zoete Witte Wijn',320,'2');
INSERT INTO bacchux.product (product_id,name,price,category_id)
    VALUES ('6','Merlot',370,'2');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('7','Cabernet Sauvignon',400,'2');

INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('8','Cola',200,'3');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('9','Fanta',180,'3');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('10','Red Bull',300,'3');

INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('11','Borrelnoten',250,'4');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('12','Tosti Kaas',150,'4');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('13','Tosti Ham & Kaas',180,'4');

INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('14','Cheeseburger',450,'5');
INSERT INTO bacchux.product (product_id,name,price,category_id)
	VALUES ('15','Veggieburger',350,'5');

