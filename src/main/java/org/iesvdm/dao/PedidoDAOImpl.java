package org.iesvdm.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PedidoDAOImpl implements PedidoDAO {

	private static final String SQL_PEDIDOS_DETALLE = """
			SELECT p.id, p.total, p.fecha, p.id_cliente, p.id_comercial,
				CONCAT_WS(' ', c.nombre, c.apellido1, c.apellido2) AS cliente_nombre,
				CONCAT_WS(' ', co.nombre, co.apellido1, co.apellido2) AS comercial_nombre
			FROM pedido p
			JOIN cliente c ON p.id_cliente = c.id
			JOIN comercial co ON p.id_comercial = co.id
			""";

	private final JdbcTemplate jdbcTemplate;

	PedidoDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public synchronized void create(Pedido pedido) {
		String sqlInsert = """
				INSERT INTO pedido (total, fecha, id_cliente, id_comercial)
				VALUES (?, ?, ?, ?)
				""";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			ps.setDouble(1, pedido.getTotal());
			LocalDate fecha = pedido.getFecha();
			ps.setDate(2, fecha == null ? null : Date.valueOf(fecha));
			ps.setInt(3, pedido.getClienteId());
			ps.setInt(4, pedido.getComercialId());
			return ps;
		}, keyHolder);

		if (keyHolder.getKey() != null) {
			pedido.setId(keyHolder.getKey().intValue());
		}
		log.info("Insertados {} registros.", rows);
	}

	@Override
	public List<Pedido> getAll() {
		List<Pedido> pedidos = jdbcTemplate.query("SELECT * FROM pedido", this::mapRow);
		log.info("Devueltos {} registros.", pedidos.size());
		return pedidos;
	}

	@Override
	public Optional<Pedido> find(int id) {
		List<Pedido> pedidos = jdbcTemplate.query(
				"SELECT * FROM pedido WHERE id = ?",
				this::mapRow,
				id);

		if (pedidos.isEmpty()) {
			log.info("Pedido con id {} no encontrado.", id);
		}
		return pedidos.stream().findFirst();
	}

	@Override
	public List<PedidoDetalle> getAllDetalle() {
		List<PedidoDetalle> pedidos = jdbcTemplate.query(SQL_PEDIDOS_DETALLE, this::mapDetalleRow);
		log.info("Devueltos {} pedidos con datos relacionados.", pedidos.size());
		return pedidos;
	}

	@Override
	public Optional<PedidoDetalle> findDetalle(int id) {
		List<PedidoDetalle> pedidos = jdbcTemplate.query(
				SQL_PEDIDOS_DETALLE + " WHERE p.id = ?",
				this::mapDetalleRow,
				id);
		return pedidos.stream().findFirst();
	}

	@Override
	public void update(Pedido pedido) {
		int rows = jdbcTemplate.update("""
				UPDATE pedido SET
					total = ?,
					fecha = ?,
					id_cliente = ?,
					id_comercial = ?
				WHERE id = ?
				""",
				pedido.getTotal(),
				pedido.getFecha() == null ? null : Date.valueOf(pedido.getFecha()),
				pedido.getClienteId(),
				pedido.getComercialId(),
				pedido.getId());

		log.info("Update de Pedido con {} registros actualizados.", rows);
	}

	@Override
	public void delete(int id) {
		int rows = jdbcTemplate.update("DELETE FROM pedido WHERE id = ?", id);
		log.info("Delete de Pedido con {} registros eliminados.", rows);
	}

	private Pedido mapRow(ResultSet rs, int rowNum) throws SQLException {
		Date fecha = rs.getDate("fecha");
		return new Pedido(
				rs.getInt("id"),
				rs.getDouble("total"),
				fecha == null ? null : fecha.toLocalDate(),
				rs.getInt("id_cliente"),
				rs.getInt("id_comercial"));
	}

	private PedidoDetalle mapDetalleRow(ResultSet rs, int rowNum) throws SQLException {
		Date fecha = rs.getDate("fecha");
		return new PedidoDetalle(
				rs.getInt("id"),
				rs.getDouble("total"),
				fecha == null ? null : fecha.toLocalDate(),
				rs.getInt("id_cliente"),
				rs.getInt("id_comercial"),
				rs.getString("cliente_nombre"),
				rs.getString("comercial_nombre"));
	}
}