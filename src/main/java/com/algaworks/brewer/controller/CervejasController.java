package com.algaworks.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.Origem;
import com.algaworks.brewer.model.Sabor;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.repository.Estilos;
import com.algaworks.brewer.repository.filter.CervejaFilter;
import com.algaworks.brewer.service.CervejaService;

@Controller
@RequestMapping("/cervejas")
public class CervejasController {
	
	@Autowired
	private Estilos estilos;
	
	@Autowired
	private CervejaService cervejaService;
	
	@Autowired
	private Cervejas cervejas;

	@RequestMapping(value = "/novo")
	public ModelAndView novo(Cerveja cerveja){
	
		ModelAndView modelAndView =  new ModelAndView("cerveja/CadastroCerveja");
		modelAndView.addObject("sabores", Sabor.values());
		modelAndView.addObject("estilos", this.estilos.findAll());
		modelAndView.addObject("origens", Origem.values());
		return modelAndView;
	}
	
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Cerveja cerveja, BindingResult result, Model model, RedirectAttributes attributes){
		
		if (result.hasErrors()){
		
			return this.novo(cerveja);
		}
		
		this.cervejaService.salvar(cerveja);
		attributes.addFlashAttribute("mensagem", "Cerveja salva com sucesso!");
		return new ModelAndView("redirect:/cervejas/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(CervejaFilter cervejaFilter, BindingResult result, 
			@PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest) {
		
		ModelAndView modelAndView = new ModelAndView("cerveja/PesquisaCervejas");
		modelAndView.addObject("estilos", this.estilos.findAll());
		modelAndView.addObject("sabores", Sabor.values());
		modelAndView.addObject("origens", Origem.values());
		
		PageWrapper<Cerveja> paginaWrapper = new PageWrapper<>(this.cervejas.filtar(cervejaFilter, pageable), httpServletRequest);
		
		modelAndView.addObject("pagina", paginaWrapper);
		return modelAndView;
	}
}
