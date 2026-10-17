package com.example.RpgServer.dao;

import com.example.RpgServer.model.Raca;
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
public class RacaDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Raca incluir(Raca r) throws Exception {
        String sqlInsert = "INSERT INTO Raca (NOME, DESCRICAO, HABILIDADE) VALUES (?, ?, ?)";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getNome());
            ps.setString(2, r.getDescricao());
            ps.setString(3, r.getHabilidade());
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                r.setId(rs.getInt(1));
                return r;
            }
        }
        throw new Exception("Erro na inserção da raça.");
    }

    public List<Raca> listar() throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO, HABILIDADE FROM Raca";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlSelect);
             ResultSet rs = ps.executeQuery()) {
            List<Raca> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public Raca obter(int id) throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO, HABILIDADE FROM Raca WHERE id = ?";
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

    public Raca atualizar(Raca r) throws Exception {
        String sqlUpdate = "UPDATE Raca SET NOME = ?, DESCRICAO = ?, HABILIDADE = ? WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
            ps.setString(1, r.getNome());
            ps.setString(2, r.getDescricao());
            ps.setString(3, r.getHabilidade());
            ps.setInt(4, r.getId());
            int result = ps.executeUpdate();
            if (result == 1) {
                return r;
            }
        }
        throw new Exception("Erro na atualização da raça.");
    }

    public String excluir(int id) throws Exception {
        String sqlDelete = "DELETE FROM Raca WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlDelete)) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result == 1) {
                return "Excluído com sucesso";
            }
            throw new Exception("Erro na exclusão da raça");
        }
    }

    private Raca mapear(ResultSet rs) throws Exception {
        Raca r = new Raca();
        r.setId(rs.getInt("id"));
        r.setNome(rs.getString("nome"));
        r.setDescricao(rs.getString("descricao"));
        r.setHabilidade(rs.getString("habilidade"));
        return r;
    }
}
