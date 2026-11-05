package org.iesvegademijas.tienda_informatica.controlador;

import org.iesvegademijas.tienda_informatica.modelo.Comercial;
import org.iesvegademijas.tienda_informatica.servicio.ComercialService;
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
public class ComercialController {

	@Autowired
	private ComercialService comercialService;

	@GetMapping("/comerciales")
	public String listar(Model model) {
		model.addAttribute("listaComerciales", comercialService.listAll());
		return "comerciales";
	}

	@GetMapping("/comerciales/{id}")
	public String detalle(Model model, @PathVariable int id) {
		Comercial comercial = comercialService.one(id);
		if (comercial == null) {
			return "redirect:/comerciales";
		}
		model.addAttribute("comercial", comercial);
		return "detalle-comercial";
	}

	@GetMapping("/comerciales/crear")
	public String crear(Model model) {
		model.addAttribute("comercial", new Comercial());
		return "crear-comercial";
	}

	@PostMapping("/comerciales/crear")
	public String submitCrear(@ModelAttribute("comercial") Comercial comercial) {
		comercialService.newComercial(comercial);
		return "redirect:/comerciales";
	}

	@GetMapping("/comerciales/editar/{id}")
	public String editar(Model model, @PathVariable int id) {
		Comercial comercial = comercialService.one(id);
		if (comercial == null) {
			return "redirect:/comerciales";
		}
		model.addAttribute("comercial", comercial);
		return "editar-comercial";
	}

	@PostMapping("/comerciales/editar/{id}")
	public String submitEditar(@PathVariable int id, @ModelAttribute("comercial") Comercial comercial) {
		comercial.setId(id);
		comercialService.replaceComercial(comercial);
		return "redirect:/comerciales";
	}

	@PostMapping("/comerciales/borrar/{id}")
	public String submitBorrar(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			comercialService.deleteComercial(id);
			redirectAttributes.addFlashAttribute("mensaje", "Comercial eliminado correctamente.");
		} catch (DataIntegrityViolationException exception) {
			redirectAttributes.addFlashAttribute("error",
					"No se puede eliminar el comercial porque tiene pedidos asociados.");
		}
		return "redirect:/comerciales";
	}
}