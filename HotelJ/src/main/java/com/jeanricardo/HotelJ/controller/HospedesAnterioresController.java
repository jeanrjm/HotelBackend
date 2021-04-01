
package com.jeanricardo.HotelJ.controller;

import com.jeanricardo.HotelJ.model.Hospede;
import com.jeanricardo.HotelJ.model.Hotel;
import java.util.ArrayList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HospedesAnterioresController {
//localhost:8080/hospedes/naohospedados

    @GetMapping("hospedes/naohospedados")
    public ResponseEntity<String> index() throws Exception {
        String resp="";
        try {
            ArrayList<Hospede> hospedes = new Hotel().getHospedesAnteriores();
            if(hospedes==null  || hospedes.size()==0){
                return new ResponseEntity<>("Sem hospedes na categoria", HttpStatus.OK);
            }else{
              
                for(int i=0;i<hospedes.size(); i++){
                    resp+="{"
                    + "\"Nome\":\""+hospedes.get(i).getNome()+"\","
                    + "\"Documento\":\""+hospedes.get(i).getDocumento()+"\","
                    + "\"Telefone\":\""+hospedes.get(i).getTelefone()+"\","
                    + "\"ValorTotal\":\""+hospedes.get(i).getGasto()+"\"}";
                    if(i != hospedes.size()-1){
                        resp+=",";
                    }
                }
                
                HttpHeaders headers= new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>(resp, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        

    }

}
