package pet;

import java.io.*;

public class PetRecord {
    public static void main(String[] args) {
        try {
            // 開啟 subrecord.txt 檔案
            File inputFile = new File("src/main/resources/analysis/record/subrecord.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            // 建立 petrecord.txt 檔案
            File outputFile = new File("src/main/resources/pet/petrecord.txt");
            FileWriter writer = new FileWriter(outputFile, true); // 使用 true 表示附加到現有檔案

            // 讀取檔案內容
            String line;
            boolean foundColors = false;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("--colors--")) {
                    foundColors = true;
                    continue;
                }

                if (foundColors) {
                    // 處理顏色資料，假設格式為 <名稱>:<顏色碼>
                    String[] parts = line.split(":");
                    String name = parts[0].trim();
                    String color = parts[1].trim();

                    // 檢查 petrecord.txt 中是否已存在相同的名稱
                    boolean nameExists = checkNameExists(outputFile, name);

                    // 如果名稱已存在，則更新該行的顏色部分
                    if (nameExists) {
                        updateColor(outputFile, name, color);
                    } else {
                        // 否則寫入新的名稱和顏色
                        writer.write(name + " 小貓 " + color + "\n");
                    }
                }
            }

            // 關閉檔案
            reader.close();
            writer.close();

            System.out.println("petrecord.txt 已成功更新資料。");

        } catch (IOException e) {
            System.out.println("處理檔案時發生錯誤：" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 檢查名稱是否已存在 petrecord.txt 中
    private static boolean checkNameExists(File file, String name) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(name)) {
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }

    // 更新 petrecord.txt 中指定名稱的顏色
    private static void updateColor(File file, String name, String newColor) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(name)) {
                // 更新該行的顏色部分
                String[] parts = line.split(" ");
                parts[2] = newColor;
                line = String.join(" ", parts);
            }
            content.append(line).append("\n");
        }
        reader.close();

        // 覆寫檔案內容
        FileWriter writer = new FileWriter(file);
        writer.write(content.toString());
        writer.close();
    }
}
