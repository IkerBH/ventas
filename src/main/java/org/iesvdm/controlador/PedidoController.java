package org.iesvdm.controlador;

import java.util.List;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.servicios.ClienteService;
import org.iesvdm.servicios.ComercialService;
import org.iesvdm.servicios.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PedidoController {

	private final PedidoService pedidoService;
	private final ClienteService clienteService;
	private final ComercialService comercialService;

	public PedidoController(PedidoService pedidoService, ClienteService clienteService,
			ComercialService comercialService) {
		this.pedidoService = pedidoService;
		this.clienteService = clienteService;
		this.comercialService = comercialService;
	}

	@GetMapping("/pedidos")
	public String listar(Model model) {
		model.addAttribute("pedidos", pedidoService.listAll());
		return "pedidos";
	}

	@GetMapping("/pedidos/{id}")
	public String detalle(Model model, @PathVariable int id) {
		return pedidoService.findDetalle(id)
				.map(pedido -> {
					model.addAttribute("pedido", pedido);
					return "detalle-pedido";
				})
				.orElse("redirect:/pedidos");
	}

	@GetMapping("/pedidos/crear")
	public String crear(Model model) {
		model.addAttribute("pedido", new Pedido());
		addRelaciones(model);
		return "crear-pedido";
	}

	@PostMapping("/pedidos/crear")
	public String submitCrear(@ModelAttribute("pedido") Pedido pedido, Model model) {
		if (!relacionesValidas(pedido)) {
			addRelaciones(model);
			model.addAttribute("errorRelaciones", "Selecciona un cliente y un comercial existentes.");
			return "crear-pedido";
		}
		pedidoService.create(pedido);
		return "redirect:/pedidos";
	}

	@GetMapping("/pedidos/editar/{id}")
	public String editar(Model model, @PathVariable int id) {
		return pedidoService.find(id)
				.map(pedido -> {
					model.addAttribute("pedido", pedido);
					addRelaciones(model);
					return "editar-pedido";
				})
				.orElse("redirect:/pedidos");
	}

	@PostMapping("/pedidos/editar/{id}")
	public String submitEditar(@PathVariable int id, @ModelAttribute("pedido") Pedido pedido, Model model) {
		pedido.setId(id);
		if (!relacionesValidas(pedido)) {
			addRelaciones(model);
			model.addAttribute("errorRelaciones", "Selecciona un cliente y un comercial existentes.");
			return "editar-pedido";
		}
		pedidoService.update(pedido);
		return "redirect:/pedidos";
	}

	@PostMapping("/pedidos/borrar/{id}")
	public String submitBorrar(@PathVariable int id) {
		pedidoService.delete(id);
		return "redirect:/pedidos";
	}

	private boolean relacionesValidas(Pedido pedido) {
		return pedido.getClienteId() > 0
				&& clienteService.find(pedido.getClienteId()).isPresent()
				&& pedido.getComercialId() > 0
				&& comercialService.find(pedido.getComercialId()).isPresent();
	}

	private void addRelaciones(Model model) {
		List<Cliente> clientes = clienteService.listAll();
		List<Comercial> comerciales = comercialService.listAll();
		model.addAttribute("clientes", clientes);
		model.addAttribute("comerciales", comerciales);
	}
}