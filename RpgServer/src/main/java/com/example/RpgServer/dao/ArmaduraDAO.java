package com.example.RpgServer.dao;

import com.example.RpgServer.model.Armadura;
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
public class ArmaduraDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Armadura incluir(Armadura a) throws Exception {
        String sqlInsert = "INSERT INTO Armadura (NOME, DESCRICAO, VALOR_DEFESA) VALUES (?, ?, ?)";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getDescricao());
            ps.setString(3, a.getValorDefesa());
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                a.setId(rs.getInt(1));
                return a;
            }
        }
        throw new Exception("Erro na inserção da armadura.");
    }

    public List<Armadura> listar() throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO, VALOR_DEFESA FROM Armadura";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlSelect);
             ResultSet rs = ps.executeQuery()) {
            List<Armadura> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public Armadura obter(int id) throws Exception {
        String sqlSelect = "SELECT ID, NOME, DESCRICAO, VALOR_DEFESA FROM Armadura WHERE id = ?";
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

    public Armadura atualizar(Armadura a) throws Exception {
        String sqlUpdate = "UPDATE Armadura SET NOME = ?, DESCRICAO = ?, VALOR_DEFESA = ? WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getDescricao());
            ps.setString(3, a.getValorDefesa());
            ps.setInt(4, a.getId());
            int result = ps.executeUpdate();
            if (result == 1) {
                return a;
            }
        }
        throw new Exception("Erro na atualização da armadura.");
    }

    public String excluir(int id) throws Exception {
        Integer usosNoInventario = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INVENTARIO WHERE TIPO_ITEM = 'ARMADURA' AND ITEM_ID = ?", Integer.class, id);
        if (usosNoInventario != null && usosNoInventario > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A armadura ainda está associada a um inventário.");
        }
        String sqlDelete = "DELETE FROM Armadura WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlDelete)) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result == 1) {
                return "Excluído com sucesso";
            }
            throw new Exception("Erro na exclusão da armadura");
        }
    }

    private Armadura mapear(ResultSet rs) throws Exception {
        Armadura a = new Armadura();
        a.setId(rs.getInt("id"));
        a.setNome(rs.getString("nome"));
        a.setDescricao(rs.getString("descricao"));
        a.setValorDefesa(rs.getString("valor_defesa"));
        return a;
    }
}
