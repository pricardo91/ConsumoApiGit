package br.com.ricardo.exception;

public class ErroConsultaGitHubException extends RuntimeException {

	private String mensagem;

	public ErroConsultaGitHubException(String mensagem) {
		this.mensagem = mensagem;
	}

	@Override
	public String getMessage() {
		return this.mensagem;
	}
}
