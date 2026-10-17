package com.example.RpgServer.controller;

import com.example.RpgServer.dao.EscudoDAO;
import com.example.RpgServer.model.Escudo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/escudos")
public class EscudoController {

    private final EscudoDAO escudoDAO;

    public EscudoController(EscudoDAO escudoDAO) {
        this.escudoDAO = escudoDAO;
    }

    @GetMapping
    public List<Escudo> listarTodos() throws Exception {
        return escudoDAO.listar();
    }

    @GetMapping("/{id}")
    public Escudo obter(@PathVariable int id) throws Exception {
        return escudoDAO.obter(id);
    }

    @PostMapping
    public Escudo salvar(@RequestBody Escudo escudo) throws Exception {
        return escudoDAO.incluir(escudo);
    }

    @PutMapping("/{id}")
    public Escudo atualizar(@PathVariable int id, @RequestBody Escudo escudo) throws Exception {
        escudo.setId(id);
        return escudoDAO.atualizar(escudo);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        return escudoDAO.excluir(id);
    }
}
