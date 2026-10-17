package com.example.RpgServer.dao;

import com.example.RpgServer.model.Feitico;
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
public class FeiticoDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Feitico incluir(Feitico f) throws Exception {
        String sqlInsert = "INSERT INTO Feitico (NOME, DESCRICAO) VALUES (?, ?)";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, f.getNome());
            ps.setString(2, f.getDescricao());
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                f.setId(rs.getInt(1));
                return f;
            }
        }
        throw new Exception("Erro na inserção do feitiço.");
    }

    public List<Feitico> listar() throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO FROM Feitico";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlSelect);
             ResultSet rs = ps.executeQuery()) {
            List<Feitico> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public Feitico obter(int id) throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO FROM Feitico WHERE id = ?";
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

    public Feitico atualizar(Feitico f) throws Exception {
        String sqlUpdate = "UPDATE Feitico SET NOME = ?, DESCRICAO = ? WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
            ps.setString(1, f.getNome());
            ps.setString(2, f.getDescricao());
            ps.setInt(3, f.getId());
            int result = ps.executeUpdate();
            if (result == 1) {
                return f;
            }
        }
        throw new Exception("Erro na atualização do feitiço.");
    }

    public String excluir(int id) throws Exception {
        String sqlDelete = "DELETE FROM Feitico WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlDelete)) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result == 1) {
                return "Excluído com sucesso";
            }
            throw new Exception("Erro na exclusão do feitiço");
        }
    }

    private Feitico mapear(ResultSet rs) throws Exception {
        Feitico f = new Feitico();
        f.setId(rs.getInt("id"));
        f.setNome(rs.getString("nome"));
        f.setDescricao(rs.getString("descricao"));
        return f;
    }
}
