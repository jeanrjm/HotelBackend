package com.jeanricardo.HotelJ.controller;

import com.jeanricardo.HotelJ.model.Hospede;
import com.jeanricardo.HotelJ.model.Hotel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class CheckInController {
//localhost:8080/checkin?nome=joao&doc=8989&tel=990&entrada=2021-03-28T10:23:54&saida=2021-03-31T18:23:54&veiculo=true

    @GetMapping("/checkin")
    public ResponseEntity<String> index(@RequestParam(required = false) 
            String nome, @RequestParam(required = false) String doc, 
            @RequestParam(required = false) String tel,
            @RequestParam(required = false) String entrada, 
            @RequestParam(required = false) String saida,
            @RequestParam(required = false) String veiculo) throws Exception {

        try {
            if ((nome != null || doc != null || tel != null) && veiculo != null && entrada != null && saida!= null) {
                boolean adicVeiculo = Boolean.parseBoolean(veiculo);
                int id = new Hotel().getIdHospede(nome, doc, tel);
                Hospede h = new Hospede(id);
                h.checkIn(adicVeiculo, entrada, saida);
                return new ResponseEntity<>("Sucesso", HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Dados insulficientes", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
