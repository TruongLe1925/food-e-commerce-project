USE `e-commerce`;
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `price` DECIMAL(15, 2) NOT NULL,
    `description` TEXT,
    `thumbnail_url` VARCHAR(255),
    `image_url` VARCHAR(255),
    `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `stock` INT DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `category` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `description` TEXT,
    `thumbnail` VARCHAR(255),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `product_category` (
    `product_id` INT NOT NULL,
    `category_id` INT NOT NULL,
    PRIMARY KEY (`product_id`, `category_id`),
    CONSTRAINT `fk_pc_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_pc_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

CREATE TABLE `option` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50),
    `price` DECIMAL(15, 2) DEFAULT 0.00,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `product_option` (
    `product_id` INT NOT NULL,
    `option_id` INT NOT NULL,
    PRIMARY KEY (`product_id`, `option_id`),
    CONSTRAINT `fk_po_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_po_option` FOREIGN KEY (`option_id`) REFERENCES `option` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

