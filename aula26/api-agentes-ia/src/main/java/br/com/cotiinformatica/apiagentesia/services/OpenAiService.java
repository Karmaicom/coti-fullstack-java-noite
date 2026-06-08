package br.com.cotiinformatica.apiagentesia.services;

import io.swagger.v3.oas.models.headers.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class OpenAiService {
    @Value("${openai.key}")
    private String key;

    @Value("${openai.endpoint}")
    private String endpoint;

    @Value("${openai.model}")
    private String model;

    //Metodo para fazermos uma requisição / chamada para a OpenAI
    public String send(String input) throws Exception {

        //Construindo a requisição para a API da OpenAI
        var restClient = RestClient.builder().baseUrl(endpoint)
                .defaultHeader("Authorization", "Bearer " + key)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        //Construindo os dados da chamada
        Map<String, Object> request = Map.of(
                "model", model,
                "input", input
        );

        //Executando a chamada e retornar a resposta
        var response = restClient
                .post()
                .uri("/responses")
                .body(request)
                .retrieve()
                .body(Map.class);

        //Capturando o texto da resposta
        var outputText = response.toString();
        String text = outputText.toString().trim();

        int startIndex = text.indexOf("text=");
        if (startIndex == -1) return text;

        startIndex += "text=".length();

        int endIndex = text.indexOf("}], role=", startIndex);

        if (endIndex == -1) {
            endIndex = text.indexOf("}],", startIndex);
        }

        if (endIndex == -1) {
            endIndex = text.indexOf("}", startIndex);
        }

        if (endIndex == -1) {
            endIndex = text.length();
        }

        String extractedText = text.substring (startIndex, endIndex).trim();
        return extractedText.isBlank() ? "Não foi possível gerar uma resposta." : extractedText;
        //return text;
    }

}
