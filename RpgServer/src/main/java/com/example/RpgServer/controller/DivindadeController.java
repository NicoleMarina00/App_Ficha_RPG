package com.example.RpgServer.controller;

import com.example.RpgServer.dao.DivindadeDAO;
import com.example.RpgServer.model.Divindade;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/divindades")
public class DivindadeController {

    private final DivindadeDAO divindadeDAO;

    public DivindadeController(DivindadeDAO divindadeDAO) {
        this.divindadeDAO = divindadeDAO;
    }

    @GetMapping
    public List<Divindade> listarTodos() throws Exception {
        return divindadeDAO.listar();
    }

    @GetMapping("/{id}")
    public Divindade obter(@PathVariable int id) throws Exception {
        return divindadeDAO.obter(id);
    }

    @PostMapping
    public Divindade salvar(@RequestBody Divindade divindade) throws Exception {
        return divindadeDAO.incluir(divindade);
    }

    @PutMapping("/{id}")
    public Divindade atualizar(@PathVariable int id, @RequestBody Divindade divindade) throws Exception {
        divindade.setId(id);
        return divindadeDAO.atualizar(divindade);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        return divindadeDAO.excluir(id);
    }
}
