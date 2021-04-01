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
            stmt.setInt(2, Integer.parseInt(documento));
            stmt.setInt(3, Integer.parseInt(telefone));
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Problema na base de dados");
        }
    }

    public void checkIn(String nomeHospede, String documento, String telefone, boolean adicionalVeiculo) throws Exception {
        try {
            c = new Conexao().criarConexao();

            int id = getIdHospede(nomeHospede, documento, telefone);

            if (id != 0) {
                stmt = c.prepareStatement("SELECT id FROM hospedagem WHERE hospede = ? AND dataSaida IS NULL", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setInt(1, id);
                ResultSet resultado = stmt.executeQuery();
                resultado.last();
                int tam = resultado.getRow();
                System.out.println("row" + tam);
                if (tam > 0) {
                    throw new Exception("Já existe hospedagem aberta");

                }

                DateTimeFormatter formato = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                LocalDateTime dataHora = LocalDateTime.now();

                String data = formato.format(dataHora);

                sql = "INSERT INTO hospedagem (hospede,dataEntrada,adicionalVeiculo) VALUES (?,?,?);";
                stmt = c.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.setString(2, data);
                stmt.setBoolean(3, adicionalVeiculo);
                stmt.executeUpdate();
            } else {
                throw new Exception("Não conseguiu fazer CheckIn");
            }

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

    }

    public void checkOut(String nomeHospede, String documento, String telefone) {
        try {
            c = new Conexao().criarConexao();

            int id = getIdHospede(nomeHospede, documento, telefone);
            int idHospedagem = 0;
            String dataEntradaStr = "";
            boolean usaGaragem;
            if (id != 0) {
                stmt = c.prepareStatement("SELECT * FROM hospedagem WHERE hospede = ? AND dataSaida IS NULL", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setInt(1, id);
                ResultSet resultado = stmt.executeQuery();

                resultado.last();
                int tam = resultado.getRow();
                if (tam > 0) {
                    resultado.first();
                    idHospedagem = resultado.getInt("id");
                    dataEntradaStr = resultado.getString("dataentrada");
                    usaGaragem = resultado.getBoolean("adicionalveiculo");

                    LocalDateTime inicio = LocalDateTime.parse(dataEntradaStr);

                    LocalDateTime fim = LocalDateTime.now();

                    int diaInicial = inicio.getDayOfWeek().getValue();
                    int diaFinal = fim.getDayOfWeek().getValue();
                    int doyInicio = inicio.getDayOfYear();
                    int doyFim = fim.getDayOfYear();
                    int diasPassados = doyFim - doyInicio + 1;

                    //decide se paga diaria no ultimo dia/calendario
                    LocalDateTime extra = LocalDateTime.of(fim.getYear(), fim.getMonth(), fim.getDayOfMonth(), 16, 30);
                    long limiteSemExtra = extra.toEpochSecond(ZoneOffset.UTC);
                    long millisFim = fim.toEpochSecond(ZoneOffset.UTC);
                    boolean pagaExtra = false;
                    if (millisFim - limiteSemExtra > 0) {
                        pagaExtra = true;
                    }

                    int diaAtual = diaInicial;
                    int montante = 0;
                    System.out.println("diaspassados" + diasPassados);
                    for (int e = 0; e < diasPassados; e++) {
                        if (diaAtual == 8) {
                            diaAtual = 1;
                        }

                        if (diaAtual == 6 || diaAtual == 7) {
                            if (e == diasPassados - 1 && !pagaExtra) {
                                break;
                            } else {
                                montante += 150;
                                if (usaGaragem) {
                                    montante += 20;
                                }
                            }
                        } else {
                            if (e == diasPassados - 1 && !pagaExtra) {
                                break;
                            } else {
                                montante += 120;
                                if (usaGaragem) {
                                    montante += 15;
                                }
                            }
                        }
                        diaAtual++;
                    }

                    DateTimeFormatter formato = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                    String dataFinalStr = formato.format(fim);

                    sql = "UPDATE hospedagem SET dataSaida = ?, custoTotal = ? WHERE id = ?;";
                    System.out.println("montante" + montante);
                    System.out.println("datafin" + dataFinalStr);
                    System.out.println("id" + id);
                    stmt = c.prepareStatement(sql);
                    stmt.setString(1, dataFinalStr);
                    stmt.setFloat(2, montante);
                    stmt.setInt(3, idHospedagem);
                    stmt.executeUpdate();
                    System.out.println("passouaqui");
                    c.close();

                } else {
                    throw new Exception("Não possuem hospedagens sem checkout para este");
                }

            } else {
                throw new Exception("Não conseguiu fazer CheckOut");
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            
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
