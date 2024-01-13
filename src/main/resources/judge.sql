-- 建表
CREATE TABLE IF NOT EXISTS `judge` (
                         `id` int NOT NULL,
                         `input` text,
                         `output` text,
                         `timeLimit` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- 插入
INSERT INTO judge (input, output, timeLimit) VALUES(#{input},#{output},#{timeLimit);
