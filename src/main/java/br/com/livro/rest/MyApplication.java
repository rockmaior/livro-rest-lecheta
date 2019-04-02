package br.com.livro.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Application;

public class MyApplication extends Application {

	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		// configura o pacte para fazer scan das classe com anotações rest
		properties.put("jersy.config.server.provider.packages", "br.com.livro");
		return properties;
	}
}
