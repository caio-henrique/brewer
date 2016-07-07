package com.algaworks.brewer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.service.EstiloService;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;

@Controller
public class EstiloController {
	
	@Autowired
	private EstiloService estiloService;

	@RequestMapping("/estilos/novo")
	public ModelAndView novo(Estilo estilo) {
		
		return new ModelAndView("estilo/CadastroEstilo");
	}
	
	@RequestMapping(value = "/estilos/novo", method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Estilo estilo, BindingResult result, RedirectAttributes attributes) {
		
		if (result.hasErrors()) {
			return novo(estilo);
		}
		
		try {
			
			this.estiloService.salvar(estilo);
			attributes.addFlashAttribute("mensagem", "Estilo salvo com sucesso");
		}
		
		catch(NomeEstiloJaCadastradoException exception) {
			
			result.rejectValue("nome", exception.getMessage(), exception.getMessage());
			return novo(estilo);
		}
		
		return new ModelAndView("redirect:/estilos/novo");
	}
	
	@RequestMapping(value = "/estilos", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvar(@RequestBody @Valid Estilo estilo, BindingResult result) {
		
		if (result.hasErrors()) {
			
			return ResponseEntity.badRequest().body(result.getFieldError("nome").getDefaultMessage());
		}
		
		try {
			
			estilo = estiloService.salvar(estilo);
		} 
		
		catch (NomeEstiloJaCadastradoException exception) {
			
			return ResponseEntity.badRequest().body(exception.getMessage());
		}
		
		return ResponseEntity.ok(estilo);
	}
}