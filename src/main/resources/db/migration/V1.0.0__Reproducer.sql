CREATE TABLE IF NOT EXISTS `user`
(
    `id`       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `email`    VARCHAR(255)    NOT NULL,
    `role` ENUM ('User', 'Admin') NOT NULL DEFAULT 'User'
);

CREATE TABLE IF NOT EXISTS `api_key`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`     BIGINT UNSIGNED NOT NULL,
    `name`        VARCHAR(255)    NOT NULL,
    `apikey`      VARCHAR(64)     NOT NULL,
    UNIQUE KEY `index_user_apikey_apikey` (`apikey`),
    CONSTRAINT `fk_user_user_apikey` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO `user` (`email`, `role`) VALUES ('admin@example.com', 'Admin');
INSERT INTO `api_key` (`user_id`, `name`, `apikey`) VALUES (1, 'test', '1234567890abcdef');
