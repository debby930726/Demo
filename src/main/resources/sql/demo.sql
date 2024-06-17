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

-- 創建寵物對話表
CREATE TABLE IF NOT EXISTS petDialogues (
     id INTEGER PRIMARY KEY AUTOINCREMENT,
     dialogue TEXT NOT NULL
);

-- 插入預設對話，如果表格是空的
INSERT OR IGNORE INTO petDialogues (dialogue)
SELECT '早安，瑪嘎八咖'
UNION ALL
SELECT '累累想睡'
UNION ALL
SELECT '好想喝酒'
    WHERE NOT EXISTS (SELECT 1 FROM petDialogues);