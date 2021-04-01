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
//localhost:8080/cadastrar?nome=joao&doc=8989&tel=990
    
    @GetMapping("/cadastrar")
    public ResponseEntity<String> index(@RequestParam(required = false) String nome, @RequestParam(required = false) String doc, @RequestParam(required = false) String tel) throws Exception {
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
