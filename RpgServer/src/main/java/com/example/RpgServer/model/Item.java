package com.example.RpgServer.model;

import lombok.Data;

@Data
public abstract class Item {
    private int id;
    private String nome;
    private String descricao;
}
