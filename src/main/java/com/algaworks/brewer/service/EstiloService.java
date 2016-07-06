package com.algaworks.brewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.Estilos;

@Service
public class EstiloService {

	@Autowired
	private Estilos estilos;
	
	@Transactional
	public void salvar(Estilo estilo) {
		
		this.estilos.save(estilo);
	}
}
