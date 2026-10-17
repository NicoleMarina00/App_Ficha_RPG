package com.example.RpgServer.dao;

import com.example.RpgServer.model.Pericia;
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
public class PericiaDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Pericia incluir(Pericia p) throws Exception {
        String sqlInsert = "INSERT INTO Pericia (NOME, DESCRICAO) VALUES (?, ?)";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getDescricao());
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                p.setId(rs.getInt(1));
                return p;
            }
        }
        throw new Exception("Erro na inserção da perícia.");
    }

    public List<Pericia> listar() throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO FROM Pericia";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlSelect);
             ResultSet rs = ps.executeQuery()) {
            List<Pericia> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public Pericia obter(int id) throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO FROM Pericia WHERE id = ?";
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

    public Pericia atualizar(Pericia p) throws Exception {
        String sqlUpdate = "UPDATE Pericia SET NOME = ?, DESCRICAO = ? WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getDescricao());
            ps.setInt(3, p.getId());
            int result = ps.executeUpdate();
            if (result == 1) {
                return p;
            }
        }
        throw new Exception("Erro na atualização da perícia.");
    }

    public String excluir(int id) throws Exception {
        String sqlDelete = "DELETE FROM Pericia WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlDelete)) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result == 1) {
                return "Excluído com sucesso";
            }
            throw new Exception("Erro na exclusão da perícia");
        }
    }

    private Pericia mapear(ResultSet rs) throws Exception {
        Pericia p = new Pericia();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setDescricao(rs.getString("descricao"));
        return p;
    }
}
