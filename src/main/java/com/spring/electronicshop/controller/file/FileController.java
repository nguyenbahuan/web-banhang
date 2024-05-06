package com.spring.electronicshop.controller.file;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.electronicshop.storage.StorageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class FileController {

	private final StorageService storageService;

	@PostMapping("/files")
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") List<MultipartFile> files) {

		List<String> filePath = storageService.store(files);

		return ResponseEntity.ok(filePath);
	}

	@GetMapping("/files/{filename}")
	public ResponseEntity<byte[]> serveFile(@PathVariable String filename) throws IOException {

		Resource file = storageService.loadAsResource(filename);

		if (file == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png"))
				.body(file.getContentAsByteArray());
	}
}
