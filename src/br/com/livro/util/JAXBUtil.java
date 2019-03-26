package br.com.livro.util;

import javax.xml.bind.JAXBContext;

import br.com.livro.domain.Carro;
import br.com.livro.servlets.ListaCarros;

public class JAXBUtil {
	private static JABUtil instance;
	private static JAXBContext context;

	public static JABUtil getInstance() {
		return instance;
	}

	static {
		try {
			context = JAXBContext.newInstance(ListaCarros.class, Carro.class);
		} catch (JAXBException e) {
			throw new RuntimeException();
		}
	}
}
