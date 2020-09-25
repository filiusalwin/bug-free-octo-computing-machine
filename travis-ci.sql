 DROP SCHEMA bacchux; -- Drops the old database
 CREATE DATABASE bacchux; -- Creates the new database
 CREATE USER 'userBacchUx'@'%' IDENTIFIED BY 'pwUserBacchUx'; -- Creates the user
 GRANT ALL ON bacchux.* TO 'userBacchUx'@'%'; -- Gives all privileges to the new user on the newly created database
 USE `bacchux`; -- Uses the new database