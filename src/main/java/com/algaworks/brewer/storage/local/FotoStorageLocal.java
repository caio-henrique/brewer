package com.algaworks.brewer.storage.local;

import static java.nio.file.FileSystems.getDefault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.storage.FotoStorage;

public class FotoStorageLocal implements FotoStorage {

	private static final Logger LOGGER = LoggerFactory.getLogger(FotoStorageLocal.class);
	
	private Path local;
	private Path localTemporario;
	
	public FotoStorageLocal(){
		
		this(getDefault().getPath(System.getenv("HOME"), ".brewerfotos"));
	}
	
	public FotoStorageLocal(Path path){
		
		this.local = path;
		this.criarPasta();
	}

	@Override
	public String salvarTemporariamente(MultipartFile[] files) {
		
		String novoNome = null;
		
		if(files != null && files.length > 0){
			
			MultipartFile arquivo = files[0];
			novoNome = this.renomearArquivo(arquivo.getOriginalFilename());
			
			try {
				
				arquivo.transferTo(new File(this.localTemporario.toAbsolutePath().toString() + getDefault().getSeparator() + novoNome));
			} 
	
			catch (IOException exception) {
				
				throw new RuntimeException("Erro salvando imagem na pasta temporária");
			}
		}
		
		return novoNome;
	}
	
	@Override
	public byte[] recuperarFotoTemporaria(String nome) {
		
		try {
			
			return Files.readAllBytes(this.localTemporario.resolve(nome));
		} 
		
		catch (IOException exception) {
			
			throw new RuntimeException("Erro ao ler foto temporaria", exception);
		}
	}
	
	private void criarPasta(){
		
		try {
			
			Files.createDirectories(this.local);
			this.localTemporario = getDefault().getPath(this.local.toString(), "temp");
			Files.createDirectories(this.localTemporario);
			
			//if(LOGGER.isDebugEnabled()) {
				
				LOGGER.debug("Pasta para salvar fotos criada com sucesso");
				LOGGER.debug("Pasta default: " + this.local.toAbsolutePath());
				LOGGER.debug("Pasta temporária: " + this.localTemporario.toAbsolutePath());
			//}
		} 
		
		catch (IOException exception) {
			
			throw new RuntimeException("Erro ao criar pasta para salvar imagem.", exception);
		}
	}
	
	private String renomearArquivo(String nomeOriginal){
		
		String novoNome = UUID.randomUUID().toString() + "_" + nomeOriginal;
		
		if(LOGGER.isDebugEnabled()) {
		
			LOGGER.debug(String.format("Nome original: %s", nomeOriginal));
			LOGGER.debug(String.format("Novo nome do arquivo: %s", novoNome));
		}
		
		return novoNome;
	}
}
