package cm.modelo;

import cm.excecao.ExplosaoException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Tabuleiro {
    private int linhas;
    private int colunas;
    private int minas;
    private final List<Campo> campos = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        gerarCampos();
        associarVizinhos();
        sortearMinas();

    }

    public void abrir(int linha, int coluna){
        try {
            campos.parallelStream()
                    .filter(campo -> campo.getLinha() == linhas && campo.getColuna() == colunas)
                    .findFirst()
                    .ifPresent(Campo::abrir);
        } catch (ExplosaoException e){
            campos.forEach(campo -> campo.setAberto(true));
            throw e;
        }
    }

    public void alternarMarcacao(int linha, int coluna){
        campos.parallelStream()
                .filter(campo -> campo.getLinha() == linhas && campo.getColuna() == colunas)
                .findFirst()
                .ifPresent(Campo::alternarMarcacao);
    }

    private void gerarCampos() {
        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                campos.add(new Campo(linha, coluna));
            }
        }
    }

    private void associarVizinhos() {
        for (Campo c1 : campos){
            for (Campo c2 : campos){
                c1.adicionarVizinho(c2);
            }
        }
    }

    private void sortearMinas() {
        long minasArmadas = 0;
        Predicate<Campo> minado = Campo::isMinado;

        do {
            minasArmadas = campos.stream()
                    .filter(minado)
                    .count();
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();
        } while(minasArmadas < minas);
    }

    public boolean objetivoAlcancado(){
        return campos.stream().allMatch(Campo::objetivoAlcacado);
    }

    public void reiniciar(){
        campos.stream().forEach(Campo::reiniciar);
        sortearMinas();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (int l = 0; l < linhas; l++) {
            for (int c = 0; c < colunas; c++) {
                sb.append(" ");
                sb.append(campos.get(i));
                i++;
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
