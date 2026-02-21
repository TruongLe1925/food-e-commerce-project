USE `e-commerce`;

-- Xóa bảng cũ để tạo lại với ràng buộc mới
DROP TABLE IF EXISTS `cart_items`;
DROP TABLE IF EXISTS `cart`;

-- 1. Tạo bảng cart với NO ACTION
CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_user_cart` FOREIGN KEY (`username`) REFERENCES `users` (`username`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Tạo bảng cart_items với NO ACTION
CREATE TABLE `cart_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cart_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` int DEFAULT '1',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_cart_ref` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_product_ref` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
ALTER TABLE `cart` ADD UNIQUE (`username`);