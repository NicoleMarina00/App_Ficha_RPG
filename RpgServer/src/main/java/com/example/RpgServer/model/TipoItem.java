package com.example.RpgServer.model;

public enum TipoItem {
    ARMA("ARMA"),
    ARMADURA("ARMADURA"),
    ESCUDO("ESCUDO");

    private final String tabela;

    TipoItem(String tabela) {
        this.tabela = tabela;
    }

    public String getTabela() {
        return tabela;
    }
}
