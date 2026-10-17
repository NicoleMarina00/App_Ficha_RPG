package com.example.RpgServer.controller;

import com.example.RpgServer.dao.ClasseDAO;
import com.example.RpgServer.model.Classe;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClasseController {

    private final ClasseDAO classeDAO;

    public ClasseController(ClasseDAO classeDAO) {
        this.classeDAO = classeDAO;
    }

    @GetMapping
    public List<Classe> listarTodos() throws Exception {
        return classeDAO.listar();
    }

    @GetMapping("/{id}")
    public Classe obter(@PathVariable int id) throws Exception {
        return classeDAO.obter(id);
    }

    @PostMapping
    public Classe salvar(@RequestBody Classe classe) throws Exception {
        return classeDAO.incluir(classe);
    }

    @PutMapping("/{id}")
    public Classe atualizar(@PathVariable int id, @RequestBody Classe classe) throws Exception {
        classe.setId(id);
        return classeDAO.atualizar(classe);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        return classeDAO.excluir(id);
    }
}
