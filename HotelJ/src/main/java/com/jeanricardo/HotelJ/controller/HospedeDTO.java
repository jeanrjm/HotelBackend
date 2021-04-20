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
class HospedeDTO {

    private String nome;
    private String documento;
    private String telefone;

    void setnome(String nome) {
        this.nome = nome;
    }

    void setdocumento(String documento) {
        this.documento = documento;
    }

    void settelefone(String telefone) {
        this.telefone = telefone;
    }
    
    String getnome(){
        return nome;
    }
    
    String getdocumento(){
        return documento;
    }
            
    String gettelefone(){
        return telefone;
    }
    HospedeDTO() {

    }
}
