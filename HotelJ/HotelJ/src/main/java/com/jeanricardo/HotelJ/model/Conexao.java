/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jeanricardo.HotelJ.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jean
 */
public class Conexao {

    private Connection c;
    private PreparedStatement stm;
    private String sql;

    public Connection criarConexao() throws Exception {
        try {
            Class.forName("org.postgresql.Driver");

            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/hotel",
                            "postgres", "adm");

            return c;
        } catch (Exception ex) {
            throw new Exception("Problema ao se conectar base de dados");
        }
    }

}
