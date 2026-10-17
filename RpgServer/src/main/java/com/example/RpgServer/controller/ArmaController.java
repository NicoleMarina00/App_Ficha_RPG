package com.example.RpgServer.controller;

import com.example.RpgServer.dao.ArmaDAO;
import com.example.RpgServer.model.Arma;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/armas")
public class ArmaController {

    private final ArmaDAO armaDAO;

    public ArmaController(ArmaDAO armaDAO) {
        this.armaDAO = armaDAO;
    }

    @GetMapping
    public List<Arma> listarTodos() throws Exception {
        return armaDAO.listar();
    }

    @GetMapping("/{id}")
    public Arma obter(@PathVariable int id) throws Exception {
        return armaDAO.obter(id);
    }

    @PostMapping
    public Arma salvar(@RequestBody Arma arma) throws Exception {
        return armaDAO.incluir(arma);
    }

    @PutMapping("/{id}")
    public Arma atualizar(@PathVariable int id, @RequestBody Arma arma) throws Exception {
        arma.setId(id);
        return armaDAO.atualizar(arma);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        return armaDAO.excluir(id);
    }
}
