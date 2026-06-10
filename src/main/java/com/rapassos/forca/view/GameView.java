package com.rapassos.forca.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import com.rapassos.forca.controller.GameController;
import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.GameState;

public class GameView extends JFrame {
    private final GameController controller;

    private JLabel lblWord;
    private JLabel lblErrors;
    private JLabel lblDifficulty;
    private JPanel pnlKeyboard;
    private final List<JButton> keyboardButtons = new ArrayList<>();

    public GameView() {
        this.controller = new GameController();
        setupLayout();
        initGame();
    }

    private void setupLayout() {
        setTitle("Jogo da Forca Sênior");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setLayout(new BorderLayout(10, 10));

        // --- Painel Superior (Status) ---
        JPanel pnlStatus = new JPanel(new GridLayout(1, 2, 10, 10));
        pnlStatus.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        pnlStatus.setBackground(new Color(240, 240, 240));

        lblDifficulty = new JLabel("Dificuldade: ");
        lblDifficulty.setFont(new Font("SansSerif", Font.BOLD, 14));

        lblErrors = new JLabel("Erros: 0 / 0", SwingConstants.RIGHT);
        lblErrors.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblErrors.setForeground(new Color(180, 40, 40));

        pnlStatus.add(lblDifficulty);
        pnlStatus.add(lblErrors);
        add(pnlStatus, BorderLayout.NORTH);

        // --- Painel Central (Palavra Oculta) ---
        JPanel pnlCentral = new JPanel(new GridBagLayout());
        lblWord = new JLabel("_ _ _ _ _ _", SwingConstants.CENTER);
        lblWord.setFont(new Font("Monospaced", Font.BOLD, 36)); // Mantido o estilo mono
        pnlCentral.add(lblWord);
        add(pnlCentral, BorderLayout.CENTER);

        // --- Painel Inferior (Teclado Virtual A-Z) ---
        pnlKeyboard = new JPanel(new GridLayout(3, 9, 5, 5));
        pnlKeyboard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        generateKeyboard();
        add(pnlKeyboard, BorderLayout.SOUTH);
    }

    private void generateKeyboard() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (char letter : alphabet.toCharArray()) {
            JButton btn = new JButton(String.valueOf(letter));
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setFocusPainted(false);

            btn.addActionListener(e -> handleGuess(letter, btn));

            keyboardButtons.add(btn);
            pnlKeyboard.add(btn);
        }
    }

    private void initGame() {
        // Por padrão, iniciaremos no MÉDIO. Depois podemos criar uma tela de menu!
        controller.startNewGame(Difficulty.MEDIO);
        resetKeyboard();
        updateScreen();
    }

    private void handleGuess(char letter, JButton pressedButton) {
        pressedButton.setEnabled(false); // Desativa o botão para evitar clique duplo

        boolean hit = controller.makeGuess(letter);
        updateScreen();

        GameState state = controller.getCurrentState();
        if (state.isGameOver()) {
            endRound(state);
        }
    }

    private void updateScreen() {
        GameState state = controller.getCurrentState();
        lblDifficulty.setText("Dificuldade: " + state.getDifficulty().getDescription());
        lblErrors.setText("Erros: " + state.getCurrentErrors() + " / "
                + state.getDifficulty().getMaxErrors());

        // Formata a palavra para o jogador com espaços entre os caracteres
        StringBuilder formattedWord = new StringBuilder();
        for (char c : state.getHiddenWord().toCharArray()) {
            formattedWord.append(c).append(" ");
        }
        lblWord.setText(formattedWord.toString().toUpperCase());
    }

    private void endRound(GameState state) {
        String title = state.isWon() ? "🎉 VITÓRIA! 🎉" : "💀 GAME OVER! 💀";
        String message =
                String.format("A palavra era: %s\n\n📚 Definição:\n%s\n\nDeseja jogar novamente?",
                        state.getTargetWord().getText().toUpperCase(),
                        state.getTargetWord().getDefinition());

        int option = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            initGame();
        } else {
            System.exit(0);
        }
    }

    private void resetKeyboard() {
        for (JButton btn : keyboardButtons) {
            btn.setEnabled(true);
        }
    }
}
