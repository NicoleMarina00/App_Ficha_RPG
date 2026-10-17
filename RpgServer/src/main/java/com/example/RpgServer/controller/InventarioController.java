package com.example.RpgServer.controller;

import com.example.RpgServer.dao.InventarioDAO;
import com.example.RpgServer.model.Inventario;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
public class InventarioController {
    private final InventarioDAO inventarioDAO;

    public InventarioController(InventarioDAO inventarioDAO) {
        this.inventarioDAO = inventarioDAO;
    }

    @GetMapping
    public List<Inventario> listarTodos() {
        return inventarioDAO.listar();
    }

    @GetMapping("/{id}")
    public Inventario obter(@PathVariable int id) {
        Inventario inventario = inventarioDAO.obter(id);
        if (inventario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro de inventário não encontrado.");
        }
        return inventario;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Inventario salvar(@RequestBody Inventario inventario) {
        return inventarioDAO.incluir(inventario);
    }

    @PutMapping("/{id}")
    public Inventario atualizar(@PathVariable int id, @RequestBody Inventario inventario) {
        inventario.setId(id);
        return inventarioDAO.atualizar(inventario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable int id) {
        inventarioDAO.excluir(id);
    }
}
