package com.example.RpgServer.model;

import lombok.Data;

@Data
public class Inventario {
    private int id;
    private int personagemId;
    private String personagemNome;
    private TipoItem tipoItem;
    private int itemId;
    private String nome;
    private int quantidade;
}
