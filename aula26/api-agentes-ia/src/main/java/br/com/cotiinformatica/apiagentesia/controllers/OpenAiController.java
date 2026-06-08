package br.com.cotiinformatica.apiagentesia.controllers;

import br.com.cotiinformatica.apiagentesia.dtos.OpenAiRequestDto;
import br.com.cotiinformatica.apiagentesia.services.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/openai")
public class OpenAiController {

    @Autowired
    private OpenAiService openAiService;

    @PostMapping
    public ResponseEntity<String> post(@RequestBody OpenAiRequestDto dto) {
        try {
            var input = "O usuário " + dto.nome() + " envia a seguinte pergunta: " + dto.pergunta();
            var response = openAiService.send(input);
            return ResponseEntity.status(200).body(response);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }
}
