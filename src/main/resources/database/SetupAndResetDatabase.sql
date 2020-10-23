DROP SCHEMA bacchux;
CREATE DATABASE bacchux;
DROP USER `userBacchUx`;
CREATE USER 'userBacchUx'@'%' IDENTIFIED BY 'pwUserBacchUx';
GRANT ALL ON bacchux.* TO 'userBacchUx'@'%';