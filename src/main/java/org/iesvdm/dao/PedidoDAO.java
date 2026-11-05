package org.iesvdm.dao;

import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoDAO {
	public void create(Pedido pedido);
	public List<Pedido> getAll();
	public Optional<Pedido> find(int id);
	public List<PedidoDetalle> getAllDetalle();
	public Optional<PedidoDetalle> findDetalle(int id);
	public void update(Pedido pedido);
	public void delete(int id);
}