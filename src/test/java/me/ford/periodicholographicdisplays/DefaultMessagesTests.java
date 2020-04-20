package me.ford.periodicholographicdisplays;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultMessagesTests extends MessagesTests {
    private static final String MESSAGES_FILE_NAME = "messages.yml";
    private final File sourceFolder = new File("src");
    private final File mainFolder = new File(sourceFolder, "main");
    private final File mainResourceFolder = new File(mainFolder, "resources");
    private File defaultMessages;
    private final File testFolder = new File(sourceFolder, "test");
    private final File testResourceFolder = new File(testFolder, "resources");
    private File testMessages;

    @Override
    @Before
    public void setUp() {
        defaultMessages = new File(mainResourceFolder, MESSAGES_FILE_NAME);
        if (!defaultMessages.exists()) {
            throw new IllegalStateException("Expected the default messages.yml ot be in " + defaultMessages);
        }
        testMessages = new File(testResourceFolder, MESSAGES_FILE_NAME);
        if (testMessages.exists()) {
            testMessages.delete();
        }
        try {
            Files.copy(defaultMessages.toPath(), testMessages.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.setUp();
    }

    @Override
    @After
    public void tearDown() {
        super.tearDown();
        testMessages.delete();
    }

    @Test
    public void testMessages_exists() {
        Assert.assertTrue(testMessages.exists());
    }

}