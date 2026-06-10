package com.rapassos.forca.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import com.rapassos.forca.model.Difficulty;

public class MenuView extends JFrame {

    public MenuView() {
        setupLayout();
    }

    private void setupLayout() {
        setTitle("Jogo da forca (Swing)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(243, 244, 246)); // Cinza claro moderno
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Título Principal ---
        JLabel lblTitle = new JLabel("JOGO DA FORCA", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(new Color(30, 41, 59)); // Slate 800
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitle, gbc);

        // --- Subtítulo ---
        JLabel lblSub = new JLabel("Selecione a dificuldade para iniciar", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setForeground(new Color(100, 116, 139)); // Slate 500
        gbc.gridy = 1;
        add(lblSub, gbc);

        // --- Label Dificuldade ---
        JLabel lblDiff = new JLabel("Dificuldade:");
        lblDiff.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblDiff.setForeground(new Color(30, 41, 59));
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(lblDiff, gbc);

        // --- ComboBox de Seleção ---
        JComboBox<Difficulty> cbDifficulty = new JComboBox<>(Difficulty.values());
        cbDifficulty.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cbDifficulty.setBackground(Color.WHITE);
        // Customiza a exibição para usar a descrição amigável do Enum
        cbDifficulty.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Difficulty) {
                    setText(((Difficulty) value).getDescription());
                }
                return this;
            }
        });
        gbc.gridx = 1;
        add(cbDifficulty, gbc);

        // --- Botão Iniciar ---
        JButton btnStart = new JButton("INICIAR JOGO");
        btnStart.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnStart.setBackground(new Color(37, 99, 235)); // Azul Royal Moderno
        btnStart.setForeground(Color.WHITE);
        btnStart.setFocusPainted(false);
        btnStart.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnStart.addActionListener(e -> {
            Difficulty selected = (Difficulty) cbDifficulty.getSelectedItem();
            // Abre o jogo passando a dificuldade escolhida
            GameView gameView = new GameView(selected);
            gameView.setVisible(true);
            this.dispose(); // Fecha o menu
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(btnStart, gbc);
    }
}
