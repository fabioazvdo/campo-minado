package cm;

import cm.modelo.Tabuleiro;
import cm.visual.Console;

public class Principal {
    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro(6, 6, 3);
        new Console(tabuleiro);
    }
}
