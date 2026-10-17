package com.example.RpgServer.model;
import lombok.Data;

@Data
public class Personagem {
    private int id;
    private String nome;
    private int forca, inteligencia, destreza, sabedoria, carisma;
    private String racaJson, classeJson, armaJson, armaduraJson, escudoJson, divindadeJson, periciaJson, feiticoJson;
}
