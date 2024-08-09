import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.ArrayList;

public class Main {

    private static int varsityTotal;
    private static int jvTotal;

    private static JPanel mainPanel;
    private static CardLayout cardLayout;

    private static int questionCounter = 0;
    private static int totalQuestions;

    private static Player[] jvPlayers = {
            new Player("JVPlayer1", "jv"),
            new Player("JVPlayer2", "jv"),
            new Player("JVPlayer3", "jv"),
            new Player("JVPlayer4", "jv")
    };

    private static Player[] varsityPlayers = {
            new Player("VarsityPlayer1", "varsity"),
            new Player("VarsityPlayer2", "varsity"),
            new Player("VarsityPlayer3", "varsity"),
            new Player("VarsityPlayer4", "varsity")
    };

    public static Team jvTeam = new Team("JV", jvPlayers);
    public static Team varsityTeam = new Team("Varsity", varsityPlayers);

    private static TossupAPI tossupAPI = new TossupAPI("http://qbreader.org/api/random-tossup");
    private static BonusAPI bonusAPI = new BonusAPI("http://qbreader.org/api/random-bonus");

    private static String tossupQuestion;
    private static String tossupAnswer;

    private static ArrayList<String> bonusQuestion;
    private static ArrayList<String> bonusAnswer;

    private static JLabel questionLabel;
    private static JLabel answerLabel;

    public static void main(String[] args) {
        UIManager.put("Label.font", new Font("Serif", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Serif", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Serif", Font.PLAIN, 14));

        JFrame frame = new JFrame("Questionnaire");
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createInitialScreen(frame), "InitialScreen");
        mainPanel.add(createQuestionTypeScreen(frame), "QuestionTypeScreen");
        mainPanel.add(createPlayerSelectionScreen(frame), "PlayerSelectionScreen");

