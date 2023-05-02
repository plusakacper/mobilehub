SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                         `address` varchar(255) COLLATE utf8_bin NOT NULL,
                         `comments` varchar(500) COLLATE utf8_bin DEFAULT NULL,
                         `created_at` datetime(6) NOT NULL,
                         `number` varchar(255) COLLATE utf8_bin NOT NULL,
                         `user` bigint(20) NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `FK7ypwt90ivk5ihxv6kayioi8rv` (`user`),
                         CONSTRAINT `FK7ypwt90ivk5ihxv6kayioi8rv` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for order_product
-- ----------------------------
DROP TABLE IF EXISTS `order_product`;
CREATE TABLE `order_product` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `quantity` varchar(255) COLLATE utf8_bin NOT NULL,
                                 `order` bigint(20) DEFAULT NULL,
                                 `product` bigint(20) NOT NULL,
                                 `user` bigint(20) NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `FKnbl338nosk066978lvyyfpl6i` (`order`),
                                 KEY `FKjh9xhp6tq400tn2kshpjdt5e3` (`product`),
                                 KEY `FK75u3hnrck25noyfvlaxmw64fd` (`user`),
                                 CONSTRAINT `FK75u3hnrck25noyfvlaxmw64fd` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
                                 CONSTRAINT `FKjh9xhp6tq400tn2kshpjdt5e3` FOREIGN KEY (`product`) REFERENCES `product` (`id`),
                                 CONSTRAINT `FKnbl338nosk066978lvyyfpl6i` FOREIGN KEY (`order`) REFERENCES `order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `active` bit(1) DEFAULT NULL,
                           `description` varchar(500) COLLATE utf8_bin DEFAULT NULL,
                           `filename` varchar(255) COLLATE utf8_bin DEFAULT NULL,
                           `name` varchar(255) COLLATE utf8_bin NOT NULL,
                           `price` varchar(255) COLLATE utf8_bin NOT NULL,
                           `product_category` bigint(20) NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKbvdd7owwhvkamc6unq83xdcrj` (`product_category`),
                           CONSTRAINT `FKbvdd7owwhvkamc6unq83xdcrj` FOREIGN KEY (`product_category`) REFERENCES `product_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                    `display_name` varchar(255) COLLATE utf8_bin NOT NULL,
                                    `name` varchar(255) COLLATE utf8_bin NOT NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `name` varchar(255) COLLATE utf8_bin NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `email` varchar(255) COLLATE utf8_bin NOT NULL,
                        `firstname` varchar(255) COLLATE utf8_bin NOT NULL,
                        `lastname` varchar(255) COLLATE utf8_bin NOT NULL,
                        `password` varchar(255) COLLATE utf8_bin NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
                             `user_id` bigint(20) NOT NULL,
                             `role_id` bigint(20) NOT NULL,
                             KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
                             KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
                             CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                             CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records
-- ----------------------------

INSERT INTO `role` (`id`, `name`) VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');

INSERT INTO `product_category` (`id`, `display_name`, `name`) VALUES (1,'Flagship Smartphones','Flagship Smartphones'),(2,'Budget Smartphones','Budget Smartphones');

INSERT INTO `product` (`id`, `active`, `description`, `filename`, `name`, `price`, `product_category`)
VALUES
(1,_binary '','The latest and greatest smartphone from Apple with a stunning 6.7-inch OLED display, A15 Bionic chip, and powerful cameras. It features a tri-camera system with 12MP ultra wide, wide, and telephoto lenses, 5G capabilities, and a ceramic shield front cover for added durability.','iphone.jpg','iPhone 12 Pro Max','1099',1),
(2,_binary '','Samsung\'s flagship smartphone with a 6.8-inch Dynamic AMOLED display, Exynos 2100 or Snapdragon 888 processor, and impressive camera capabilities. The S21 Ultra features a quad-camera system with 108MP primary lens, 10MP telephoto lens, 10MP telephoto lens, and 12MP ultra wide lens. It also supports 5G and has a long-lasting battery life.','samsung.jpg','Samsung Galaxy S21 Ultra','1199',1),
(3,_binary '','A sleek and powerful smartphone from Google with a 6.4-inch OLED display, the latest and greatest Google software, and a great camera experience. It features a dual-camera system with 12.2MP primary and 16MP ultra-wide lenses, a fast and smooth Google interface, and 5G capabilities.','google.jpg','Google Pixel 6 Pro','799',2),
(4,_binary '','A flagship killer with a 6.7-inch AMOLED display, Snapdragon 888 processor, and excellent camera capabilities. It features a quad-camera system with 48MP primary lens, 50MP ultra-wide lens, 8MP telephoto lens, and 2MP monochrome lens. It also supports fast charging and has a sleek and stylish design.','oneplus.jpg','OnePlus 9 Pro','969',2),
(5,_binary '','A premium smartphone from Xiaomi with a 6.81-inch AMOLED display, Snapdragon 888 processor, and impressive camera features. It features a triple-camera system with 108MP primary lens, 13MP ultra-wide lens, and 5MP telephoto lens. It also supports 5G and has a long-lasting battery life.','xiaomi.jpg','Xiaomi Mi 11','749',2),
(6,_binary '','A cutting-edge smartphone from Oppo with a 6.7-inch AMOLED display, Snapdragon 888 processor, and innovative camera capabilities. It features a quad-camera system with 50MP primary lens, 50MP ultra-wide lens, 13MP telephoto lens, and 2MP monochrome lens. It also supports 5G and has a unique and stylish design.','oppo.jpg','Oppo Find X3 Pro','1099',1);

INSERT INTO `user` (`id`, `email`, `firstname`, `lastname`, `password`) VALUES (1,'mobilehub@gmail.com','Super','Admin','$2a$10$NY3Te5472/bhqexdlGRP0e.6y4nS2VXN.gLn8dO0Er6kNKxsnFMKi'),(2,'testuser@gmail.com','Test','User','$2a$10$2XkFtcjwId8B3zMpQOihFeF8UB29J7Vc5l9eZgDLwVYWTE0ffHyl2');
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (1,1),(1,2),(2,2);


