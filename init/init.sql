SET NAMES 'utf8mb4';
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS `e-commerce`;
CREATE DATABASE `e-commerce` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `e-commerce`;

-- 1. User Entity
CREATE TABLE `users` (
    `username` VARCHAR(50) PRIMARY KEY,
    `password` VARCHAR(255) NOT NULL,
    `enabled` BOOLEAN NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Authority Entity
CREATE TABLE `authorities` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `authority` ENUM('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_CUSTOMER') NOT NULL,
    `username` VARCHAR(50) NOT NULL,
    CONSTRAINT `fk_authorities_users` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. CustomerDetails Entity
CREATE TABLE `customer_details` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `full_name` VARCHAR(255),
    `address` VARCHAR(500),
    `email` VARCHAR(255) UNIQUE,
    `phone_number` VARCHAR(50),
    `users_id` VARCHAR(50),
    CONSTRAINT `fk_customer_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Category Entity
CREATE TABLE `category` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `description` TEXT,
    `thumbnail` VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Product Entity
CREATE TABLE `product` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `price` DECIMAL(15,2) NOT NULL,
    `stock` INT DEFAULT 0,
    `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `thumbnail_url` VARCHAR(255),
    `image_url` VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Product-Category Join Table
CREATE TABLE `product_category` (
    `product_id` INT NOT NULL,
    `category_id` INT NOT NULL,
    PRIMARY KEY (`product_id`, `category_id`),
    CONSTRAINT `fk_pc_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
    CONSTRAINT `fk_pc_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Option Entity
CREATE TABLE `option` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50),
    `price` VARCHAR(255) DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. Product-Option Join Table
CREATE TABLE `product_option` (
    `product_id` INT NOT NULL,
    `option_id` INT NOT NULL,
    PRIMARY KEY (`product_id`, `option_id`),
    CONSTRAINT `fk_po_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_po_option` FOREIGN KEY (`option_id`) REFERENCES `option` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. Banner Entity
CREATE TABLE `banner` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `image_url` VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. Status Entity
CREATE TABLE `status` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` ENUM('PENDING', 'CONFIRMED', 'PROCESSING', 'COMPLETED', 'CANCELLED') NOT NULL,
    `description` VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 11. Promotion Entity
CREATE TABLE `promotion` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100),
    `discount_type` ENUM('PERCENTAGE', 'FIXED_AMOUNT'),
    `discount_value` DECIMAL(15,2),
    `start_date` DATE,
    `end_date` DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 12. Orders Entity
CREATE TABLE `orders` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `order_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `order_address` VARCHAR(255),
    `note` TEXT,
    `original_price` DECIMAL(15,2),
    `discount_price` DECIMAL(15,2),
    `customer_id` INT,
    `status_id` INT,
    `promotion_id` INT,
    CONSTRAINT `fk_orders_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer_details` (`id`),
    CONSTRAINT `fk_orders_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
    CONSTRAINT `fk_orders_promotion` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 13. OrderDetails Entity
CREATE TABLE `order_details` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `quantity` INT NOT NULL,
    `original_price` DECIMAL(15,2),
    `discount_price` DECIMAL(15,2),
    `product_id` INT,
    `order_id` INT,
    CONSTRAINT `fk_od_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
    CONSTRAINT `fk_od_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 14. Cart Entity
CREATE TABLE `cart` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `username` VARCHAR(50),
    CONSTRAINT `fk_cart_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 15. CartItems Entity
CREATE TABLE `cart_items` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `quantity` INT DEFAULT 1,
    `product_id` INT,
    `cart_id` INT,
    CONSTRAINT `fk_ci_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
    CONSTRAINT `fk_ci_cart` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 16. Sample Data
-- Status data
INSERT INTO `status` (name, description) VALUES
('PENDING', 'Đơn hàng mới chờ xác nhận.'),
('CONFIRMED', 'Đơn hàng đã xác nhận.'),
('PROCESSING', 'Đang chuẩn bị hàng.'),
('COMPLETED', 'Giao hàng thành công.'),
('CANCELLED', 'Đã hủy.');

-- Promotion data
INSERT INTO `promotion` (name, discount_type, discount_value, start_date, end_date) VALUES
('Chương trình Hè Rực Rỡ', 'PERCENTAGE', 20.00, '2026-06-01', '2026-08-31'),
('Ưu đãi Khách hàng Mới', 'FIXED_AMOUNT', 50000.00, '2026-01-01', '2026-12-31');

-- Users data (mật khẩu đã được mã hóa BCrypt)
INSERT INTO `users` (username, password, enabled) VALUES
('admin', '$2a$12$ncWIjLs8iKwM4W.rA92vQulVWW4PuXXvbmzulDzTajGItURZ0TSSS', true),
('customer1', '$2a$12$ncWIjLs8iKwM4W.rA92vQulVWW4PuXXvbmzulDzTajGItURZ0TSSS', true),
('customer2', '$2a$12$ncWIjLs8iKwM4W.rA92vQulVWW4PuXXvbmzulDzTajGItURZ0TSSS', true);

-- Authorities data
INSERT INTO `authorities` (authority, username) VALUES
('ROLE_ADMIN', 'admin'),
('ROLE_MANAGER', 'admin'),
('ROLE_CUSTOMER', 'customer1'),
('ROLE_CUSTOMER', 'customer2');

-- Customer Details data
INSERT INTO `customer_details` (full_name, address, email, phone_number, users_id) VALUES
('Nguyễn Văn An', '123 Nguyễn Huệ, Quận 1, TP.HCM', 'an.nguyen@email.com', '0901234567', 'customer1'),
('Trần Thị Bình', '456 Lê Lợi, Quận 3, TP.HCM', 'binh.tran@email.com', '0912345678', 'customer2');

-- Categories data
INSERT INTO `category` (name, description, thumbnail) VALUES
('Cơm', 'Các món cơm nóng hổi', '/images/category/com-ga.jpg'),
('Gà', 'Các món gà hấp dẫn', '/images/category/ga-ran.jpg'),
('Bánh Mì', 'Bánh mì Việt Nam truyền thống', '/images/category/1773588128201_Banh-Mi_HERO.webp'),
('Cà Phê', 'Cà phê Việt Nam đậm đà', '/images/category/1773475146011_Bac-xiu-la-gi-nguon-goc-va-cach-lam-bac-xiu-thom-ngon-don-gian-tai-nha-5-800x529.jpg'),
('Nước Ngọt', 'Các loại nước giải khát', '/images/category/1773475122757_pepsi-boisson-gazeuse-au-cola-6-x-33-cl.jpg'),
('Khô Gà', 'Món khô gà cay nồng', '/images/category/kho-ga.jpg');

-- Products data
INSERT INTO `product` (name, price, description, thumbnail_url, image_url, stock) VALUES
('Cơm Gà Xối Mỡ', 45000.00, 'Cơm gà xối mỡ thơm ngon, da giòn, thịt mềm', 'om-Ga-Xoi-Mo-77-1.jpg', 'Com-Ga-Xoi-Mo-77-1.jpg', 50),
('Gà Rán KFC', 55000.00, 'Gà rán giòn rụm, vị đậm đà', 'cach-chien-ga-kfc-5.webp', 'cach-chien-ga-kfc-5.webp', 40),
('Bánh Mì Kẹp', 25000.00, 'Bánh mì Việt Nam với pate, thịt nguội, rau thơm', '1773598565473_Banh-Mi_HERO.webp', '1773598565473_Banh-Mi_HERO.webp', 60),
('Bạc Xỉu', 28000.00, 'Cà phê sữa đá đặc trưng Sài Gòn', '1773473619280_Bac-xiu-la-gi-nguon-goc-va-cach-lam-bac-xiu-thom-ngon-don-gian-tai-nha-5-800x529.jpg', '/images/product/1773473619280_Bac-xiu-la-gi-nguon-goc-va-cach-lam-bac-xiu-thom-ngon-don-gian-tai-nha-5-800x529.jpg', 100),
('Pepsi 330ml', 12000.00, 'Nước ngọt Pepsi lạnh giải khát', 'thumnail.webp', 'thumnail.webp', 200),
('Khô Gà Cay', 35000.00, 'Khô gà lá chanh cay nồng, vị đậm đà', 'cach-lam-kho-ga-6.jpg', 'cach-lam-kho-ga-6.jpg', 30),
('Gà Lạc Phô Mai', 40000.00, 'Gà lạc phô mai giòn tan, béo ngậy', '1773429491999_ga-lac-pho-mai.jpg', '1773429491999_ga-lac-pho-mai.jpg', 45),
('Trà Đào', 22000.00, 'Trà đào tươi mát, ngọt thanh', '1773714700045_ly-tra-dao-0b4abead.jpg', '1773714700045_ly-tra-dao-0b4abead.jpg', 80);

-- Product-Category Relationships
INSERT INTO `product_category` (product_id, category_id) VALUES
(1, 1), -- Cơm Gà Xối Mỡ -> Cơm
(1, 2), -- Cơm Gà Xối Mỡ -> Gà
(2, 2), -- Gà Rán KFC -> Gà
(3, 3), -- Bánh Mì Kẹp -> Bánh Mì
(4, 4), -- Bạc Xỉu -> Cà Phê
(5, 5), -- Pepsi -> Nước Ngọt
(6, 6), -- Khô Gà Cay -> Khô Gà
(7, 2), -- Gà Lạc Phô Mai -> Gà
(8, 5); -- Trà Đào -> Nước Ngọt

-- Options data
INSERT INTO `option` (name, price) VALUES
('Size L', '5000.00'),
('Size XL', '10000.00'),
('Thêm trứng', '8000.00'),
('Thêm phô mai', '5000.00'),
('Không hành', '0.00'),
('Ít đường', '0.00'),
('Nhiều đá', '0.00'),
('Ít đá', '0.00');

-- Product-Option Relationships
INSERT INTO `product_option` (product_id, option_id) VALUES
(1, 1), -- Cơm Gà Xối Mỡ -> Size L
(1, 2), -- Cơm Gà Xối Mỡ -> Size XL
(1, 3), -- Cơm Gà Xối Mỡ -> Thêm trứng
(2, 1), -- Gà Rán KFC -> Size L
(2, 2), -- Gà Rán KFC -> Size XL
(3, 3), -- Bánh Mì Kẹp -> Thêm trứng
(3, 4), -- Bánh Mì Kẹp -> Thêm phô mai
(4, 6), -- Bạc Xỉu -> Ít đường
(4, 7), -- Bạc Xỉu -> Nhiều đá
(4, 8), -- Bạc Xỉu -> Ít đá
(5, 7), -- Pepsi -> Nhiều đá
(5, 8), -- Pepsi -> Ít đá
(8, 6), -- Trà Đào -> Ít đường
(8, 7), -- Trà Đào -> Nhiều đá
(8, 8); -- Trà Đào -> Ít đá

-- Banners data
INSERT INTO `banner` (image_url) VALUES
    ('1773901420572_9f5d9b57101328c5102c2e34e2bcd0cd.jpg');

SET FOREIGN_KEY_CHECKS = 1;