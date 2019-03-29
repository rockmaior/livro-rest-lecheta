package br.com.livro.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.jettison.JettisonFeature;

public class MyApplication extends Application {
	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<Object>();

		// Driver do Jettison para gerar JSON
		singletons.add(new JettisonFeature());
		return singletons;
	}

	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		// configura o pacte para fazer scan das classe com anotações rest
		properties.put("jersy.config.server.provider.packages", "br.com.livro");
		return properties;
	}
}
