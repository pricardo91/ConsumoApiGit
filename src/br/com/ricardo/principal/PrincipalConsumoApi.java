package br.com.ricardo.principal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.ricardo.exception.ErroConsultaGitHubException;
import br.com.ricardo.modelo.UsuarioGithubJson;

public class PrincipalConsumoApi {

	public static void main(String[] args) throws UnsupportedEncodingException {
		Scanner leitura = new Scanner(System.in);
		System.out.println("Quem deseja localizar no Github? ");
		String username = leitura.nextLine();

		String encodedUsername = URLEncoder.encode(username, "UTF-8");

		String endereco = "https://api.github.com/users/" + encodedUsername;
		System.out.println("Endereço http tratado com a classe URLEncoder passando o charset UTF-8: " + endereco);
		System.out.println();

		try {
			HttpClient cliente = HttpClient.newHttpClient();
			HttpRequest requisicao = HttpRequest.newBuilder().uri(URI.create(endereco)).build();

			HttpResponse<String> resposta = cliente.send(requisicao, HttpResponse.BodyHandlers.ofString());

			if (resposta.statusCode() == 404) {
				throw new ErroConsultaGitHubException("Usuário não encontrado no GitHub.");
			}

			String json = resposta.body();
			System.out.println("JOSN completo, sem tratamento: " + json);

			Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

			UsuarioGithubJson meuUsuarioGithub = gson.fromJson(json, UsuarioGithubJson.class);

			System.out.println("JSON convertido pelo gson: " + meuUsuarioGithub);
			System.out.println();
			
			System.out.println("**********************************");
			System.out.println("Nome: " + meuUsuarioGithub.name());
			System.out.println("URL: " + meuUsuarioGithub.html_url());
			System.out.println("Bio: " + meuUsuarioGithub.bio());
			System.out.println("**********************************");
			
		} catch (IOException | InterruptedException e) {
			System.out.println("Opss… Houve um erro durante a consulta à API do GitHub.");
			System.out.println(e.getMessage());

		} catch (IllegalArgumentException e) {

			System.out.println("Existem caracteres invalidos na pesquisa, por favor tente de novo");

		} catch (ErroConsultaGitHubException e) {
			System.out.println(e.getMessage());
		}

		leitura.close();
	}

}
