package com.example.RpgServer.dao;

import com.example.RpgServer.model.Inventario;
import com.example.RpgServer.model.TipoItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.server.ResponseStatusException;

import java.sql.PreparedStatement;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class InventarioDAOTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private InventarioDAO inventarioDAO;

    @Autowired
    private ArmaDAO armaDAO;

    private int personagemId;
    private int armaId;

    @BeforeEach
    void prepararDados() {
        personagemId = inserir("INSERT INTO PERSONAGEM (NOME) VALUES (?)", "Teste Inventário");
        armaId = inserir("INSERT INTO ARMA (NOME, DESCRICAO, VALOR_ATAQUE) VALUES (?, ?, ?)",
                "Espada de teste", "Arma criada para teste", "1d8");
    }

    @AfterEach
    void limparDados() {
        jdbcTemplate.update("DELETE FROM INVENTARIO WHERE PERSONAGEM_ID = ?", personagemId);
        jdbcTemplate.update("DELETE FROM ARMA WHERE ID = ?", armaId);
        jdbcTemplate.update("DELETE FROM PERSONAGEM WHERE ID = ?", personagemId);
    }

    @Test
    void persisteListaAtualizaEExcluiItemDeInventario() {
        Inventario novo = criarInventario();

        Inventario salvo = inventarioDAO.incluir(novo);
        assertTrue(salvo.getId() > 0);
        assertEquals("Espada de teste", salvo.getNome());
        assertEquals("Teste Inventário", salvo.getPersonagemNome());
        assertEquals(1, inventarioDAO.listar().size());

        salvo.setQuantidade(3);
        Inventario atualizado = inventarioDAO.atualizar(salvo);
        assertEquals(3, atualizado.getQuantidade());
        assertEquals(salvo.getId(), inventarioDAO.obter(salvo.getId()).getId());

        inventarioDAO.excluir(salvo.getId());
        assertNull(inventarioDAO.obter(salvo.getId()));
    }

    @Test
    void rejeitaReferenciaDeItemInexistente() {
        Inventario inventario = criarInventario();
        inventario.setItemId(Integer.MAX_VALUE);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> inventarioDAO.incluir(inventario));
        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
    }

    @Test
    void impedeExcluirArmaAindaAssociadaAoInventario() {
        Inventario salvo = inventarioDAO.incluir(criarInventario());

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> armaDAO.excluir(armaId));
        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());

        inventarioDAO.excluir(salvo.getId());
    }

    private Inventario criarInventario() {
        Inventario inventario = new Inventario();
        inventario.setPersonagemId(personagemId);
        inventario.setTipoItem(TipoItem.ARMA);
        inventario.setItemId(armaId);
        inventario.setQuantidade(1);
        return inventario;
    }

    private int inserir(String sql, String... valores) {
        KeyHolder chaves = new GeneratedKeyHolder();
        jdbcTemplate.update(conexao -> {
            PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < valores.length; i++) {
                ps.setString(i + 1, valores[i]);
            }
            return ps;
        }, chaves);
        Number id = chaves.getKey();
        if (id == null) {
            throw new IllegalStateException("O banco não retornou o identificador do dado de teste.");
        }
        return id.intValue();
    }
}
