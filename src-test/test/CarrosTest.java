package test;

import java.util.List;

import org.junit.Assert;

import br.com.livro.domain.Carro;
import br.com.livro.domain.CarroService;
import junit.framework.TestCase;

public class CarrosTest extends TestCase {
	
	private CarroService carroService = new CarroService();

	public void testGetCarroById() {
		fail("Not yet implemented");
	}

	public void testFindByName() {
		fail("Not yet implemented");
	}

	public void testFindByTipo() {
		fail("Not yet implemented");
	}

	public void testGetCarros() {
		List<Carro> carros = carroService.getCarros();
		assertNotNull(carros);
		
		//Valida se encontrou algo
		assertTrue(carros.size()>0);
		
		Carro tucker = carroService.findByName("Tucker 1948").get(0);
		assertEquals("Tucker 1948", tucker.getNome());
		
		Carro ferrari = carroService.findByName("Ferrari FF").get(0);
		assertEquals("Ferrari FF", ferrari.getNome());
		
		Carro bugatti = carroService.findByName("Bugatti Veyron").get(0);
		assertEquals("Bugatti Veyron", bugatti.getNome());
	}

	public void testSave() {
		fail("Not yet implemented");
	}

	public void testDelete() {
		fail("Not yet implemented");
	}

}
