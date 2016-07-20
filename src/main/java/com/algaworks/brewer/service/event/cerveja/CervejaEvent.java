package com.algaworks.brewer.service.event.cerveja;

import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Cerveja;

public class CervejaEvent {

	private Cerveja cerveja;

	public CervejaEvent(Cerveja cerveja) {
	
		this.cerveja = cerveja;
	}

	public Cerveja getCerveja() {
		
		return cerveja;
	}
	
	public boolean isFoto(){
		
		return !StringUtils.isEmpty(this.cerveja.getFoto());
	}
}
