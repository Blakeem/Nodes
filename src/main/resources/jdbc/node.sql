
CREATE TABLE node (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `parent` bigint UNSIGNED DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);