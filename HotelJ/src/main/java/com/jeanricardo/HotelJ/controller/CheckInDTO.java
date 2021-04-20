/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeanricardo.HotelJ.controller;

/**
 *
 * @author Jean
 */
class CheckInDTO {

    HospedeDTO hospede;
    String dataEntrada;
    String dataSaida;
    String adicionalVeiculo;

    String getdataEntrada() {
        return dataEntrada;
    }

    String getdataSaida() {
        return dataSaida;
    }

    String getadicionalVeiculo() {
        return adicionalVeiculo;
    }
    
    HospedeDTO gethospede (){
        return hospede;
        
    }
    
    void setdataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    void setdataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }
    
    void setadicionalVeiculo(String adicionalVeiculo) {
        this.adicionalVeiculo = adicionalVeiculo;
    }
    
    void sethospede(HospedeDTO hospede) {
        this.hospede = hospede;
    }
    
    CheckInDTO(){
        
    }

}
