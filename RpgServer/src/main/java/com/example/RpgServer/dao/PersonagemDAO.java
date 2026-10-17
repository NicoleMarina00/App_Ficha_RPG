package com.example.RpgServer.dao;

import com.example.RpgServer.model.Personagem;
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
public class PersonagemDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Personagem incluir(Personagem p) throws Exception {
        String sqlInsert = "INSERT INTO Personagem (NOME, FORCA, INTELIGENCIA, DESTREZA, SABEDORIA, CARISMA, RACA_JSON, CLASSE_JSON, ARMA_JSON, ARMADURA_JSON, ESCUDO_JSON, DIVINDADE_JSON, PERICIA_JSON, FEITICO_JSON) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNome());
            ps.setInt(2, p.getForca());
            ps.setInt(3, p.getInteligencia());
            ps.setInt(4, p.getDestreza());
            ps.setInt(5, p.getSabedoria());
            ps.setInt(6, p.getCarisma());
            ps.setString(7, p.getRacaJson());
            ps.setString(8, p.getClasseJson());
            ps.setString(9, p.getArmaJson());
            ps.setString(10, p.getArmaduraJson());
            ps.setString(11, p.getEscudoJson());
            ps.setString(12, p.getDivindadeJson());
            ps.setString(13, p.getPericiaJson());
            ps.setString(14, p.getFeiticoJson());
            int result = ps.executeUpdate();
            if (result == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                p.setId(rs.getInt(1));
                return p;
            }
        }
        throw new Exception("Erro na inserção do personagem.");
    }

    public List<Personagem> listar() throws Exception {
        String sqlSelect = "SELECT ID, NOME, FORCA, INTELIGENCIA, DESTREZA, SABEDORIA, CARISMA, RACA_JSON, CLASSE_JSON, ARMA_JSON, ARMADURA_JSON, ESCUDO_JSON, DIVINDADE_JSON, PERICIA_JSON, FEITICO_JSON FROM Personagem";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlSelect);
             ResultSet rs = ps.executeQuery()) {
            List<Personagem> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    public Personagem obter(int id) throws Exception {
        String sqlSelect = "SELECT ID, NOME, FORCA, INTELIGENCIA, DESTREZA, SABEDORIA, CARISMA, RACA_JSON, CLASSE_JSON, ARMA_JSON, ARMADURA_JSON, ESCUDO_JSON, DIVINDADE_JSON, PERICIA_JSON, FEITICO_JSON FROM Personagem WHERE id = ?";
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

    public Personagem atualizar(Personagem p) throws Exception {
        String sqlUpdate = "UPDATE Personagem SET NOME = ?, FORCA = ?, INTELIGENCIA = ?, DESTREZA = ?, SABEDORIA = ?, CARISMA = ?, RACA_JSON = ?, CLASSE_JSON = ?, ARMA_JSON = ?, ARMADURA_JSON = ?, ESCUDO_JSON = ?, DIVINDADE_JSON = ?, PERICIA_JSON = ?, FEITICO_JSON = ? WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
            ps.setString(1, p.getNome());
            ps.setInt(2, p.getForca());
            ps.setInt(3, p.getInteligencia());
            ps.setInt(4, p.getDestreza());
            ps.setInt(5, p.getSabedoria());
            ps.setInt(6, p.getCarisma());
            ps.setString(7, p.getRacaJson());
            ps.setString(8, p.getClasseJson());
            ps.setString(9, p.getArmaJson());
            ps.setString(10, p.getArmaduraJson());
            ps.setString(11, p.getEscudoJson());
            ps.setString(12, p.getDivindadeJson());
            ps.setString(13, p.getPericiaJson());
            ps.setString(14, p.getFeiticoJson());
            ps.setInt(15, p.getId());
            int result = ps.executeUpdate();
            if (result == 1) {
                return p;
            }
        }
        throw new Exception("Erro na atualização do personagem.");
    }

    public String excluir(int id) throws Exception {
        String sqlDelete = "DELETE FROM Personagem WHERE id = ?";
        try (Connection con = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlDelete)) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            if (result == 1) {
                return "Excluído com sucesso";
            }
            throw new Exception("Erro na exclusão do personagem");
        }
    }

    private Personagem mapear(ResultSet rs) throws Exception {
        Personagem p = new Personagem();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setForca(rs.getInt("forca"));
        p.setInteligencia(rs.getInt("inteligencia"));
        p.setDestreza(rs.getInt("destreza"));
        p.setSabedoria(rs.getInt("sabedoria"));
        p.setCarisma(rs.getInt("carisma"));
        p.setRacaJson(rs.getString("raca_json"));
        p.setClasseJson(rs.getString("classe_json"));
        p.setArmaJson(rs.getString("arma_json"));
        p.setArmaduraJson(rs.getString("armadura_json"));
        p.setEscudoJson(rs.getString("escudo_json"));
        p.setDivindadeJson(rs.getString("divindade_json"));
        p.setPericiaJson(rs.getString("pericia_json"));
        p.setFeiticoJson(rs.getString("feitico_json"));
        return p;
    }
}
