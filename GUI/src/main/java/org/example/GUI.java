package org.example;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;

import java.io.IOException;
import java.util.List;
//import java.util.regex.Pattern;

public class GUI {
    public static void main(String[] args) throws IOException {
        // setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        // create main panel for holding all sub-panels
        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new GridLayout(2));

        // create logo for top of window!
        String logoStr =
                " _________  ________  ________        ________  ________  ________  ________  ________     \n" +
                        "|\\___   ___\\\\   ____\\|\\   __  \\      |\\   __  \\|\\   __  \\|\\   __  \\|\\   __  \\|\\   ___ \\    \n" +
                        "\\|___ \\  \\_\\ \\  \\___|\\ \\  \\|\\  \\     \\ \\  \\|\\ /\\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\_|\\ \\   \n" +
                        "     \\ \\  \\ \\ \\  \\    \\ \\   ____\\     \\ \\   __  \\ \\  \\\\\\  \\ \\   __  \\ \\   _  _\\ \\  \\ \\\\ \\  \n" +
                        "      \\ \\  \\ \\ \\  \\____\\ \\  \\___|      \\ \\  \\|\\  \\ \\  \\\\\\  \\ \\  \\ \\  \\ \\  \\\\  \\\\ \\  \\_\\\\ \\ \n" +
                        "       \\ \\__\\ \\ \\_______\\ \\__\\          \\ \\_______\\ \\_______\\ \\__\\ \\__\\ \\__\\\\ _\\\\ \\_______\\\n" +
                        "        \\|__|  \\|_______|\\|__|           \\|_______|\\|_______|\\|__|\\|__|\\|__|\\|__|\\|_______|\n" +
                        "                                                                                           \n" +
                        "                                                                                           \n" +
                        "                                                                                           ";
        Label logoLabel = new Label(logoStr);

        // create user info panel to keep the logo art in its own row at the top
        Panel userLabelPanel = new Panel();
        userLabelPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        userLabelPanel.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER,
                GridLayout.Alignment.CENTER, true, false));
        // create labels to hold user info
        userLabelPanel.addComponent((new Label("User: ")));
        userLabelPanel.addComponent((new Label("Groups: ")));
        userLabelPanel.addComponent((new Label("Status: ")));
        //mainPanel.addComponent(1, userLabelPanel.withBorder(Borders.singleLine("Client Info")));


        // create panel to hold table with server commands/descriptions
        Panel cmdInfoPanel = new Panel();
        cmdInfoPanel.setLayoutManager(new GridLayout(1));

        // create server commands/descriptions table
        Table<String> cmdInfoTable = new Table<String>("Command", "Description");
        cmdInfoTable.getTableModel().addRow("%connect [address] [port]: ", "Connect to a different server");
        cmdInfoTable.getTableModel().addRow("%join [username]: ", "Join the group");
        cmdInfoTable.getTableModel().addRow("%post [subject] [content]: ", "Post a message");
        cmdInfoTable.getTableModel().addRow("%users: ", "Get the list of users in the group");
        cmdInfoTable.getTableModel().addRow("%message [message ID]: ", "Retrieve the content of a message");
        cmdInfoTable.getTableModel().addRow("%leave: ", "Leave the group");
        cmdInfoTable.getTableModel().addRow("%exit: ", "Exit the client program");

        // print selection when user chooses a row
        cmdInfoTable.setSelectAction(new Runnable() {
            @Override
            public void run() {
                List<String> data = cmdInfoTable.getTableModel().getRow(cmdInfoTable.getSelectedRow());
                for(int i = 0; i < data.size(); i++) {
                    System.out.println(data.get(i));
                }
            }
        });

        // add cmdInfoTable, label, and spacing to cmdInfoPanel
        cmdInfoPanel.addComponent(cmdInfoTable.withBorder(Borders.singleLine("Client Commands")));
        cmdInfoPanel.addComponent(new EmptySpace(new TerminalSize(0, 0)));

        // create panel to hold table with test info
        Panel testPanel = new Panel();
        testPanel.setLayoutManager(new GridLayout(1));

        // create test table
        Table<String> testTable = new Table<String>("test col1", "test col2");
        testTable.getTableModel().addRow("%connect [address] [port]: ", "Connect to a different server");
        testTable.getTableModel().addRow("%join [username]: ", "Join the group");
        testTable.getTableModel().addRow("%post [subject] [content]: ", "Post a message");

        // add testTable, label, and spacing to testPanel
        testPanel.addComponent(testTable.withBorder(Borders.singleLine("Test panel")));
        testPanel.addComponent(new EmptySpace(new TerminalSize(0, 0)));

        // add logo art label to main panel
        mainPanel.addComponent(0, logoLabel);
        // add user info panel to main panel
        mainPanel.addComponent(1, userLabelPanel.withBorder(Borders.singleLine("Client Info")));
        // add command info panel to main panel
        mainPanel.addComponent(2, cmdInfoPanel);
        // add test panel to main panel
        mainPanel.addComponent(3, testPanel);

        // create window to hold the main panel
        BasicWindow window = new BasicWindow();
        window.setComponent(mainPanel);

        // create gui and start gui
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindowAndWait(window);
    }
}
