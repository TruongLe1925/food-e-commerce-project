use `e-commerce`;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `orders` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `customer_id` INT,
    `status_id` INT,
    `order_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `order_address` VARCHAR(255),
    `note` TEXT,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_orders_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer_details` (`id`),
    CONSTRAINT `fk_orders_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `order_details` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `product_id` INT,
    `promotion_id` INT,
    `order_id` INT,
    `quantity` INT NOT NULL,
    `original_price` DECIMAL(15, 2),
    `discount_price` DECIMAL(15, 2),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_od_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
    CONSTRAINT `fk_od_promotion` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`),
    CONSTRAINT `fk_od_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `promotion` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) DEFAULT NULL,
  `discount_type` VARCHAR(20) DEFAULT NULL,
  `discount_value` DECIMAL(15,2) DEFAULT NULL,
  `start_date` DATETIME DEFAULT NULL,
  `end_date` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `orders` 
ADD COLUMN `promotion_id` INT,
`original_price` DECIMAL(15, 2),
`discount_price` DECIMAL(15, 2);
ADD CONSTRAINT `fk_orders_promotion` 
FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`)
ON DELETE SET NULL ON UPDATE NO ACTION;
ALTER TABLE `orders` 
ADD COLUMN `original_price` DECIMAL(15, 2),
ADD COLUMN `discount_price` DECIMAL(15, 2);

ALTER TABLE `order_details` 
DROP FOREIGN KEY `fk_od_promotion`;

-- 2. Xóa 2 cột không cần thiết
ALTER TABLE `orders_details` 
DROP COLUMN `promotion_id`;
