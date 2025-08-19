CREATE USER 'rootx'@'localhost' IDENTIFIED BY 'password1234' ;
GRANT ALL ON *.* TO 'rootx'@'localhost';
FLUSH PRIVILEGES;