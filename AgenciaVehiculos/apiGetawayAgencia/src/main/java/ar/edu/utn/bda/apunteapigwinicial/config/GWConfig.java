package ar.edu.utn.bda.apunteapigwinicial.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GWConfig {

    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder,
                                        @Value("${url-microservicio-prueba}") String uriPrueba,
                                        @Value("${url-microservicio-notificacion}") String uriNotificacion,
                                        @Value("${url-microservicio-servicio}") String uriServicio) {
        return builder.routes()
                .route(p -> p.path("/api/agencia/**").uri(uriPrueba))
                .route(p -> p.path("/api/notificaciones/**").uri(uriNotificacion))
                .route(p -> p.path("/api/servicio/**").uri(uriServicio))
                .build();

    }

}
