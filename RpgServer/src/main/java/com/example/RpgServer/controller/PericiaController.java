package com.example.RpgServer.controller;

import com.example.RpgServer.dao.PericiaDAO;
import com.example.RpgServer.model.Pericia;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pericias")
public class PericiaController {

    private final PericiaDAO periciaDAO;

    public PericiaController(PericiaDAO periciaDAO) {
        this.periciaDAO = periciaDAO;
    }

    @GetMapping
    public List<Pericia> listarTodos() throws Exception {
        return periciaDAO.listar();
    }

    @GetMapping("/{id}")
    public Pericia obter(@PathVariable int id) throws Exception {
        return periciaDAO.obter(id);
    }

    @PostMapping
    public Pericia salvar(@RequestBody Pericia pericia) throws Exception {
        return periciaDAO.incluir(pericia);
    }

    @PutMapping("/{id}")
    public Pericia atualizar(@PathVariable int id, @RequestBody Pericia pericia) throws Exception {
        pericia.setId(id);
        return periciaDAO.atualizar(pericia);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        return periciaDAO.excluir(id);
    }
}
