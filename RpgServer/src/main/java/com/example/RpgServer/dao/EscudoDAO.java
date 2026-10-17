package com.example.RpgServer.dao;

import com.example.RpgServer.model.Escudo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EscudoDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Escudo incluir(Escudo e) throws Exception {
        String sqlInsert = "INSERT INTO Escudo (NOME, DESCRICAO, VALOR_DEFESA) VALUES (?, ?, ?)";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getDescricao());
            ps.setString(3, e.getValorDefesa());
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                e.setId(rs.getInt(1));
                return e;
            }
        }
        throw new Exception("Erro na inserção do escudo.");
    }

    public List<Escudo> listar() throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO, VALOR_DEFESA FROM Escudo";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlSelect);
             ResultSet rs = ps.executeQuery()) {
            List<Escudo> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public Escudo obter(int id) throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO, VALOR_DEFESA FROM Escudo WHERE id = ?";
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

    public Escudo atualizar(Escudo e) throws Exception {
        String sqlUpdate = "UPDATE Escudo SET NOME = ?, DESCRICAO = ?, VALOR_DEFESA = ? WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getDescricao());
            ps.setString(3, e.getValorDefesa());
            ps.setInt(4, e.getId());
            int result = ps.executeUpdate();
            if (result == 1) {
                return e;
            }
        }
        throw new Exception("Erro na atualização do escudo.");
    }

    public String excluir(int id) throws Exception {
        Integer usosNoInventario = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INVENTARIO WHERE TIPO_ITEM = 'ESCUDO' AND ITEM_ID = ?", Integer.class, id);
        if (usosNoInventario != null && usosNoInventario > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O escudo ainda está associado a um inventário.");
        }
        String sqlDelete = "DELETE FROM Escudo WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlDelete)) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result == 1) {
                return "Excluído com sucesso";
            }
            throw new Exception("Erro na exclusão do escudo");
        }
    }

    private Escudo mapear(ResultSet rs) throws Exception {
        Escudo e = new Escudo();
        e.setId(rs.getInt("id"));
        e.setNome(rs.getString("nome"));
        e.setDescricao(rs.getString("descricao"));
        e.setValorDefesa(rs.getString("valor_defesa"));
        return e;
    }
}
