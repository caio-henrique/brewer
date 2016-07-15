/**
 * 
 */
package com.algaworks.brewer.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Caio
 *
 */
public class FotoStorageRunnable implements Runnable {
	
	private MultipartFile[] files;
	private DeferredResult<String> resultado;

	public FotoStorageRunnable(MultipartFile[] files, DeferredResult<String> resultado){
		
		this.files = files;
		this.resultado = resultado;
	}
	
	@Override
	public void run() {
		
		this.resultado.setResult("Ok, foto recebida!");
	}

}
