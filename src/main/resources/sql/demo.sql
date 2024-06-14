-- 創建記錄表
CREATE TABLE IF NOT EXISTS record (
    subject VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(7) NOT NULL,
    times INT NOT NULL
    );

-- 插入範例數據
INSERT OR IGNORE INTO record (subject, name, color, times) VALUES
('Math', 'Fluffy', '#FFAABB', 30),
('Science', 'Whiskers', '#00FF00', 15),
('English', 'Felix', '#0000FF', 100),
('History', 'Snowball', '#FF0000', 25);
