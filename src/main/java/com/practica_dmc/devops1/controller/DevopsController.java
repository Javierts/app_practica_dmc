package com.practica_dmc.devops1.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica_dmc.devops1.service.DevopsService;

@RestController
public class DevopsController {

	private final DevopsService devopsService;
	private static final Path IMAGEN = Path.of("/mnt/traverse.png");

	public DevopsController(DevopsService devopsService) {
		this.devopsService = devopsService;
	}

	@GetMapping("/hello")
	public String getHello() {
		return "Bienvenido Javier Tapia";
	}

	@GetMapping("/variable")
	public Map<String, Object> getVariable() {
		return devopsService.getVariable();
	}

	@GetMapping("/secreto")
	public Map<String, Object> getSecreto() {
		return devopsService.getSecreto();
	}

	@GetMapping(value = "/imagen", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> imagen() {
        if (!Files.isRegularFile(IMAGEN)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new FileSystemResource(IMAGEN));
    }

	@GetMapping("/consulta")
	public Map<String, Object> getConsulta() {
		return devopsService.getConsulta();
	}
}
