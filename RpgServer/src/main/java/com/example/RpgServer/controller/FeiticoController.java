package com.example.RpgServer.controller;

import com.example.RpgServer.dao.FeiticoDAO;
import com.example.RpgServer.model.Feitico;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feiticos")
public class FeiticoController {

    private final FeiticoDAO feiticoDAO;

    public FeiticoController(FeiticoDAO feiticoDAO) {
        this.feiticoDAO = feiticoDAO;
    }

    @GetMapping
    public List<Feitico> listarTodos() throws Exception {
        return feiticoDAO.listar();
    }

    @GetMapping("/{id}")
    public Feitico obter(@PathVariable int id) throws Exception {
        return feiticoDAO.obter(id);
    }

    @PostMapping
    public Feitico salvar(@RequestBody Feitico feitico) throws Exception {
        return feiticoDAO.incluir(feitico);
    }

    @PutMapping("/{id}")
    public Feitico atualizar(@PathVariable int id, @RequestBody Feitico feitico) throws Exception {
        feitico.setId(id);
        return feiticoDAO.atualizar(feitico);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        return feiticoDAO.excluir(id);
    }
}
