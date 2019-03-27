package br.com.livro.util;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.livro.domain.Carro;
import br.com.livro.domain.CarroService;
import br.com.livro.servlets.ListaCarros;

public class TestClass {

	public static void main(String[] args) {
		CarroService carroService = new CarroService();
		List<Carro> carros = carroService.getCarros();

		ListaCarros lista = new ListaCarros();
		lista.setCarros(carros);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(lista);

		jsonToObject(gson, json);
	}
	
	public static void jsonToObject(Gson gson, String json) {
		List<Carro> testeLista =  (List<Carro>) gson.fromJson(json, ListaCarros.class);
		for (Carro carro : testeLista) {
			System.out.println(carro.getNome());
		}
	}

}
