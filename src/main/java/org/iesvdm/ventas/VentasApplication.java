package org.iesvdm.ventas;

import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.modelo.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication(scanBasePackages = "org.iesvdm")
@RestController
@Slf4j
public class VentasApplication implements CommandLineRunner {

	@Autowired
	private ClienteDAO clienteDAO;


	public static void main(String[] args) {
		SpringApplication.run(VentasApplication.class, args);
	}

	@GetMapping("/")
	public String home() {
		// This is a simple endpoint that returns a welcome message when accessed via HTTP GET request.
		return "Welcome to the Ventas Application!";
	}

	@Override 
	public void run(String... args) throws Exception {
		log.info("*********************************");
		log.info("* Prueba de ClienteDAO *");
		log.info("*********************************");

		clienteDAO.getAll().forEach(cliente -> log.info("Cliente: {}", cliente));

		int id = 1;
		Optional<Cliente> cliente = clienteDAO.find(id);
		
		if (cliente.isPresent()) {
			log.info("Cliente con id {}: {}", id, cliente.get());

			String nombreOld = cliente.get().getNombre();
			cliente.get().setNombre("Jose M");
			clienteDAO.update(cliente.get());
			log.info("Cliente con id {} actualizado: {}", id, cliente.get());

			clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));

			cliente.get().setNombre(nombreOld);
			clienteDAO.update(cliente.get());
			log.info("Cliente con id {} restaurado: {}", id, cliente.get());

		} else {
			log.info("No se encontró cliente con id {}", id);
		}

	}
}