        cardLayout.show(mainPanel, "InitialScreen");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JPanel createInitialScreen(JFrame frame) {
        jvTeam.readFromFile();
        varsityTeam.readFromFile();

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel label = new JLabel("Enter the number of questions:");
        label.setBounds(390, 250, 250, 50);
        panel.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(350, 300, 300, 50);
        panel.add(textField);

        JButton button = new JButton("Submit");
        button.setBounds(350, 350, 300, 50);
        panel.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = textField.getText();
                try {
                    totalQuestions = Integer.parseInt(input);
                    cardLayout.show(mainPanel, "QuestionTypeScreen");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
                }
            }
        });

        return panel;
    }

    private static JPanel createQuestionTypeScreen(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton tossupButton = new JButton("TOSSUP");
        tossupButton.setBounds(430, 300, 120, 50);
        panel.add(tossupButton);

        JButton bonusButton = new JButton("BONUS");
        bonusButton.setBounds(430, 350, 120, 50);
        panel.add(bonusButton);

        tossupButton.addActionListener(e -> {
            tossupAPI.refresh();
            updateQuestionLabels("Tossup");
            cardLayout.show(mainPanel, "PlayerSelectionScreen");
        });

        bonusButton.addActionListener(e -> {
            bonusAPI.refresh();
            updateQuestionLabels("Bonus");
            cardLayout.show(mainPanel, "PlayerSelectionScreen");
        });

        return panel;
    }

    private static JPanel createPlayerSelectionScreen(JFrame frame) {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        questionLabel = new JLabel("", SwingConstants.CENTER);
        answerLabel = new JLabel("", SwingConstants.CENTER);

        questionPanel.add(questionLabel);
        questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        questionPanel.add(answerLabel);

        panel.add(questionPanel, BorderLayout.NORTH);

        JPanel jvPanel = new JPanel(new GridLayout(jvTeam.size(), 1));
        JPanel varsityPanel = new JPanel(new GridLayout(varsityTeam.size(), 1));

        for (Player player : jvTeam.getPlayers()) {
            JButton playerButton = createPlayerButton(player, frame);
            jvPanel.add(playerButton);
        }

        for (Player player : varsityTeam.getPlayers()) {
            JButton playerButton = createPlayerButton(player, frame);
            varsityPanel.add(playerButton);
        }

        JPanel teamsPanel = new JPanel(new GridLayout(1, 2));
        teamsPanel.add(new JScrollPane(jvPanel));
        teamsPanel.add(new JScrollPane(varsityPanel));

        panel.add(teamsPanel, BorderLayout.CENTER);

        return panel;
    }

    private static void updateQuestionLabels(String type) {
        try {
            if (type.equals("Tossup")) {
                tossupQuestion = tossupAPI.getQuestion();
                tossupAnswer = tossupAPI.getAnswer();
                questionLabel.setText(
                        "<html><H2><center>Question:</center></H2><p><center>" + tossupQuestion
                                + "</center></p></html>");
                answerLabel.setText(
                        "<html><H2><center>Answer:</center></H2><p><center>" + tossupAnswer + "</center></p></html>");
            } else if (type.equals("Bonus")) {
                String bonusFormatted = "";

                bonusAnswer = bonusAPI.getBonusAnswersArrayList();
                bonusQuestion = bonusAPI.getBonusQuestionsArrayList();

                for (int i = 0; i < bonusQuestion.size(); i++) {
                    bonusFormatted += "<H4><center>Question " + (i + 1) + ":</center></H4><p><center>"
                            + bonusQuestion.get(i) + "</center></p>";
                }

                questionLabel.setText(
                        "<html><H2><center>Lead in: " + bonusAPI.getLeadIn() + "</center></H2><p><center>"
                                + "</center></p>" + bonusFormatted + "</html>");

                bonusFormatted = "";
                for (int i = 0; i < bonusAnswer.size(); i++) {
                    bonusFormatted += "<H4><center>Answer " + (i + 1) + ":</center></H4><p><center>"
                            + bonusAnswer.get(i)
                            + "</center></p>";
                }

                answerLabel.setText(
                        "<html><H2><center>Answer:</center></H2><p><center>" + bonusFormatted + "</center></p></html>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JButton createPlayerButton(Player player, JFrame frame) {
        JButton playerButton = new JButton(
                "<html>" + player.getName() + "<br>Points: " + player.getPoints() + "</html>");
        playerButton.addActionListener(e -> handlePlayerSelection(player, frame, playerButton));
        return playerButton;
    }

    private static void handlePlayerSelection(Player player, JFrame frame, JButton playerButton) {
        player.addPoints(10);

        if (player.getType().equals("jv")) {
            jvTotal += 10;
        } else {
            varsityTotal += 10;
        }

        playerButton.setText("<html>" + player.getName() + "<br>Points: " + player.getPoints() + "</html>");
        questionCounter++;

        if (questionCounter >= totalQuestions) {
            displayResults(frame);
        } else {
            cardLayout.show(mainPanel, "QuestionTypeScreen");
        }
    }

    private static void displayResults(JFrame frame) {
        String winner = (jvTotal > varsityTotal) ? "JV" : ((varsityTotal > jvTotal) ? "Varsity" : "It's a tie");

        Player[] allPlayers = new Player[jvTeam.size() + varsityTeam.size()];
        System.arraycopy(jvTeam.getPlayers(), 0, allPlayers, 0, jvTeam.size());
        System.arraycopy(varsityTeam.getPlayers(), 0, allPlayers, jvTeam.size(), varsityTeam.size());
        Arrays.sort(allPlayers, (p1, p2) -> Integer.compare(p2.getPoints(), p1.getPoints()));

        StringBuilder results = new StringBuilder("Results:\n");
        results.append("Winner: ").append(winner).append("\n\nPlayer Rankings:\n");
        for (Player player : allPlayers) {
            results.append(player).append("\n");
        }

        JOptionPane.showMessageDialog(frame, results.toString());

        jvTeam.store();
        varsityTeam.store();

        System.exit(0);

    }
}
