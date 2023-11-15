package main.java.com.xtivia.assessment;

import io.github.pixee.security.BoundedLineReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class FileParser {
    private List<String> words;
    private List<String> phrases;
    private String error;

    private final Set<String> allowedExtensions;

    public FileParser() {
        allowedExtensions = new HashSet<>();
        allowedExtensions.addAll(Arrays.asList("txt", "rtf"));
    }

    public boolean parseFile(String filePath) {
        words = new ArrayList<>();
        phrases = new ArrayList<>();
        error = null;
        try {
            validateFile(filePath);

            List<String> fileContents = getFileContents(filePath);

            fileContents.forEach((line) -> {
                if (line.indexOf(' ') > -1) {
                    phrases.add(line);
                } else {
                    words.add(line);
                }
            });

            sort(phrases);
        } catch (Exception e) {
            error = e.getMessage();
            return false;
        }

        return true;
    }

    private void sort(List<String> list) {
        list.sort((a, b) -> {
            if (a.length() == b.length()) {
                return a.compareTo(b);
            }

            return a.length() - b.length();
        });
    }

    private List<String> getFileContents(String filePath) throws Exception {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line = BoundedLineReader.readLine(reader, 5_000_000);
        while (line != null) {
            // remove any leading and trailing whitespaces
            line = line.trim();
            lines.add(line);
            // read next line
            line = BoundedLineReader.readLine(reader, 5_000_000);
        }
        reader.close();
        return lines;
    }

    private void validateFile(String filePath) throws Exception {
        if (filePath.isEmpty()) {
            throw new Exception("File path is empty");
        }

        String ext = filePath.substring(filePath.lastIndexOf('.') + 1);
        if (ext.isEmpty()) {
            throw new Exception("Unknown file extension");
        }

        if (!allowedExtensions.contains(ext.toLowerCase(Locale.ROOT))) {
            throw new Exception("Unsupported file extension");
        }

        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception(filePath + " does not exist");
        }

        if (file.isDirectory()) {
            throw new Exception(filePath + " is a directory");
        }
    }

    public List<String> getWords() {
        return words;
    }

    public List<String> getPhrases() {
        return phrases;
    }

    public String getError() {
        return error;
    }

    public List<String> getUniqueWords() {
        Set<String> uniqueWords = new HashSet<>(words);
        return new ArrayList<>(uniqueWords);
    }

    public List<String> getUniquePhrases() {
        Set<String> uniquePhrases = new HashSet<>(phrases);
        return new ArrayList<>(uniquePhrases);
    }

    public static void main(String[] args) {
        FileParser parser = new FileParser();
        String filePath = System.getProperty("user.dir") + "/test.txt";
        if (parser.parseFile(filePath)) {
            List<String> words = parser.getWords();
            List<String> phrases = parser.getPhrases();

            System.out.println("Words in file = " + words);
            System.out.println("Phrases in file = " + phrases);
        }
    }
}
