package br.com.ricardo.modelo;

public record UsuarioGithubJson(
	    String login,
	    String name,
	    String html_url,
	    String bio
	) {}