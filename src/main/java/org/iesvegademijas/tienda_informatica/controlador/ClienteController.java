package org.iesvegademijas.tienda_informatica.controlador;

import org.iesvegademijas.tienda_informatica.modelo.Cliente;
import org.iesvegademijas.tienda_informatica.servicio.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping("/clientes")
	public String listar(Model model) {
		model.addAttribute("listaClientes", clienteService.listAll());
		return "clientes";
	}

	@GetMapping("/clientes/{id}")
	public String detalle(Model model, @PathVariable int id) {
		Cliente cliente = clienteService.one(id);
		if (cliente == null) {
			return "redirect:/clientes";
		}
		model.addAttribute("cliente", cliente);
		return "detalle-cliente";
	}

	@GetMapping("/clientes/crear")
	public String crear(Model model) {
		model.addAttribute("cliente", new Cliente());
		return "crear-cliente";
	}

	@PostMapping("/clientes/crear")
	public String submitCrear(@ModelAttribute("cliente") Cliente cliente) {
		clienteService.newCliente(cliente);
		return "redirect:/clientes";
	}

	@GetMapping("/clientes/editar/{id}")
	public String editar(Model model, @PathVariable int id) {
		Cliente cliente = clienteService.one(id);
		if (cliente == null) {
			return "redirect:/clientes";
		}
		model.addAttribute("cliente", cliente);
		return "editar-cliente";
	}

	@PostMapping("/clientes/editar/{id}")
	public String submitEditar(@PathVariable int id, @ModelAttribute("cliente") Cliente cliente) {
		cliente.setId(id);
		clienteService.replaceCliente(cliente);
		return "redirect:/clientes";
	}

	@PostMapping("/clientes/borrar/{id}")
	public String submitBorrar(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			clienteService.deleteCliente(id);
			redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado correctamente.");
		} catch (DataIntegrityViolationException exception) {
			redirectAttributes.addFlashAttribute("error",
					"No se puede eliminar el cliente porque tiene pedidos asociados.");
		}
		return "redirect:/clientes";
	}
}