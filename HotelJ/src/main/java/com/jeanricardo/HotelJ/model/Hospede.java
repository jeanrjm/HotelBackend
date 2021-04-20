/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeanricardo.HotelJ.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Jean
 */
public class Hospede {

    private int id;
    private String nome;
    private String documento;
    private String telefone;
    private float gasto;
    private Connection c;
    private PreparedStatement stmt;
    private String sql;

    public String getNome() {
        return this.nome;
    }

    public String getDocumento() {
        return this.documento;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public float getGasto() {
        return this.gasto;
    }

    public int getId() {
        return this.id;
    }

    private float calcularCheckout(LocalDateTime inicio, LocalDateTime fim, boolean usaGaragem) throws Exception {

        int diaInicial = inicio.getDayOfWeek().getValue();
        int diaFinal = fim.getDayOfWeek().getValue();
        int doyInicio = inicio.getDayOfYear();
        int doyFim = fim.getDayOfYear();
        int diasPassados = doyFim - doyInicio + 1;

        //decide se paga diaria no ultimo dia/calendario
        if (fim.getYear() != inicio.getYear()) {
            int anos = fim.getYear() - inicio.getYear();
            if (anos <= 1) {
                diasPassados = (365 - doyInicio) + doyFim;
            } else {
                anos = - 1;
                int diasAnosMeio = anos * 365;
                diasPassados = (365 - doyInicio) + doyFim + diasAnosMeio;
            }
        }
        LocalDateTime extra = LocalDateTime.of(fim.getYear(), fim.getMonth(), fim.getDayOfMonth(), 16, 30);
        long limiteSemExtra = extra.toEpochSecond(ZoneOffset.UTC);
        long millisFim = fim.toEpochSecond(ZoneOffset.UTC);
        boolean pagaExtra = false;
        if (millisFim - limiteSemExtra > 0) {
            pagaExtra = true;
            System.out.println("vai pagar extra");
        }

        int diaAtual = diaInicial;
        float montante = 0;
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
        System.out.println("montante" + montante);
        return montante;
    }

    public void calcularGastosTotais() throws Exception {
        c = new Conexao().criarConexao();
        stmt = c.prepareStatement("SELECT SUM(custoTotal) FROM hospedagem WHERE hospede =?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stmt.setInt(1, id);

        ResultSet resultado = stmt.executeQuery();
        resultado.last();
        int tam = resultado.getRow();
        float auxGasto = 0;

        if (tam > 0) {
            resultado.first();
            auxGasto = resultado.getFloat(1);
        }
        
        this.gasto = auxGasto;
    }

     public boolean checkIn(boolean adicionalVeiculo, String entrada, String saida) throws Exception {
        try {
            c = new Conexao().criarConexao();

            if (id != 0) {
                LocalDateTime inicio = LocalDateTime.parse(entrada);
                LocalDateTime fim = LocalDateTime.parse(saida);

                DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                entrada = formato.format(inicio); 
                saida = formato.format(fim);
                
                Timestamp i = Timestamp.valueOf(entrada);
                Timestamp f = Timestamp.valueOf(saida);
                float custo = calcularCheckout(inicio, fim, adicionalVeiculo);

                sql = "INSERT INTO hospedagem (hospede,dataEntrada,dataSaida, adicionalVeiculo, custoTotal) VALUES (?,?,?,?,?);";
                stmt = c.prepareStatement(sql);
                stmt.setInt(1, id);
               stmt.setTimestamp(2, i);
               stmt.setTimestamp(3, f);
                stmt.setBoolean(4, adicionalVeiculo);
                stmt.setFloat(5, custo);
                stmt.executeUpdate();
                
            } else {
                throw new Exception("Não conseguiu fazer CheckIn");
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
    
    
    public Hospede(int id, String nome, String documento, String telefone) {
        this.id = id;
        this.documento = documento;
        this.nome = nome;
        this.telefone = telefone;

    }

    public Hospede(int id) {
        this.id = id;
    }

}
