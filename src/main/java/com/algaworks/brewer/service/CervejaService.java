package com.algaworks.brewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.Cervejas;

@Service
public class CervejaService {

	@Autowired
	private Cervejas cervejas;
	
	@Transactional
	public void salvar(Cerveja cerveja){
		
		this.cervejas.save(cerveja);
	}
}
