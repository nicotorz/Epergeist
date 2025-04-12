package ar.edu.unq.epersgeist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Configuration
// Le indicamos a la configuracion a partir de que paquete tiene que "escanear" con esta anotacion de @ComponentScan
// Spring escanea entonces desde ese paquete para abajo por componentes (@Service, @Controller, @Component y DAOs) para levantar y agregar al contexto.
// Decirle que haga un escaneo desde el root es poco eficiente, pero nos sirve en un ejemplo chico

// Ahora, por que en la configuracion de tests hace falta agregar un component scan y en la configuracion del mainApp (EjemploSpringApp.kt) no?
// Por que el main app (EjemploSpringApp.kt) cuando lo corres fuerza un scan desde la carpeta donde esta corriendo para abajo.
// Y no casualmente la pusimos en el root del proyecto por esa misma razon.
// El test hace lo mismo, fuerza un scan del archivo de test (InventarioServiceControllerTest) para abajo,
// pero como no encuentra a los DAOs, service y controllers (No estan en las carpetas del test para abajo)
// hay que agregarle el @ComponentScan a la configuracion para que vaya a escanear por los objetos que nos faltan.

// Una vez ya se hicieron todos los escaneos, hace los autowirings y le pasa por ejemplo el controller al mockMVC que declaramos abajo
    @ComponentScan(basePackages = {"ar.edu.unq.epersgeist"})
public class ControllerTestConfiguration {

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        // Spring va a intentar hacer autowiring de toda dependencia que declaremos
        // como parametro en el metodo del Bean.
        public MockMvc mockMvc(EspirituControllerREST espirituControllerREST, UbicacionControllerREST ubicacionControllerREST, MediumControllerREST mediumControllerREST, HabilidadControllerREST habilidadControllerREST) { //contollers haya en parametros
            return MockMvcBuilders.standaloneSetup(espirituControllerREST, ubicacionControllerREST, mediumControllerREST,habilidadControllerREST).build();
        }
}

