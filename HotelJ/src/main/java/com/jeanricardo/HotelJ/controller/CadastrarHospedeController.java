package com.jeanricardo.HotelJ.controller;

import com.jeanricardo.HotelJ.model.Hotel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class CadastrarHospedeController {

    /*
    localhost:8080/cadastrar
    
    {
        "nome":"Fulano da Silva",
        "documento":"123456",
        "telefone":"9925-2211"
    }
     */

    @PostMapping("/cadastrar")
    public ResponseEntity<String> index(@RequestBody HospedeDTO cadastro) throws Exception {
        String nome = cadastro.getnome();
        String doc = cadastro.getdocumento();
        String tel = cadastro.gettelefone();

        try {
            if (nome != null && doc != null && tel != null) {
                Hotel hotel = new Hotel();
                hotel.cadastrarHospede(nome, doc, tel);
                return new ResponseEntity<>("Sucesso", HttpStatus.OK);
            } else {
                
                return new ResponseEntity<>("Dados insulficientes", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
