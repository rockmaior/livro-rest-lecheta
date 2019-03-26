package br.com.livro.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.livro.domain.Carro;
import br.com.livro.domain.CarroService;
@WebServlet("/carros")
public class CarrosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public CarroService carroService = new CarroService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Carro> carros = carroService.getCarros();
		String carrosString = carros.toString();
		response.getWriter().write(carrosString);
	}

}
