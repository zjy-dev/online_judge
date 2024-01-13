-- 创建
CREATE TABLE IF NOT EXISTS `problem` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `name` varchar(50) unique key,
                           `description` text,
                           `sampleInput` text,
                           `sampleOutput` text,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- 插入
INSERT INTO problem (name,description,sampleInput,sampleOutput) VALUES(#{name},#{description},#{sampleInput},#{sampleOutput});

-- 重置自增
ALTER TABLE problem AUTO_INCREMENT=1;