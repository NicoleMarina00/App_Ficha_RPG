package com.example.RpgServer.dao;

import com.example.RpgServer.model.Divindade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DivindadeDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Divindade incluir(Divindade d) throws Exception {
        String sqlInsert = "INSERT INTO Divindade (NOME, DESCRICAO) VALUES (?, ?)";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getNome());
            ps.setString(2, d.getDescricao());
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                d.setId(rs.getInt(1));
                return d;
            }
        }
        throw new Exception("Erro na inserção da divindade.");
    }

    public List<Divindade> listar() throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO FROM Divindade";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlSelect);
             ResultSet rs = ps.executeQuery()) {
            List<Divindade> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public Divindade obter(int id) throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO FROM Divindade WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlSelect)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
            return null;
        }
    }

    public Divindade atualizar(Divindade d) throws Exception {
        String sqlUpdate = "UPDATE Divindade SET NOME = ?, DESCRICAO = ? WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
            ps.setString(1, d.getNome());
            ps.setString(2, d.getDescricao());
            ps.setInt(3, d.getId());
            int result = ps.executeUpdate();
            if (result == 1) {
                return d;
            }
        }
        throw new Exception("Erro na atualização da divindade.");
    }

    public String excluir(int id) throws Exception {
        String sqlDelete = "DELETE FROM Divindade WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlDelete)) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result == 1) {
                return "Excluído com sucesso";
            }
            throw new Exception("Erro na exclusão da divindade");
        }
    }

    private Divindade mapear(ResultSet rs) throws Exception {
        Divindade d = new Divindade();
        d.setId(rs.getInt("id"));
        d.setNome(rs.getString("nome"));
        d.setDescricao(rs.getString("descricao"));
        return d;
    }
}
