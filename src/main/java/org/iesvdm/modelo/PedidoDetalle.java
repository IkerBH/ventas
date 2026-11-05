package org.iesvdm.modelo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDetalle {
	private int id;
	private double total;
	private LocalDate fecha;
	private int clienteId;
	private int comercialId;
	private String clienteNombre;
	private String comercialNombre;
}