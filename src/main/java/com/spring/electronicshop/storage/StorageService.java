package com.spring.electronicshop.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.spring.electronicshop.exceptionhandler.StorageException;
import com.spring.electronicshop.exceptionhandler.StorageFileNotFoundException;

@Service
public class StorageService {

	private final Path rootLocation;

	public StorageService() {
		StorageProperties properties = new StorageProperties();

		if (properties.getLocation().trim().length() == 0) {
			throw new StorageException("File upload location can not be Empty.");
		}

		this.rootLocation = Paths.get(properties.getLocation());
	}

	public List<String> store(List<MultipartFile> files) {
		List<String> filePaths = new ArrayList<>();
		for (MultipartFile file : files) {
			filePaths.add(uploadFile(file));
		}

		return filePaths;
	}

	public String uploadFile(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			String originalFilename = file.getOriginalFilename();
			String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
			String fileName = String.format("%s%s", "image_", RandomStringUtils.randomAlphanumeric(8)) + fileType;
			Path destinationFile = this.rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new StorageException("Cannot store file outside current directory.");
			}

			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}

			return fileName;
		} catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
					.map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}
	public void deleteFile(String fileName) throws FileNotFoundException {
		File filedelete =  new File(rootLocation.toFile(),fileName);
		if (filedelete.exists()) {
	        if (!filedelete.delete()) {
	            throw new StorageException("Could not delete file: " + fileName);
	        }
	    } else {
	        throw new FileNotFoundException("File not found: " + fileName);
	    }
	}

	public void init() {
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
