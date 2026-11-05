package org.iesvegademijas.tienda_informatica.dao;

import java.util.List;
import java.util.Optional;

import org.iesvegademijas.tienda_informatica.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ComercialDAOImpl implements ComercialDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void create(Comercial comercial) {
		jdbcTemplate.update("INSERT INTO comercial (nombre, apellido1, apellido2, comision) VALUES (?, ?, ?, ?)",
				comercial.getNombre(), comercial.getApellido1(), comercial.getApellido2(), comercial.getComision());
	}

	@Override
	public List<Comercial> getAll() {
		return jdbcTemplate.query("SELECT * FROM comercial ORDER BY id", (rs, rowNum) -> new Comercial(
				rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido1"), rs.getString("apellido2"),
				rs.getDouble("comision")));
	}

	@Override
	public Optional<Comercial> find(int id) {
		List<Comercial> comerciales = jdbcTemplate.query("SELECT * FROM comercial WHERE id = ?",
				(rs, rowNum) -> new Comercial(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido1"),
						rs.getString("apellido2"), rs.getDouble("comision")), id);
		return comerciales.stream().findFirst();
	}

	@Override
	public void update(Comercial comercial) {
		jdbcTemplate.update("UPDATE comercial SET nombre = ?, apellido1 = ?, apellido2 = ?, comision = ? WHERE id = ?",
				comercial.getNombre(), comercial.getApellido1(), comercial.getApellido2(), comercial.getComision(),
				comercial.getId());
	}

	@Override
	public void delete(int id) {
		jdbcTemplate.update("DELETE FROM comercial WHERE id = ?", id);
	}
}