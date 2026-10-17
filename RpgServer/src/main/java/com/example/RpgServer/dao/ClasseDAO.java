package com.example.RpgServer.dao;

import com.example.RpgServer.model.Classe;
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
public class ClasseDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Classe incluir(Classe c) throws Exception {
        String sqlInsert = "INSERT INTO Classe (NOME, DESCRICAO) VALUES (?, ?)";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getDescricao());
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                c.setId(rs.getInt(1));
                return c;
            }
        }
        throw new Exception("Erro na inserção da classe.");
    }

    public List<Classe> listar() throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO FROM Classe";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlSelect);
             ResultSet rs = ps.executeQuery()) {
            List<Classe> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public Classe obter(int id) throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO FROM Classe WHERE id = ?";
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

    public Classe atualizar(Classe c) throws Exception {
        String sqlUpdate = "UPDATE Classe SET NOME = ?, DESCRICAO = ? WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getDescricao());
            ps.setInt(3, c.getId());
            int result = ps.executeUpdate();
            if (result == 1) {
                return c;
            }
        }
        throw new Exception("Erro na atualização da classe.");
    }

    public String excluir(int id) throws Exception {
        String sqlDelete = "DELETE FROM Classe WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlDelete)) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result == 1) {
                return "Excluído com sucesso";
            }
            throw new Exception("Erro na exclusão da classe");
        }
    }

    private Classe mapear(ResultSet rs) throws Exception {
        Classe c = new Classe();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setDescricao(rs.getString("descricao"));
        return c;
    }
}
