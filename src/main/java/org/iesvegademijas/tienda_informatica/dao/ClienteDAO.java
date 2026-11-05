package org.iesvegademijas.tienda_informatica.dao;

import java.util.List;
import java.util.Optional;

import org.iesvegademijas.tienda_informatica.modelo.Cliente;

public interface ClienteDAO {

	void create(Cliente cliente);
	List<Cliente> getAll();
	Optional<Cliente> find(int id);
	void update(Cliente cliente);
	void delete(int id);
}