package analysis;

import javafx.scene.paint.Color;
import java.util.Map;
import java.util.Set;

public class SubjectRecord {
    private static Map<String, Integer> subjectMap;
    private static Map<String, Color> colorMap;
    private static Map<String, String> nameMap;

    public SubjectRecord() {
        subjectMap = DBQuery.getSubjectData();
        colorMap = DBQuery.getColorData();
        nameMap = DBQuery.getNameData();
    }

    public void recordColor(String subject, Color color) {
        colorMap.put(subject, color);
        saveToDatabase();
    }

    public void recordPomodoro(String subject, int count) {
        subjectMap.put(subject, subjectMap.getOrDefault(subject, 0) + count);
        saveToDatabase();
    }

    public static void recordName(String subject, String name) {
        nameMap.put(subject, name);
        saveToDatabase();
    }

    public void removeSubject(String subject) {
        subjectMap.remove(subject);
        colorMap.remove(subject);
        nameMap.remove(subject);
        saveToDatabase();
    }

    private static void saveToDatabase() {
        DBQuery.saveData(subjectMap, colorMap, nameMap);
    }

    public Set<String> getSubjects() {
        return subjectMap.keySet();
    }

    public Color getColor(String subject) {
        return colorMap.get(subject);
    }

    public String getName(String subject) {
        return nameMap.get(subject);
    }
}
