package com.example.RpgServer.controller;

import com.example.RpgServer.dao.RacaDAO;
import com.example.RpgServer.model.Raca;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/racas")
public class RacaController {

    private final RacaDAO racaDAO;

    public RacaController(RacaDAO racaDAO) {
        this.racaDAO = racaDAO;
    }

    @GetMapping
    public List<Raca> listarTodos() throws Exception {
        return racaDAO.listar();
    }

    @GetMapping("/{id}")
    public Raca obter(@PathVariable int id) throws Exception {
        return racaDAO.obter(id);
    }

    @PostMapping
    public Raca salvar(@RequestBody Raca raca) throws Exception {
        return racaDAO.incluir(raca);
    }

    @PutMapping("/{id}")
    public Raca atualizar(@PathVariable int id, @RequestBody Raca raca) throws Exception {
        raca.setId(id);
        return racaDAO.atualizar(raca);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        return racaDAO.excluir(id);
    }
}
