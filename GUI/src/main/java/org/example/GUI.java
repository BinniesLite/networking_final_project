package org.example;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
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

        // create panel to hold table with send command options
        Panel sendCmdPanel = new Panel();
        sendCmdPanel.setLayoutManager(new GridLayout(1));

        // create two subpanels for the send commands panel: one for regular, one for group commands
        Panel regCmds = new Panel();
        regCmds.setLayoutManager(new GridLayout(9));
        sendCmdPanel.addComponent(regCmds.withBorder(Borders.singleLine("Solo Commands")));
        Panel groupCmds = new Panel();
        groupCmds.setLayoutManager(new GridLayout(9));
        sendCmdPanel.addComponent(groupCmds.withBorder(Borders.singleLine("Group Commands")));

        // add %connect [address] [port]: Connect to a different server
        Label connectLabel = new Label("Connect to a different server: %connect").addTo(regCmds); 
        final TextBox connectAddr = new TextBox().setValidationPattern(Pattern.compile("[0-9]*")).addTo(regCmds);
        Label connectAddrLabel = new Label("(address)").addTo(regCmds);
        final TextBox connectPort = new TextBox().setValidationPattern(Pattern.compile("[0-9]*")).addTo(regCmds);
        Label connectPortLabel = new Label("(port)").addTo(regCmds);
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button sendConnectButton = new Button("SEND").addTo(regCmds);

        // add %join [username]: Join with the username
        Label joinLabel = new Label("Join with the username: %join").addTo(regCmds); 
        final TextBox userName = new TextBox().setValidationPattern(Pattern.compile("[a-z]*")).addTo(regCmds);
        Label usernameLabel = new Label("(username)").addTo(regCmds);
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button joinButton = new Button("SEND").addTo(regCmds);

        // add %post [subject] [content]: Post a message
        Label postLabel = new Label("Post a message: %post").addTo(regCmds); 
        final TextBox subject = new TextBox().setValidationPattern(Pattern.compile("[a-z]*")).addTo(regCmds);
        Label subjectLabel = new Label("(subject)").addTo(regCmds);
        final TextBox content = new TextBox().setValidationPattern(Pattern.compile("[a-z]*")).addTo(regCmds);
        Label contentLabel = new Label("(content)").addTo(regCmds);
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button postButton = new Button("SEND").addTo(regCmds);

        // add %users: Get the list of users in the group
        Label usersLabel = new Label("Get the list of users in the group: %users").addTo(regCmds); 
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button usersButton = new Button("SEND").addTo(regCmds);

        // add %message [message ID]: Retrieve the content of a message
        Label messageLabel = new Label("Retrieve the content of a message: %message").addTo(regCmds); 
        final TextBox message = new TextBox().setValidationPattern(Pattern.compile("[0-9]*")).addTo(regCmds);
        Label messageIdLabel = new Label("(message ID)").addTo(regCmds);
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button messageButton = new Button("SEND").addTo(regCmds);

        // add %leave: Leave the group
        Label leaveLabel = new Label("Leave the group: %leave").addTo(regCmds); 
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button leaveButton = new Button("SEND").addTo(regCmds);

        // add %exit: Exit the client program
        Label exitLabel = new Label("Exit the client program: %exit").addTo(regCmds); 
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        regCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button exitButton = new Button("SEND").addTo(regCmds);

        // add %groups: Show all groups available
        Label groupsLabel = new Label("Show all groups availables: %groups").addTo(groupCmds); 
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button groupsButton = new Button("SEND").addTo(groupCmds);

        // add %groupsjoin [group ID]: Join the group with ID
        Label groupsjoinLabel = new Label("Join the group with id: %groupsjoin").addTo(groupCmds); 
        final TextBox groupsjoinIdTextbox = new TextBox().setValidationPattern(Pattern.compile("[0-9]*")).addTo(groupCmds);
        Label groupIdLabel = new Label("(group ID)").addTo(groupCmds);
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button groupsjoinButton = new Button("SEND").addTo(groupCmds);

        // add %grouppost [group ID] [subject] [content]: Post to the group with that id
        Label groupPostLabel = new Label("Post to the group with ID: %grouppost").addTo(groupCmds); 
        final TextBox grouppostIdTextbox = new TextBox().setValidationPattern(Pattern.compile("[0-9]*")).addTo(groupCmds);
        Label grouppostIdLabel = new Label("(group ID)").addTo(groupCmds);
        final TextBox grouppostSubjectTextbox = new TextBox().setValidationPattern(Pattern.compile("[a-z],[0-9]*")).addTo(groupCmds);
        Label grouppostSubjectLabel = new Label("(subject)").addTo(groupCmds);
        final TextBox grouppostContentTextbox = new TextBox().setValidationPattern(Pattern.compile("[a-z],[0-9]*")).addTo(groupCmds);
        Label grouppostContentLabel = new Label("(content)").addTo(groupCmds);
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button grouppostButton = new Button("SEND").addTo(groupCmds);

        // add %groupusers [group ID]: Retrieve a list of users in the given group
        Label groupusersLabel = new Label("Retrieve list of users in group with ID: %groupusers").addTo(groupCmds); 
        final TextBox groupusersIdTextbox = new TextBox().setValidationPattern(Pattern.compile("[0-9]*")).addTo(groupCmds);
        Label groupusersIdLabel = new Label("(group ID)").addTo(groupCmds);
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button groupusersButton = new Button("SEND").addTo(groupCmds);

        // add %groupmessage [group ID] [message ID]: retrieve the content of an earlier post if you belong to that group
        Label groupmsgLabel = new Label("Retrieve content of an earlier post if you belong to that group: %groupmessage").addTo(groupCmds); 
        final TextBox groupmsgGroupIdTextbox = new TextBox().setValidationPattern(Pattern.compile("[0-9]*")).addTo(groupCmds);
        Label groupmsgGroupIdLabel = new Label("(group ID)").addTo(groupCmds);
        final TextBox groupmsgMsgIdTextbox = new TextBox().setValidationPattern(Pattern.compile("[a-z],[0-9]*")).addTo(groupCmds);
        Label groupmsgMsgIdLabel = new Label("(message ID)").addTo(groupCmds);
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button groupmsgButton = new Button("SEND").addTo(groupCmds);

        // add %groupleave [group ID]: Leave the current group if client is in that group
        Label groupleaveLabel = new Label("Leave the current group if client is in that group: %groupleave").addTo(groupCmds); 
        final TextBox groupleaveIdTextbox = new TextBox().setValidationPattern(Pattern.compile("[0-9]*")).addTo(groupCmds);
        Label groupleaveIdLabel = new Label("(group ID)").addTo(groupCmds);
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        groupCmds.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        Button groupleaveButton = new Button("SEND").addTo(groupCmds);

        // create panel to display output to client
        Panel outputPanel = new Panel();
        outputPanel.setLayoutManager(new GridLayout(1));
        // add display box to output panel

        // add logo art label to main panel
        mainPanel.addComponent(0, logoLabel);
        // add user info panel to main panel
        mainPanel.addComponent(1, userLabelPanel.withBorder(Borders.singleLine("Client Info")));
        // add send commands panel to main panel
        mainPanel.addComponent(2, sendCmdPanel.withBorder(Borders.singleLine("Send Commands")));
        // add output panel to main panel
        mainPanel.addComponent(3, outputPanel.withBorder(Borders.singleLine("Output")));

        // create window to hold the main panel
        BasicWindow window = new BasicWindow();
        window.setComponent(mainPanel);

        // create gui and start gui
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindowAndWait(window);
    }
}
