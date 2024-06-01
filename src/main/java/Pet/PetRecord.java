package Pet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PetRecord {

    public void writeSubjectsToPetRecord(String subRecordFilePath, String petRecordFilePath) { //將subrecord裡面存的subject寫入petrecord
        try (BufferedReader br = new BufferedReader(new FileReader(subRecordFilePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(petRecordFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length > 0) {
                    bw.write(parts[0].trim());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String subRecordFilePath = "src/main/resources/analysis/record/subrecord.txt";
        String petRecordFilePath = "src/main/resources/pet/petrecord.txt";

        PetRecord petRecord = new PetRecord();
        petRecord.writeSubjectsToPetRecord(subRecordFilePath, petRecordFilePath); //進行寫入
    }
}
