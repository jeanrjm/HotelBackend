/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeanricardo.HotelJ.model;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author Jean
 */
public class Hotel {

    private Connection c;
    private PreparedStatement stmt;
    private String sql;

    public void cadastrarHospede(String nomeHospede, String documento, String telefone) throws Exception {

        c = new Conexao().criarConexao();

        try {
            sql = "INSERT INTO hospedes (nome,documento,telefone) VALUES (?,?,?);";
            stmt = c.prepareStatement(sql);
            stmt.setString(1, nomeHospede);
            stmt.setString(2, documento);
            stmt.setString(3, telefone);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception(e.getMessage()+"Problema na base de dados. Pode já haver hospede com estes dados");
        }
    }

   

    public int getIdHospede(String nomeHospede, String documento, String telefone) {

        int id = 0;

        try {
            c = new Conexao().criarConexao();

           
            if (!nomeHospede.isEmpty()) {
                stmt = c.prepareStatement("SELECT id FROM hospedes WHERE hospedes.nome = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, nomeHospede);
                ResultSet resultado = stmt.executeQuery();
                if (resultado.first()) {
                    id = resultado.getInt("id");
                }
            } else if (!documento.isEmpty()) {
                stmt = c.prepareStatement("SELECT id FROM hospedes WHERE documento = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, documento);
                ResultSet resultado = stmt.executeQuery();
                if (resultado.first()) {
                    id = resultado.getInt("id");
                }

            } else if (!telefone.isEmpty()) {
                stmt = c.prepareStatement("SELECT id FROM hospedes WHERE hospedes.telefone = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, telefone);
                ResultSet resultado = stmt.executeQuery();
                if (resultado.first()) {
                    id = resultado.getInt("id");
                }

            } else {
                throw new Exception("Problema dados incorretos");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

   
    public ArrayList<Hospede> getHospedesAtuais() throws Exception {
        try {
            c = new Conexao().criarConexao();

            
            LocalDateTime agora = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                String agoraStr = formato.format(agora); 
                Timestamp ag = Timestamp.valueOf(agoraStr);

            stmt = c.prepareStatement("SELECT r.id, r.nome, r.telefone, r.documento FROM hospedagem h JOIN hospedes r "
                    + "ON h.hospede = r.id WHERE h.datasaida > ? GROUP BY r.id;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setTimestamp(1, ag);
            
            ResultSet resultado = stmt.executeQuery();
            resultado.last();
            int tam = resultado.getRow();
            resultado.first();

            ArrayList<Hospede> hospedes = new ArrayList<Hospede>();

            if (tam > 0) {
                int id;
                String nome;
                String telefone;
                String documento;
                resultado.first();

                for (int a = 0; a < tam; a++) {
                    
                    id = resultado.getInt(1);
                    nome = resultado.getString(2);
                    telefone = resultado.getString(3);
                    documento = resultado.getString(4);
                    
                    Hospede aux = new Hospede(id, nome, documento, telefone);
                    aux.calcularGastosTotais();
                    hospedes.add(aux);
                    resultado.next();
                    
                }
                return hospedes;
            }else{
                return null;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public ArrayList<Hospede> getHospedesAnteriores() throws Exception {
        try {
            c = new Conexao().criarConexao();
            
            LocalDateTime agora = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                String agoraStr = formato.format(agora); 
                Timestamp ag = Timestamp.valueOf(agoraStr);

            stmt = c.prepareStatement("SELECT r.id, r.nome, r.telefone, r.documento FROM hospedagem h JOIN hospedes r "
                    + "ON h.hospede = r.id WHERE h.datasaida < ? AND r.id NOT IN (SELECT r.id FROM hospedagem h JOIN hospedes r "
                    + "ON h.hospede = r.id WHERE h.datasaida > ? GROUP BY r.id)"
                    + "GROUP BY r.id;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setTimestamp(1, ag);
            stmt.setTimestamp(2, ag);
            ResultSet resultado = stmt.executeQuery();
            resultado.last();
            int tam = resultado.getRow();
            resultado.first();

            ArrayList<Hospede> hospedes = new ArrayList<Hospede>();

            if (tam > 0) {
                int id;
                String nome;
                String telefone;
                String documento;
                resultado.first();

                for (int a = 0; a < tam; a++) {
                    id = resultado.getInt(1);
                    nome = resultado.getString(2);
                    telefone = resultado.getString(3);
                    documento = resultado.getString(4);

                    Hospede aux = new Hospede(id, nome, documento, telefone);
                    aux.calcularGastosTotais();
                    hospedes.add(aux);
                    resultado.next();
                }
                return hospedes;
            }else{
                return null;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }
}
