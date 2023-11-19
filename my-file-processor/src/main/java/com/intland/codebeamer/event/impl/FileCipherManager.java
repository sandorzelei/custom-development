package com.intland.codebeamer.event.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.intland.codebeamer.processing.FileExportListener;
import com.intland.codebeamer.processing.FileImportListener;
import com.intland.codebeamer.processing.exception.FileProcessException;

@Component("fileEncryptionManager")
public class FileCipherManager implements FileImportListener, FileExportListener {

	private static final Logger logger = LogManager.getLogger(FileCipherManager.class);

	private String password;

	public FileCipherManager() {
		this("ThisIsASecretKey");
	}

	public FileCipherManager(String password) {
		Assert.isTrue(StringUtils.length(password) == 16, "Password must be exectly 16 characters");
		this.password = password;
	}

	@Override
	public void postProcessExportedFile(String path) throws FileProcessException {
		doCipher(Paths.get(path), Cipher.ENCRYPT_MODE);
	}

	@Override
	public void preProcessImportedFile(String path) throws FileProcessException {
		doCipher(Paths.get(path), Cipher.DECRYPT_MODE);
	}
	
	private void doCipher(Path fielPath, int encryptMode) throws FileProcessException {
		try {
			
			File tmpFile = createTempFile();
			logger.debug("Temp file is created {}", tmpFile.getPath());
			
			try (FileOutputStream outputStream = new FileOutputStream(tmpFile);
				CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, getCipher(encryptMode))) {
				logger.debug("Start copying....");
				Files.copy(fielPath, cipherOutputStream);
			} catch (Exception e) {
				logger.error("File cannot be processed", e);
				FileUtils.delete(tmpFile);
				logger.debug("Temp file was removed");
				throw e;
			}

			logger.debug("Replacing original file with the temporaly file");
			Files.move(tmpFile.toPath(), fielPath, StandardCopyOption.REPLACE_EXISTING);

		} catch (Exception e) {
			logger.error("Post processing cannot be executed", e);
			throw new FileProcessException(e.getMessage(), e);
		}
	}

	private Cipher getCipher(int encryptMode) throws Exception {
		SecretKeySpec secretKeySpec = new SecretKeySpec(this.password.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(encryptMode, secretKeySpec);
		return cipher;
	}
	
	private File createTempFile() throws FileProcessException {
		try {
			return File.createTempFile("fileEncryptionManager", ".tmp");
		} catch (IOException e) {
			throw new FileProcessException("Temp file cannot be created", e);
		}
	}

}