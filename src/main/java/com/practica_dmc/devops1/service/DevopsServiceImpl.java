package com.practica_dmc.devops1.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Los valores de variable, secreto e imagen se leen de la configuracion de la app.
 * En Azure App Service se inyectan como App Settings (definidos con Terraform):
 *  - APP_VARIABLE : valor plano
 *  - APP_SECRETO  : referencia a Key Vault (@Microsoft.KeyVault(SecretUri=...))
 *  - APP_IMAGEN   : URL del blob en Storage Account
 */
@Service
public class DevopsServiceImpl implements DevopsService {

	//@Value("${app.variable:}")
	//private String variable;

	//@Value("${app.secreto:}")
	//private String secreto;

	@Value("${app.imagen:}")
	private String imagen;
	
	
	//@Value("${ENVIRONMENT_VAR:}")
	//private String environmentVar;

	@Override
	public Map<String, Object> getVariable() {
		String variable = System.getenv("ENVIRONMENT_VAR");
		return respuesta(variable, "variable de entorno ENVIRONMENT_VAR");
	}

	@Override
	public Map<String, Object> getSecreto() {
		String passwordbd = System.getenv("passwordbd");
		return respuesta(passwordbd, "secreto");
	}

	@Override
	public Map<String, Object> getImagen() {
		return respuesta(imagen, "imagen");
	}

	@Override
	public Map<String, Object> getConsulta() {
		// Pendiente: definir el origen de la consulta (p. ej. Azure SQL / PostgreSQL)
		Map<String, Object> map = new HashMap<>();
		map.put("data", null);
		map.put("result", 1);
		map.put("mensaje", "Servicio de consulta pendiente de implementar");
		return map;
	}

	private Map<String, Object> respuesta(String valor, String nombre) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.hasText(valor)) {
			map.put("data", valor);
			map.put("result", 1);
			map.put("mensaje", "OK");
		} else {
			map.put("data", null);
			map.put("result", 0);
			map.put("mensaje", "No se encontro configurado el valor de " + nombre);
		}
		return map;
	}
}
