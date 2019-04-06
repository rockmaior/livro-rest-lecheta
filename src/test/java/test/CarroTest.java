package test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.com.livro.domain.Carro;
import br.com.livro.domain.CarroService;
import junit.framework.TestCase;

public class CarroTest extends TestCase {

	private CarroService carroService;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		carroService = (CarroService) SpringUtil.getInstance().getBean(CarroService.class);
	}

	public void testListaCarros() {
		List<Carro> carros = carroService.getCarros();
		Assert.assertNotNull(carros);

		// Valida se encontrou algo
		assertTrue(carros.size() > 0);

		Carro tucker = carroService.findByName("Tucker 1948").get(0);
		Assert.assertEquals("Tucker 1948", tucker.getNome());

		Carro ferrari = carroService.findByName("Ferrari FF").get(0);
		Assert.assertEquals("Ferrari FF", ferrari.getNome());

		Carro bugatti = carroService.findByName("Bugatti Veyron").get(0);
		Assert.assertEquals("Bugatti Veyron", bugatti.getNome());

	}

	public void testSalvarDeletarCarro() {
		Carro c = new Carro();

		c.setDesc("Teste desc");
		c.setLatitude("lat");
		c.setLongitude("lng");
		c.setNome("Teste");
		c.setTipo("tipo");
		c.setUrlFoto("url foto aqui");
		c.setUrlVideo("url video aqui");

		carroService.save(c);

		// id do carro salvo
		Long id = c.getId();
		Assert.assertNotNull(id);

		// Busca no banco de dados para confirmar se o carro foi salvo

		c = carroService.getCarro(id);
		Assert.assertEquals("Teste", c.getNome());
		Assert.assertEquals("Teste desc", c.getDesc());
		Assert.assertEquals("lat", c.getLatitude());
		Assert.assertEquals("lng", c.getLongitude());
		Assert.assertEquals("tipo", c.getTipo());
		Assert.assertEquals("url foto aqui", c.getUrlFoto());
		Assert.assertEquals("url video aqui", c.getUrlVideo());

		// Atualiza o carro

		c.setNome("teste Update");
		carroService.save(c);

		// Busca carro novamente, vai estar atualizado

		c = carroService.getCarro(id);
		Assert.assertEquals("teste Update", c.getNome());

		// Deleta o carro
		carroService.delete(id);

		// Dessta vez, carro nao existe mais
		c = carroService.getCarro(id);
		Assert.assertNull(c);
	}

}
