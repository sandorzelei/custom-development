package com.intland.codebeamer.event.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileCipherManagerTest {

	private FileCipherManager fileCipherManager;

	private File testFile;

	private File encryptedTestFile;

	@BeforeEach
	void setUp() throws Exception {
		this.fileCipherManager = new FileCipherManager();
		this.testFile = createTempFile(getResourceUri("testFile.txt"));
		this.encryptedTestFile = createTempFile(getResourceUri("testFileEncrypted.txt"));
	}

	@Test
	void testPostProcessExportedFile() throws Exception {
		// Given
		// see setup method

		// When
		fileCipherManager.postProcessExportedFile(this.testFile.getAbsolutePath());

		// Then
		assertArrayEquals(FileUtils.readFileToByteArray(encryptedTestFile), FileUtils.readFileToByteArray(testFile));
	}

	@Test
	void testPreProcessImportedFile() throws Exception {
		// Given
		// see setup method

		// When
		fileCipherManager.preProcessImportedFile(encryptedTestFile.getAbsolutePath());

		// Then
		assertArrayEquals(FileUtils.readFileToByteArray(encryptedTestFile), FileUtils.readFileToByteArray(testFile));
	}

	private URI getResourceUri(String name) throws URISyntaxException {
		return this.getClass().getResource(name).toURI();
	}
	
	private File createTempFile(URI fileResourceUri) throws IOException, URISyntaxException, FileNotFoundException {
		File tempFile = File.createTempFile("fileEncryptionManagerTest", ".tmp");
		Files.copy(Path.of(fileResourceUri), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		return tempFile;
	}

}