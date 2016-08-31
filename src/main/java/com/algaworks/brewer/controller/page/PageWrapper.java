package com.algaworks.brewer.controller.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.util.UriComponentsBuilder;

public class PageWrapper<T> {

	private Page<T> page;
	private UriComponentsBuilder uriBuilder;

	public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
		
		this.page = page;
		
		String httpUrl = httpServletRequest.getRequestURL().append(
				httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "")
				.toString().replaceAll("\\+", "%20");
		
		this.uriBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl);
	}
	
	public List<T> getConteudo(){
		
		return this.page.getContent();
	}
	
	public boolean isEmpty(){
		
		return this.page.getContent().isEmpty();
	}
	
	public int getPaginaAtual(){
		
		return this.page.getNumber();
	}
	
	public boolean isFirst(){
		
		return this.page.isFirst();
	}
	
	public boolean isLast(){
		
		return this.page.isLast();
	}
	
	public int getTotalPaginas(){
		
		return this.page.getTotalPages();
	}
	
	public String urlParaPagina(int pagina){
		
		return this.uriBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();
	}
	
	public String urlOrdenada(String propriedade){
		
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
				.fromUriString(this.uriBuilder.build(true).encode().toUriString());
		
		String valorSort = String.format("%s,%s", propriedade, this.inverterDirecao(propriedade));
		
		return uriComponentsBuilder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();
	}
	
	public String inverterDirecao(String propriedade){
		
		String direcao = "asc";
		
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
		
		if(order != null)
			direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
		
		
		return direcao;
	}
	
	public boolean descendente(String propriedade){
		
		return this.inverterDirecao(propriedade).equals("asc");
	}
	
	public boolean ordenada(String propriedade) {
		
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null; 
		
		if (order == null) {
			
			return false;
		}
		
		return page.getSort().getOrderFor(propriedade) != null ? true : false;
	}
}
