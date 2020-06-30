drop database blog;
create database blog;

use blog;


DROP TABLE IF EXISTS `article`;
CREATE TABLE IF NOT EXISTS `article`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `image_url` VARCHAR(103) DEFAULT NULL,
   `introduce` VARCHAR(203) DEFAULT NULL,
   `title` VARCHAR(103) DEFAULT NULL,
   `content` VARCHAR(10003) DEFAULT NULL,
   `click` INT DEFAULT NULL,
   `date` DATE DEFAULT NULL,
   `is_show` INT(4) DEFAULT NULL,
   PRIMARY KEY ( `id` ),
   KEY (`click`),
   KEY (`date`),
   KEY (`is_show`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tag`;
CREATE TABLE IF NOT EXISTS `tag`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `name` VARCHAR(103) DEFAULT NULL,
   `color` VARCHAR(103) DEFAULT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE IF NOT EXISTS `article_tag`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `article_id` INT UNSIGNED DEFAULT NULL,
   `tag_id` INT UNSIGNED DEFAULT NULL,
   PRIMARY KEY ( `id` ),
   KEY (`article_id`),
   KEY (`tag_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `comment`;
CREATE TABLE IF NOT EXISTS `comment`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `headshot_id` INT(5) DEFAULT NULL,
   `name` VARCHAR(103) DEFAULT NULL,
   `Email` VARCHAR(103) DEFAULT NULL,
   `website` VARCHAR(103) DEFAULT NULL,
   `content` VARCHAR(10003) DEFAULT NULL,
   `ip` VARCHAR(103) DEFAULT NULL,
   `date` DATE DEFAULT NULL,
   `is_read` INT(4) DEFAULT NULL,
   `is_show` INT(4) DEFAULT NULL,
   `article_id` INT UNSIGNED DEFAULT NULL,
   PRIMARY KEY ( `id` ),
   KEY (`article_id`),
   KEY (`is_read`),
   KEY (`is_show`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `image`;
CREATE TABLE IF NOT EXISTS `image`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `name` VARCHAR(103) DEFAULT NULL,
   `url` VARCHAR(103) DEFAULT NULL,
   KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tag` VALUES (1, '技术', 'badge-primary');
INSERT INTO `tag` VALUES (2, '工具', 'badge-success');
INSERT INTO `tag` VALUES (3, '杂谈', 'badge-info');
INSERT INTO `tag` VALUES (4, '公告', 'badge-dark');
INSERT INTO `tag` VALUES (5, '关于', 'badge-light');

INSERT INTO `article` VALUES (1, '/images/defualt.png', '本博客的规则', '公告', '请于后台页面修改。', 0, '2020-1-1', 1);
INSERT INTO `article` VALUES (2, '/images/defualt.png', '关于本博客', '关于', '请于后台页面修改。', 0, '2020-1-1', 1);

INSERT INTO `article_tag` VALUES (1, 1, 4);
INSERT INTO `article_tag` VALUES (2, 2, 5);
