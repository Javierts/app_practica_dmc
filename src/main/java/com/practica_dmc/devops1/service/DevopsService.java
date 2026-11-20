package com.practica_dmc.devops1.service;

import java.util.Map;

public interface DevopsService {

	Map<String, Object> getVariable();

	Map<String, Object> getSecreto();

	Map<String, Object> getImagen();

	Map<String, Object> getConsulta();
}
