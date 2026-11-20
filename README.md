# devops1

API REST en Spring Boot para una práctica de DevOps. La aplicación se empaqueta como imagen Docker, se publica en Docker Hub y se despliega en Azure con Terraform sobre Azure Container Apps.

## Tecnologías

- Java 25
- Spring Boot 4.1.2 (Spring Web MVC)
- Maven (wrapper incluido: `mvnw`)
- Lombok
- Docker (build multi-etapa)
- Docker Hub como registro de imágenes
- Terraform y Azure Container Apps para el despliegue

## Endpoints

Todos son `GET` y devuelven un JSON con la estructura:

```json
{ "data": <Object>, "result": <int>, "mensaje": "<String>" }
```

`result` vale `1` si todo salió bien y `0` si falta el valor.

| Ruta        | Servicio      | Descripción                                   |
|-------------|---------------|-----------------------------------------------|
| `/variable` | `getVariable` | Devuelve la variable de entorno configurada   |
| `/secreto`  | `getSecreto`  | Devuelve el secreto configurado               |
| `/imagen`   | `getImagen`   | Devuelve la URL de la imagen configurada      |
| `/consulta` | `getConsulta` | Consulta (pendiente de implementar)           |
| `/hello`    | -             | Mensaje de bienvenida                         |

## Estructura

```
src/main/java/com/practica_dmc/devops1/
├── controller/   DevopsController
└── service/      DevopsService (interfaz) y DevopsServiceImpl
```

## Variables de entorno

La aplicación no lleva valores fijos en el código. Los lee de variables de entorno que se definen en Azure, en la configuración del Container App (con Terraform):

| Variable          | Uso                              |
|-------------------|----------------------------------|
| `ENVIRONMENT_VAR` | Valor del endpoint `/variable`   |
| `passwordbd`      | Valor del endpoint `/secreto`    |
| `app.imagen`      | URL del endpoint `/imagen`       |

Si una variable no existe, el servicio responde con `result: 0` y un mensaje indicándolo. Para probar en local, defínelas antes de arrancar la aplicación.

## Ejecución local

```bash
./mvnw spring-boot:run
```

La aplicación queda en `http://localhost:8080`.

## Imagen Docker y despliegue

1. **Construir la imagen.** El `Dockerfile` compila el proyecto con Maven y genera una imagen ligera con solo el JRE, ejecutándose con un usuario no root en el puerto 8080:

   ```bash
   docker build -t <usuario>/mi-app-java:<version> .
   ```

2. **Publicarla en Docker Hub:**

   ```bash
   docker login
   docker push <usuario>/mi-app-java:<version>
   ```

3. **Desplegar en Azure.** Terraform crea el Container App, que descarga la imagen desde Docker Hub (`docker.io/<usuario>/mi-app-java:<version>`) y le inyecta las variables de entorno. Cada vez que publiques una versión nueva, actualiza la etiqueta de la imagen en la configuración de Terraform y ejecuta `terraform apply`.

La infraestructura de Terraform vive en un repositorio o carpeta aparte del código de la aplicación.
