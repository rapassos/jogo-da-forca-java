package com.rapassos.forca.main;

import java.util.Scanner;
import com.rapassos.forca.controller.GameController;
import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.GameState;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameController controller = new GameController();

        System.out.println("=========================================");
        System.out.println("      BEM-VINDO AO JOGO DA FORCA SÊNIOR   ");
        System.out.println("=========================================");
        System.out.println("Escolha a dificuldade:");
        System.out.println("1 - Fácil");
        System.out.println("2 - Médio");
        System.out.println("3 - Difícil");
        System.out.println("4 - Extremo");
        System.out.print("Opção: ");

        int option = scanner.nextInt();
        Difficulty selectedDifficulty = switch (option) {
            case 2 -> Difficulty.MEDIO;
            case 3 -> Difficulty.DIFICIL;
            case 4 -> Difficulty.EXTREMO;
            default -> Difficulty.FACIL;
        };

        System.out.println("\n[INFO] Carregando palavra do dicionário, aguarde...");
        controller.startNewGame(selectedDifficulty);
        GameState state = controller.getCurrentState();

        System.out.println(
                "\nPartida Iniciada! Dificuldade: " + state.getDifficulty().getDescription());

        // Loop principal do jogo via console
        while (!state.isGameOver()) {
            System.out.println("\n-----------------------------------------");
            System.out.println("Palavra: " + state.getHiddenWord());
            System.out.println("Erros cometidos: " + state.getCurrentErrors() + " / "
                    + state.getDifficulty().getMaxErrors());
            System.out.println("Letras já tentadas: " + state.getGuessedLetters());
            System.out.print("Digite uma letra: ");

            String input = scanner.next();
            if (input.isBlank())
                continue;

            char guess = input.charAt(0);
            boolean hit = controller.makeGuess(guess);

            if (hit) {
                System.out.println(
                        " Boa! A letra '" + Character.toUpperCase(guess) + "' existe na palavra.");
            } else {
                System.out.println(" Errou! A palavra não tem a letra '"
                        + Character.toUpperCase(guess) + "'.");
            }
        }

        // Fim de jogo - Exibição dos resultados e da definição da palavra
        System.out.println("\n=========================================");
        if (state.isWon()) {
            System.out.println("🎉 PARABÉNS! Você venceu! 🎉");
        } else {
            System.out.println("💀 FIM DE JOGO! Você foi enforcado. 💀");
        }
        System.out.println("A palavra era: " + state.getTargetWord().getText());
        System.out.println("-----------------------------------------");
        System.out.println("📚 SIGNIFICACO/FONTE DE CONHECIMENTO:");
        System.out.println(state.getTargetWord().getDefinition());
        System.out.println("=========================================");

        scanner.close();
    }
}
