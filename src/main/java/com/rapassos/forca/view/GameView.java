package com.rapassos.forca.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import com.rapassos.forca.controller.GameController;
import com.rapassos.forca.model.Difficulty;
import com.rapassos.forca.model.GameState;

public class GameView extends JFrame {
    private final GameController controller;
    private final Difficulty initialDifficulty;

    private JLabel lblWord;
    private JLabel lblErrors;
    private JLabel lblDifficulty;
    private JPanel pnlKeyboard;
    private final List<JButton> keyboardButtons = new ArrayList<>();

    // 🌟 Construtor agora exige a dificuldade escolhida no menu
    public GameView(Difficulty difficulty) {
        this.controller = new GameController();
        this.initialDifficulty = difficulty;
        setupLayout();
        initGame();
    }

    private void setupLayout() {
        setTitle("Jogo da Forca Sênior");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 550);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(249, 250, 251)); // Fundo clean (Gray 50)
        setLayout(new BorderLayout(15, 15));

        // --- Painel Superior (Status Estilizado) ---
        JPanel pnlStatus = new JPanel(new GridLayout(1, 2, 10, 10));
        pnlStatus.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        pnlStatus.setBackground(new Color(30, 41, 59)); // Slate 800 (Contraste escuro no topo)

        lblDifficulty = new JLabel("Dificuldade: ");
        lblDifficulty.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblDifficulty.setForeground(new Color(241, 245, 249));

        lblErrors = new JLabel("Erros: 0 / 0", SwingConstants.RIGHT);
        lblErrors.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblErrors.setForeground(new Color(248, 113, 113)); // Vermelho suave pastel

        pnlStatus.add(lblDifficulty);
        pnlStatus.add(lblErrors);
        add(pnlStatus, BorderLayout.NORTH);

        // --- Painel Central (Palavra Secreta) ---
        JPanel pnlCentral = new JPanel(new GridBagLayout());
        pnlCentral.setBackground(new Color(249, 250, 251));

        lblWord = new JLabel("_ _ _ _ _ _", SwingConstants.CENTER);
        lblWord.setFont(new Font("Monospaced", Font.BOLD, 40));
        lblWord.setForeground(new Color(17, 24, 39)); // Quase preto (Gray 900)
        pnlCentral.add(lblWord);
        add(pnlCentral, BorderLayout.CENTER);

        // --- Painel Inferior (Teclado Estilizado) ---
        pnlKeyboard = new JPanel(new GridLayout(3, 9, 6, 6));
        pnlKeyboard.setBackground(new Color(243, 244, 246)); // Gray 100
        pnlKeyboard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        generateKeyboard();
        add(pnlKeyboard, BorderLayout.SOUTH);
    }

    private void generateKeyboard() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (char letter : alphabet.toCharArray()) {
            JButton btn = new JButton(String.valueOf(letter));
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(55, 65, 81)); // Gray 700
            btn.setBorder(new LineBorder(new Color(209, 213, 219), 1, true)); // Borda arredondada
                                                                              // leve
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Efeito hover visual indireto mudando o comportamento ao clicar
            btn.addActionListener(e -> handleGuess(letter, btn));

            keyboardButtons.add(btn);
            pnlKeyboard.add(btn);
        }
    }

    private void initGame() {
        controller.startNewGame(initialDifficulty);
        resetKeyboard();
        updateScreen();
    }

    private void handleGuess(char letter, JButton pressedButton) {
        pressedButton.setEnabled(false);
        pressedButton.setBackground(new Color(229, 231, 235)); // Gray 200 quando desativado

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

        StringBuilder formattedWord = new StringBuilder();
        for (char c : state.getHiddenWord().toCharArray()) {
            formattedWord.append(c).append(" ");
        }
        lblWord.setText(formattedWord.toString().toUpperCase());
    }

    private void endRound(GameState state) {
        String title = state.isWon() ? "🎉 VITÓRIA!" : "💀 GAME OVER";
        String message =
                String.format("A palavra era: %s\n\n📚 Definição:\n%s\n\nDeseja jogar novamente?",
                        state.getTargetWord().getText().toUpperCase(),
                        state.getTargetWord().getDefinition());

        int option = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            // Se quiser jogar de novo, volta para o Menu Inicial!
            SwingUtilities.invokeLater(() -> {
                new MenuView().setVisible(true);
            });
            this.dispose();
        } else {
            System.exit(0);
        }
    }

    private void resetKeyboard() {
        for (JButton btn : keyboardButtons) {
            btn.setEnabled(true);
            btn.setBackground(Color.WHITE);
        }
    }
}
