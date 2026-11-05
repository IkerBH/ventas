package org.iesvegademijas.tienda_informatica.dao;

import java.util.List;
import java.util.Optional;

import org.iesvegademijas.tienda_informatica.modelo.Comercial;

public interface ComercialDAO {

	void create(Comercial comercial);
	List<Comercial> getAll();
	Optional<Comercial> find(int id);
	void update(Comercial comercial);
	void delete(int id);
}