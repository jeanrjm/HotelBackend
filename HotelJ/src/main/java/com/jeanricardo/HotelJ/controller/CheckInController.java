package com.jeanricardo.HotelJ.controller;

import com.jeanricardo.HotelJ.model.Hospede;
import com.jeanricardo.HotelJ.model.Hotel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class CheckInController {

    /*
    localhost:8080/checkin
    
    {
        "hospede": {
            "nome":"Fulano da Silva",
            "documento":"123456",
            "telefone":"9925-2211"
        },
        "dataEntrada": "2018-03-14T08:00:00",
        "dataSaida": "2018-03-16T10:17:00",
        "adicionalVeiculo": "false"
    }
    */
    @PostMapping("/checkin")
    public ResponseEntity<String> index(@RequestBody CheckInDTO checkin) {
        String nome = checkin.gethospede().getnome();
        String doc = checkin.gethospede().getdocumento();;
        String tel = checkin.gethospede().gettelefone();;
        String entrada = checkin.getdataEntrada();
        String saida = checkin.getdataSaida();
        String veiculo = checkin.getadicionalVeiculo();

        try {
            if ((nome != null || doc != null || tel != null) && veiculo != null && entrada != null && saida != null) {
                boolean adicVeiculo = Boolean.parseBoolean(veiculo);
                int id = new Hotel().getIdHospede(nome, doc, tel);
                Hospede h = new Hospede(id);
                h.checkIn(adicVeiculo, entrada, saida);

                return new ResponseEntity<>("Sucesso", HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Dados insulficientes", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() + "aqui", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
