package com.example.RpgServer.controller;

import com.example.RpgServer.dao.PersonagemDAO;
import com.example.RpgServer.model.Personagem;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/personagens")
public class PersonagemController {

    private final PersonagemDAO personagemDAO;

    public PersonagemController(PersonagemDAO personagemDAO) {
        this.personagemDAO = personagemDAO;
    }

    @GetMapping
    public List<Personagem> listarTodos() throws Exception {
        return personagemDAO.listar();
    }

    @GetMapping("/{id}")
    public Personagem obter(@PathVariable int id) throws Exception {
        return personagemDAO.obter(id);
    }

    @PostMapping
    public Personagem salvar(@RequestBody Personagem personagem) throws Exception {
        validarPontos(personagem);
        return personagemDAO.incluir(personagem);
    }

    @PutMapping("/{id}")
    public Personagem atualizar(@PathVariable int id, @RequestBody Personagem personagem) throws Exception {
        validarPontos(personagem);
        personagem.setId(id);
        return personagemDAO.atualizar(personagem);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        return personagemDAO.excluir(id);
    }

    private void validarPontos(Personagem personagem) {
        int[] atributos = {
                personagem.getForca(),
                personagem.getInteligencia(),
                personagem.getDestreza(),
                personagem.getSabedoria(),
                personagem.getCarisma()
        };
        int soma = 0;
        for (int atributo : atributos) {
            if (atributo < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada atributo precisa ser zero ou maior.");
            }
            soma += atributo;
        }
        if (soma > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A soma dos atributos não pode passar de 20 pontos.");
        }
    }
}
