package com.algaworks.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.Estilos;
import com.algaworks.brewer.repository.filter.EstiloFilter;
import com.algaworks.brewer.service.EstiloService;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;

@Controller
@RequestMapping(value = "/estilos")
public class EstiloController {
	
	@Autowired
	private EstiloService estiloService;
	
	@Autowired
	private Estilos estilos;

	@RequestMapping("/novo")
	public ModelAndView novo(Estilo estilo) {
		
		return new ModelAndView("estilo/CadastroEstilo");
	}
	
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
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
	
	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvar(@RequestBody @Valid Estilo estilo, BindingResult result) {
		
		if (result.hasErrors()) {
			
			return ResponseEntity.badRequest().body(result.getFieldError("nome").getDefaultMessage());
		}

		estilo = estiloService.salvar(estilo);
		
		return ResponseEntity.ok(estilo);
	}
	
	@GetMapping
	public ModelAndView pesquisar(EstiloFilter estiloFilter, BindingResult result,
			@PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest) {
		
		ModelAndView modelAndView = new ModelAndView("estilo/PesquisaEstilos");
		
		PageWrapper<Estilo> paginaWrapper = new PageWrapper<>(estilos.filtrar(estiloFilter, pageable), httpServletRequest);
		modelAndView.addObject("pagina", paginaWrapper);
		
		return modelAndView;
	}
}