package com.example.RpgServer.dao;

import com.example.RpgServer.model.Inventario;
import com.example.RpgServer.model.TipoItem;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class InventarioDAO {
    private final JdbcTemplate jdbcTemplate;

    public InventarioDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Inventario incluir(Inventario inventario) {
        validarReferencias(inventario);
        String sql = "INSERT INTO INVENTARIO (PERSONAGEM_ID, TIPO_ITEM, ITEM_ID, QUANTIDADE) VALUES (?, ?, ?, ?)";
        KeyHolder chaves = new GeneratedKeyHolder();
        jdbcTemplate.update(conexao -> {
            PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, inventario.getPersonagemId());
            ps.setString(2, inventario.getTipoItem().name());
            ps.setInt(3, inventario.getItemId());
            ps.setInt(4, inventario.getQuantidade());
            return ps;
        }, chaves);
        Number id = chaves.getKey();
        if (id == null) {
            throw new IllegalStateException("O banco não retornou o identificador do inventário criado.");
        }
        return obter(id.intValue());
    }

    public List<Inventario> listar() {
        String sql = "SELECT I.ID, I.PERSONAGEM_ID, P.NOME AS PERSONAGEM_NOME, I.TIPO_ITEM, I.ITEM_ID, I.QUANTIDADE "
                + "FROM INVENTARIO I JOIN PERSONAGEM P ON P.ID = I.PERSONAGEM_ID ORDER BY I.ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapear(rs));
    }

    public Inventario obter(int id) {
        String sql = "SELECT I.ID, I.PERSONAGEM_ID, P.NOME AS PERSONAGEM_NOME, I.TIPO_ITEM, I.ITEM_ID, I.QUANTIDADE "
                + "FROM INVENTARIO I JOIN PERSONAGEM P ON P.ID = I.PERSONAGEM_ID WHERE I.ID = ?";
        List<Inventario> resultados = jdbcTemplate.query(sql, (rs, rowNum) -> mapear(rs), id);
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public Inventario atualizar(Inventario inventario) {
        validarReferencias(inventario);
        String sql = "UPDATE INVENTARIO SET PERSONAGEM_ID = ?, TIPO_ITEM = ?, ITEM_ID = ?, QUANTIDADE = ? WHERE ID = ?";
        int atualizados = jdbcTemplate.update(sql, inventario.getPersonagemId(), inventario.getTipoItem().name(),
                inventario.getItemId(), inventario.getQuantidade(), inventario.getId());
        if (atualizados == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro de inventário não encontrado.");
        }
        return obter(inventario.getId());
    }

    public void excluir(int id) {
        int excluidos = jdbcTemplate.update("DELETE FROM INVENTARIO WHERE ID = ?", id);
        if (excluidos == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro de inventário não encontrado.");
        }
    }

    private Inventario mapear(java.sql.ResultSet rs) throws java.sql.SQLException {
        Inventario inventario = new Inventario();
        inventario.setId(rs.getInt("ID"));
        inventario.setPersonagemId(rs.getInt("PERSONAGEM_ID"));
        inventario.setPersonagemNome(rs.getString("PERSONAGEM_NOME"));
        inventario.setTipoItem(TipoItem.valueOf(rs.getString("TIPO_ITEM")));
        inventario.setItemId(rs.getInt("ITEM_ID"));
        inventario.setQuantidade(rs.getInt("QUANTIDADE"));
        inventario.setNome(buscarNomeItem(inventario.getTipoItem(), inventario.getItemId()));
        return inventario;
    }

    private void validarReferencias(Inventario inventario) {
        if (inventario.getPersonagemId() <= 0 || inventario.getItemId() <= 0 || inventario.getQuantidade() <= 0
                || inventario.getTipoItem() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Personagem, tipo de item e identificador devem ser válidos; quantidade precisa ser positiva.");
        }
        Integer personagemExiste = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM PERSONAGEM WHERE ID = ?", Integer.class, inventario.getPersonagemId());
        if (personagemExiste == null || personagemExiste == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O personagem informado não existe.");
        }
        Integer itemExiste = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM " + inventario.getTipoItem().getTabela() + " WHERE ID = ?",
                Integer.class, inventario.getItemId());
        if (itemExiste == null || itemExiste == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O item informado não existe no catálogo.");
        }
    }

    private String buscarNomeItem(TipoItem tipoItem, int itemId) {
        return jdbcTemplate.queryForObject(
                "SELECT NOME FROM " + tipoItem.getTabela() + " WHERE ID = ?", String.class, itemId);
    }
}
