package com.example.RpgServer.controller;

import com.example.RpgServer.dao.ArmaduraDAO;
import com.example.RpgServer.model.Armadura;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/armaduras")
public class ArmaduraController {

    private final ArmaduraDAO armaduraDAO;

    public ArmaduraController(ArmaduraDAO armaduraDAO) {
        this.armaduraDAO = armaduraDAO;
    }

    @GetMapping
    public List<Armadura> listarTodos() throws Exception {
        return armaduraDAO.listar();
    }

    @GetMapping("/{id}")
    public Armadura obter(@PathVariable int id) throws Exception {
        return armaduraDAO.obter(id);
    }

    @PostMapping
    public Armadura salvar(@RequestBody Armadura armadura) throws Exception {
        return armaduraDAO.incluir(armadura);
    }

    @PutMapping("/{id}")
    public Armadura atualizar(@PathVariable int id, @RequestBody Armadura armadura) throws Exception {
        armadura.setId(id);
        return armaduraDAO.atualizar(armadura);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        return armaduraDAO.excluir(id);
    }
}
