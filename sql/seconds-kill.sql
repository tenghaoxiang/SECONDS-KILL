/*用户信息表*/
    CREATE TABLE `user` (
	  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
	  `wx_id` varchar(128) NOT NULL COMMENT '微信ID',
	  `name` varchar(128) NOT NULL COMMENT '姓名',
	  `head_img` varchar(512) NOT NULL COMMENT '头像url',
	  `sex` tinyint(4) NOT NULL COMMENT '性别',
      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '注册时间',
      `address` varchar(512) NOT NULL COMMENT '收货地址',
	  PRIMARY KEY (`id`),
	  KEY `wx_id` (`wx_id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*库存商品表*/
    CREATE TABLE `stock` (
        `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '商品ID',
        `name` varchar(64) NOT NULL COMMENT '商品名称',
        `price` int(11) not null comment '商品价钱',
        `count` int(11) NOT NULL COMMENT '库存量',
        `sale` int(11) NOT NULL DEFAULT '0' COMMENT '已售',
        `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
        PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*订单表*/
    CREATE TABLE `order` (
        `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单ID',
        `user_id` varchar(128) NOT NULL COMMENT '用户微信ID',
        `stock_id` int(11) unsigned NOT NULL COMMENT '商品ID',
        `address` varchar(512) NOT NULL COMMENT '收货地址',
        `price` int(11) unsigned NOT NULL COMMENT '商品价钱',
        `status` tinyint(2) NOT NULL COMMENT '订单状态：0 未支付，1已支付',
        `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
        `finish_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
        PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4