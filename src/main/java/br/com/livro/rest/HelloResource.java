package br.com.livro.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResource {
	@GET
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.TEXT_HTML + ";charset=utf8") 
	public String helloHTML() {
		return "Olá mundo HTML";
	}
	@GET
	@Consumes({MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
	@Produces({MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN}) 
	public String helloXML() {
		return "Olá mundo XML";
	}
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public String helloJSON() {
		return "Olá mundo JSON";
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN) 
	public String helloTextPlain() {
		return "Olá mundo Texto";
	}
	
	@POST
	public String post() {
		return "HTTP POST";
	}
	
	@PUT
	public String put() {
		return "HTTP PUT";
	}
	
	@DELETE
	public String delete() {
		return "HTTP DELETE";
	}
}
