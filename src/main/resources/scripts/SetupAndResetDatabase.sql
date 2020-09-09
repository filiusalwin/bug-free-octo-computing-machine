 drop schema bacchux; -- Drops the old database
 create database bacchux; -- Creates the new database
 create user 'userBacchUx'@'%' identified by 'pwUserBacchUx'; -- Creates the user
 grant all on bacchux.* to 'userBacchUx'@'%'; -- Gives all privileges to the new user on the newly created database
 use `bacchux`; -- Uses the new database