-- 創建記錄表
CREATE TABLE IF NOT EXISTS record (
                                      subject VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(7) NOT NULL,
    times INT NOT NULL
    );

-- 插入範例數據，僅當表格沒有任何記錄時
INSERT OR IGNORE INTO record (subject, name, color, times)
SELECT 'JAVA', 'JAVA小怪', '#386f54', 60
UNION ALL
SELECT 'Algorithm', 'Tab', '#814e51', 15
UNION ALL
SELECT 'Python', 'BEBE', '#ffb82e', 100
UNION ALL
SELECT 'Films', 'Snowball', '#1f2937', 37
    WHERE NOT EXISTS (SELECT 1 FROM record);
