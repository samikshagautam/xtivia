package test.java.com.xtivia.assessment;

import main.java.com.xtivia.assessment.FileParser;
import org.junit.Before;
import org.junit.Test;


class FileParserTest {
    private FileParser parser;

    @Before()
    public void initialize() {
        parser = new FileParser();
    }

    @Test
    public void fileEmptyPathTest() {
        String filePath = "";
        boolean parse = parser.parseFile(filePath);
        assert !parse;
    }

    @Test
    public void fileNotFoundTest() {
        String filePath = System.getProperty("user.dir") + "/abc.txt";
        boolean parse = parser.parseFile(filePath);
        assert !parse;
    }

    @Test
    public void fileInvalidTest() {
        String filePath = System.getProperty("user.dir") + "/invalid";
        boolean parse = parser.parseFile(filePath);
        assert !parse;
    }

    @Test
    public void fileIsDirectoryTest() {
        String filePath = System.getProperty("user.dir") + "/test";
        boolean parse = parser.parseFile(filePath);
        assert !parse;
    }

    @Test
    public void wordsTest() {
        String filePath = System.getProperty("user.dir") + "/text.txt";
        boolean parse = parser.parseFile(filePath);
        assert parse;
        assert (parser.getWords().size() == 1);
    }
}