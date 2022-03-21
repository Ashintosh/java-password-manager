import com.google.gson.Gson;
import database.Database;
import database.Entry;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.List;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Scanner;

public class MainForm extends JFrame {
    private JPanel mainFormPanel;
    private JPanel createDatabaseMain;
    private JPanel loadDatabaseMain;
    private JPanel welcomeMain;
    private JPanel manageDatabaseMain;

    private JButton btnLoad_Main;
    private JButton btnCreate_Main;
    private JButton btnBrowse_Create;
    private JButton btnCreate_Create;
    private JButton btnBrowse_Load;
    private JButton btnLoad_Load;
    private JButton btnBack_Load;
    private JButton btnBack_Create;
    private JButton btnShow_Manage;
    private JButton btnCopy_Manage;
    private JButton btnAddGroup_Manage;
    private JButton btnAddEntry_Manage;
    private JButton btnDeleteGroup_Manage;
    private JButton btnDeleteEntry_Manage;
    private JButton btnSave_Manage;
    private JButton btnSaveGroupName_Manage;
    private JButton btnSaveEntryName_Manage;

    private JLabel frameLabel_Main;
    private JLabel directoryLabel_Create;
    private JLabel passwordLabel_Create;
    private JLabel confirmLabel_Create;
    private JLabel frameLabel_Create;
    private JLabel directoryLabel_Load;
    private JLabel passwordLabel_Load;
    private JLabel frameLabel_Load;
    private JLabel usernameLabel_Manage;
    private JLabel emailLabel_Manage;
    private JLabel labelPassword_Manage;
    private JLabel labelNotes_Manage;
    private JLabel groupsLabel_Manage;
    private JLabel entriesLabel_Manage;
    private JLabel urlLabel_Manage;
    private JLabel labelEntryName_Manage;
    private JLabel labelGroupName_Manage;

    private JTextField txtDirectory_Create;
    private JTextField txtDirectory_Load;
    private JTextField txtUsername_Manage;
    private JTextField txtURL_Manage;
    private JTextField txtEmail_Manage;
    private JTextField txtGroupName_Manage;
    private JTextField txtEntryName_Manage;

    private JPasswordField pwdConfirm_Create;
    private JPasswordField pwdPassword_Create;
    private JPasswordField pwdPassword_Load;
    private JPasswordField pwdPassword_Manage;

    private JList<String> lstGroups_Manage;
    private JList<String> lstEntries_Manage;

    private JTextArea txtNotes_Manage;

    private JScrollPane notesScrollPane_Manage;
    private JScrollPane groupsScrollPane_Manage;
    private JScrollPane entriesScrollPane_Manage;

    ///// Manage Panel Vars ///// <editor-fold desc="~Manage Panel Vars~"
    private static Database database;

    private boolean showPassword_Manage = false;

    private int currentGroup_Manage = 0;
    private int currentEntry_Manage = 0;

    private File fileDirectory;
    private String databasePassword;
    ///// End Manage Panel Vars ///// </editor-fold>

    public MainForm () {
        super("Secure Password Manager");
        setContentPane(mainFormPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(870, 500);
        setResizable(false);
        setVisible(true);
        createDatabaseMain.setVisible(false);
        loadDatabaseMain.setVisible(false);
        manageDatabaseMain.setVisible(false);
        welcomeMain.setVisible(true);

        initialize();
    }

    private void initialize () {
        ///// Action Listeners ///// <editor-fold desc="~Action Listeners~">
        ActionListener actionListener = e -> {
            /// Welcome Panel ///
            // btnLoad_Main // <editor-fold desc="~btnLoad_Main~">
            if (e.getSource() == btnLoad_Main) {
                loadDatabaseMain.setVisible(true);
                welcomeMain.setVisible(false);
            }
            // End btnLoad_Main // </editor-fold>
            // btnCreate_Main // <editor-fold desc="~btnCreate_Main~">
            else if (e.getSource() == btnCreate_Main) {
                createDatabaseMain.setVisible(true);
                welcomeMain.setVisible(false);
            }
            // End btnCreate_Main // </editor-fold>

            /// Create Panel ///
            // btnBack_Create // <editor-fold desc="~btnBack_Create~">
            else if (e.getSource() == btnBack_Create) {
                welcomeMain.setVisible(true);
                createDatabaseMain.setVisible(false);
            }
            // End btnBack_Create // </editor-fold>
            // btnBrowse_Create // <editor-fold desc="~btnBrowse_Create~">
            else if (e.getSource() == btnBrowse_Create) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Secure Database File", "sdb");
                fileChooser.setDialogTitle("Database File Directory");
                fileChooser.setFileFilter(filter);
                int response = fileChooser.showSaveDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    txtDirectory_Create.setText(fileChooser.getSelectedFile().getAbsolutePath() + ".sdb");
                }
            }
            // End btnBrowse_Create // </editor-fold>
            // btnCreate_Create // <editor-fold desc="~btnCreate_Create~">
            else if (e.getSource() == btnCreate_Create) {
                if (txtDirectory_Create.getText().length() < 1)
                    JOptionPane.showMessageDialog(createDatabaseMain, "Select database file directory.", "Warning", JOptionPane.WARNING_MESSAGE);
                else if (pwdPassword_Create.getPassword().length < 6)
                    JOptionPane.showMessageDialog(createDatabaseMain, "Password must be at least 6 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                else if (!Arrays.equals(pwdPassword_Create.getPassword(), pwdConfirm_Create.getPassword()))
                    JOptionPane.showMessageDialog(createDatabaseMain, "Passwords do not match.", "Warning", JOptionPane.WARNING_MESSAGE);
                else {
                    try {
                        StringBuilder password = new StringBuilder();
                        for (char c : pwdPassword_Create.getPassword()) { password.append(c); }
                        if (saveDatabase_Manage(new File(txtDirectory_Create.getText()), password.toString()))
                            JOptionPane.showMessageDialog(createDatabaseMain, "Database has been created.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        else
                            JOptionPane.showMessageDialog(createDatabaseMain, "Could not create database.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) { JOptionPane.showMessageDialog(createDatabaseMain, "Could not create database.", "Error", JOptionPane.ERROR_MESSAGE); }
                }
            }
            // End btnCreate_Create // </editor-fold>

            /// Load Panel ///
            // btnBack_Load // <editor-fold desc="~btnBack_Load~">
            else if (e.getSource() == btnBack_Load) {
                welcomeMain.setVisible(true);
                loadDatabaseMain.setVisible(false);
            }
            // End btnBack_Load // </editor-fold>
            // btnBrowse_Load // <editor-fold desc="~btnBrowse_Load~">
            else if (e.getSource() == btnBrowse_Load) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Secure Database File", "sdb");
                fileChooser.setDialogTitle("Database File Directory");
                fileChooser.setFileFilter(filter);

                int response = fileChooser.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    if (filePath.contains(".sdb"))
                        txtDirectory_Load.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    else txtDirectory_Load.setText(filePath + ".sdb");
                }
            }
            // End btnBrowse_Load // </editor-fold>
            // btnLoad_load <editor-fold desc="~btnLoad_Load~">
            else if (e.getSource() == btnLoad_Load) {
                try {
                    fileDirectory = new File(txtDirectory_Load.getText());
                    StringBuilder password = new StringBuilder();
                    for (char c : pwdPassword_Load.getPassword()) { password.append(c); }
                    databasePassword = password.toString();

                    loadDatabase_Manage(new File(txtDirectory_Load.getText()));
                    refreshGroups_Manage();
                    refreshSelectedGroup_Manage();
                    refreshEntries_Manage();
                    refreshSelectedEntry_Manage();

                    //setSize(860, 500);
                    loadDatabaseMain.setVisible(false);
                    manageDatabaseMain.setVisible(true);
                } catch (FileNotFoundException ex) { JOptionPane.showMessageDialog(loadDatabaseMain,"Could not open database.", "Error", JOptionPane.ERROR_MESSAGE); } catch (Exception ex) { JOptionPane.showMessageDialog(loadDatabaseMain,"Could not decrypt database.", "Error", JOptionPane.ERROR_MESSAGE); }
            }
            // End btnLoad_Load // </editor-fold>

            /// Manage Panel ///
            // btnCopy_Manage // <editor-fold desc="~btnCopy_Manage~">
            else if (e.getSource() == btnCopy_Manage) {
                StringBuilder password = new StringBuilder();
                for (char c : pwdPassword_Manage.getPassword()) { password.append(c); }
                final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(password.toString()), null);
            }
            // End btnCopy_Manage // </editor-fold>
            // btnShow_Manage // <editor-fold desc="~btnShow_Manage~">
            else if (e.getSource() == btnShow_Manage) {
                Icon icon;
                if (showPassword_Manage) {
                    icon = new ImageIcon("resources/eye-unchecked-30.png");
                    pwdPassword_Manage.setEchoChar('•');
                } else {
                    icon = new ImageIcon("resources/eye-checked-30.png");
                    pwdPassword_Manage.setEchoChar((char)0);
                }
                btnShow_Manage.setIcon(icon);
                showPassword_Manage = !showPassword_Manage;
            }
            // End // </editor-fold>
            // btnSave_Manage // <editor-fold desc="~btnSave_Manage~">
            else if (e.getSource() == btnSave_Manage) {
                try {
                    StringBuilder passwordString = new StringBuilder();
                    for (char c : pwdPassword_Manage.getPassword()) {
                        passwordString.append(c);
                    }
                    database.addEntry(new Entry(database.getGroups().get(currentGroup_Manage).get(currentEntry_Manage).getName(), txtURL_Manage.getText(), txtUsername_Manage.getText(), passwordString.toString(), txtEmail_Manage.getText(), txtNotes_Manage.getText()), currentGroup_Manage, currentEntry_Manage);

                    if (saveDatabase_Manage(fileDirectory, database)) {
                        refreshEntries_Manage();
                        refreshSelectedEntry_Manage();

                        JOptionPane.showMessageDialog(createDatabaseMain, "Entry has been saved.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else
                        JOptionPane.showMessageDialog(createDatabaseMain, "Could not save entry.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) { JOptionPane.showMessageDialog(createDatabaseMain, "Could not save entry.", "Error", JOptionPane.ERROR_MESSAGE); }
            }
            // End btnSave_Manage // </editor-fold>
            // btnAddEntry_Manage // <editor-fold desc="~btnAddEntry_Manage~">
            else if (e.getSource() == btnAddEntry_Manage) {
                try {
                    Entry newEntry = new Entry("New Entry", "", "", "", "", "");
                    database.addEntry(newEntry, currentGroup_Manage);

                    saveDatabase_Manage(fileDirectory, database);
                    loadDatabase_Manage(fileDirectory);

                    refreshGroups_Manage();
                    refreshSelectedGroup_Manage();
                    refreshEntries_Manage();
                    refreshSelectedEntry_Manage();
                } catch (Exception ex) { JOptionPane.showMessageDialog(createDatabaseMain, "Could not add entry.", "Error", JOptionPane.ERROR_MESSAGE); }
            }
            // End btnAddEntry_Manage // </editor-fold>
            // btnAddGroup_Manage // <editor-fold desc="~btnAddGroup_Manage~">
            else if (e.getSource() == btnAddGroup_Manage) {
                try {
                    database.addGroup();

                    saveDatabase_Manage(fileDirectory, database);
                    loadDatabase_Manage(fileDirectory);

                    refreshGroups_Manage();
                    refreshSelectedGroup_Manage();
                    refreshEntries_Manage();
                    refreshSelectedEntry_Manage();
                } catch (Exception ex) { JOptionPane.showMessageDialog(createDatabaseMain, "Could not add group.", "Error", JOptionPane.ERROR_MESSAGE); }
            }
            // End btnAddGroup_Manage // </editor-fold>
            // btnDeleteEntry_Manage // <editor-fold desc="~btnDeleteEntry_Manage~">
            else if (e.getSource() == btnDeleteEntry_Manage) {
                int result = JOptionPane.showConfirmDialog(mainFormPanel, "Are you sure you want to delete this entry?", "Confirm Deletion.",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        database.removeEntry(currentGroup_Manage, lstEntries_Manage.getSelectedIndex());
                        currentEntry_Manage -= currentEntry_Manage;

                        saveDatabase_Manage(fileDirectory, database);
                        loadDatabase_Manage(fileDirectory);

                        refreshGroups_Manage();
                        refreshSelectedGroup_Manage();
                        refreshEntries_Manage();
                        refreshSelectedEntry_Manage();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(createDatabaseMain, "Could not delete entry.", "Error", JOptionPane.ERROR_MESSAGE); }
                }
            }
            // End btnDeleteEntry_Manage // </editor-fold>
            // btnDeleteGroup_Manage // <editor-fold desc="~btnDeleteGroup_Manage~">
            else if (e.getSource() == btnDeleteGroup_Manage) {
                int result = JOptionPane.showConfirmDialog(mainFormPanel, "Are you sure you want to delete this group?", "Confirm Deletion.",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        database.removeGroup(currentGroup_Manage);
                        currentGroup_Manage -= currentGroup_Manage;

                        saveDatabase_Manage(fileDirectory, database);
                        loadDatabase_Manage(fileDirectory);

                        refreshGroups_Manage();
                        refreshSelectedGroup_Manage();
                        refreshEntries_Manage();
                        refreshSelectedEntry_Manage();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(createDatabaseMain, "Could not delete group.", "Error", JOptionPane.ERROR_MESSAGE); }
                }
            }
            // End btnDeleteEntry_Manage // </editor-fold>
            // btnSaveEntryName_Manage // <editor-fold desc="~btnSaveEntryName_Manage~"?
            else if (e.getSource() == btnSaveEntryName_Manage) {
                try {
                    Entry newEntry = new Entry(
                            txtEntryName_Manage.getText(),
                            database.getEntry(currentGroup_Manage, currentEntry_Manage).getUrl(),
                            database.getEntry(currentGroup_Manage, currentEntry_Manage).getUsername(),
                            database.getEntry(currentGroup_Manage, currentEntry_Manage).getPassword(),
                            database.getEntry(currentGroup_Manage, currentEntry_Manage).getEmail(),
                            database.getEntry(currentGroup_Manage, currentEntry_Manage).getNotes()
                    );
                    database.addEntry(newEntry, currentGroup_Manage, currentEntry_Manage);

                    saveDatabase_Manage(fileDirectory, database);
                    loadDatabase_Manage(fileDirectory);

                    refreshGroups_Manage();
                    refreshSelectedGroup_Manage();
                    refreshEntries_Manage();
                    refreshSelectedEntry_Manage();
                } catch (Exception ex) { JOptionPane.showMessageDialog(createDatabaseMain, "Could not save entry name.", "Error", JOptionPane.ERROR_MESSAGE); }
            }
            // End btnSaveEntryName_Manage // </editor-fold>
            // btnSaveGroupName_Manage // <editor-fold desc="~btnSaveGroupName_Manage~"?
            else if (e.getSource() == btnSaveGroupName_Manage) {
                try {
                    database.setGroupNamesIndex(currentGroup_Manage, txtGroupName_Manage.getText());

                    saveDatabase_Manage(fileDirectory, database);
                    loadDatabase_Manage(fileDirectory);

                    refreshGroups_Manage();
                    refreshSelectedGroup_Manage();
                    refreshEntries_Manage();
                    refreshSelectedEntry_Manage();
                } catch (Exception ex) { JOptionPane.showMessageDialog(createDatabaseMain, "Could not save group name. " + ex, "Error", JOptionPane.ERROR_MESSAGE); }
            }
            // End btnSaveGroupName_Manage // </editor-fold>
        };

        // Welcome Panel //
        btnLoad_Main.addActionListener(actionListener);
        btnCreate_Main.addActionListener(actionListener);

        // Create Panel //
        btnBack_Create.addActionListener(actionListener);
        btnBrowse_Create.addActionListener(actionListener);
        btnCreate_Create.addActionListener(actionListener);

        // Load Panel
        btnBack_Load.addActionListener(actionListener);
        btnBrowse_Load.addActionListener(actionListener);
        btnLoad_Load.addActionListener(actionListener);

        // Manage Panel
        btnCopy_Manage.addActionListener(actionListener);
        btnShow_Manage.addActionListener(actionListener);
        btnSave_Manage.addActionListener(actionListener);
        btnAddEntry_Manage.addActionListener(actionListener);
        btnAddGroup_Manage.addActionListener(actionListener);
        btnDeleteEntry_Manage.addActionListener(actionListener);
        btnDeleteGroup_Manage.addActionListener(actionListener);
        btnSaveEntryName_Manage.addActionListener(actionListener);
        btnSaveGroupName_Manage.addActionListener(actionListener);
        ///// End Action Listeners ///// </editor-fold>

        // List Selection Manager // <editor-fold desc="~List Selection Manager~">
        ListSelectionListener listSelectionListener = e -> {
            // lstGroups_Manage // <editor-fold desc="~lstGroups_Manage~">
            if (e.getSource() == lstGroups_Manage) {
                if (lstGroups_Manage.getSelectedIndex() >= 0) {
                    currentGroup_Manage = lstGroups_Manage.getSelectedIndex();
                    refreshSelectedGroup_Manage();
                    refreshEntries_Manage();
                }
            }
            // End lstGroups_Manage // </editor-fold>
            // lstEntries_Manage // <editor-fold desc="~lstEntries_Manage~">
            else if (e.getSource() == lstEntries_Manage) {
                if (lstEntries_Manage.getSelectedIndex() >= 0) {
                    currentEntry_Manage = lstEntries_Manage.getSelectedIndex();
                    refreshSelectedEntry_Manage();
                }
            }
            // End lstEntries_Manage // </editor-fold>
        };
        lstGroups_Manage.addListSelectionListener(listSelectionListener);
        lstEntries_Manage.addListSelectionListener(listSelectionListener);
        // End List Selection Manager // </editor-fold>

        ///// Mouse Events ///// <editor-fold desc="~Mouse Events~">
        Color defaultButtonColor = new Color(77, 81, 83);
        Color hoveredButtonColor = new Color(87, 91, 93);

        // Manage Panel //
        Color defaultManageButtonColor = new Color(30, 30, 30);
        Color hoveredManageButtonColor = new Color(28, 28, 28);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (
                        e.getComponent() == btnAddGroup_Manage    ||
                        e.getComponent() == btnAddEntry_Manage    ||
                        e.getComponent() == btnDeleteGroup_Manage ||
                        e.getComponent() == btnDeleteEntry_Manage
                ) e.getComponent().setBackground(hoveredManageButtonColor);
                else e.getComponent().setBackground(hoveredButtonColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (
                        e.getComponent() == btnAddGroup_Manage    ||
                        e.getComponent() == btnAddEntry_Manage    ||
                        e.getComponent() == btnDeleteGroup_Manage ||
                        e.getComponent() == btnDeleteEntry_Manage
                ) e.getComponent().setBackground(defaultManageButtonColor);
                else e.getComponent().setBackground(defaultButtonColor);
            }
        };

        // Main Panel //
        btnLoad_Main.addMouseListener(mouseAdapter);
        btnCreate_Main.addMouseListener(mouseAdapter);

        // Create Panel //
        btnBrowse_Create.addMouseListener(mouseAdapter);
        btnCreate_Create.addMouseListener(mouseAdapter);

        // Load Panel //
        btnBrowse_Load.addMouseListener(mouseAdapter);
        btnLoad_Load.addMouseListener(mouseAdapter);

        // Manage Panel //
        btnSave_Manage.addMouseListener(mouseAdapter);
        btnAddGroup_Manage.addMouseListener(mouseAdapter);
        btnAddEntry_Manage.addMouseListener(mouseAdapter);
        btnDeleteGroup_Manage.addMouseListener(mouseAdapter);
        btnDeleteEntry_Manage.addMouseListener(mouseAdapter);
        btnSaveGroupName_Manage.addMouseListener(mouseAdapter);
        btnSaveEntryName_Manage.addMouseListener(mouseAdapter);
        ///// End Mouse Events ///// </editor-fold>

        ///// Set Borders ///// <editor-fold desc="~Set Borders~">
        /// Create Panel ///
        txtDirectory_Create.setBorder(BorderFactory.createEtchedBorder());
        pwdPassword_Create.setBorder(BorderFactory.createEtchedBorder());
        pwdConfirm_Create.setBorder(BorderFactory.createEtchedBorder());

        /// Load Panel ///
        txtDirectory_Load.setBorder(BorderFactory.createEtchedBorder());
        pwdPassword_Load.setBorder(BorderFactory.createEtchedBorder());

        /// Manage Panel //
        txtEntryName_Manage.setBorder(BorderFactory.createEtchedBorder());
        txtUsername_Manage.setBorder(BorderFactory.createEtchedBorder());
        txtEmail_Manage.setBorder(BorderFactory.createEtchedBorder());
        pwdPassword_Manage.setBorder(BorderFactory.createEtchedBorder());
        txtURL_Manage.setBorder(BorderFactory.createEtchedBorder());
        txtNotes_Manage.setBorder(BorderFactory.createEtchedBorder());
        txtGroupName_Manage.setBorder(BorderFactory.createEtchedBorder());
        lstGroups_Manage.setBorder(BorderFactory.createEtchedBorder());
        lstEntries_Manage.setBorder(BorderFactory.createEtchedBorder());
        notesScrollPane_Manage.setBorder(BorderFactory.createEtchedBorder());
        groupsScrollPane_Manage.setBorder(BorderFactory.createEtchedBorder());
        notesScrollPane_Manage.setBorder(BorderFactory.createEtchedBorder());

        ///// End Set Borders ///// </editor-fold>
    }

    ///// refreshGroups_Manage() ///// <editor-fold desc="~refreshGroups_Manage()~">
    private void refreshGroups_Manage () {
        List<List<Entry>> groups = database.getGroups();

        if (groups.size() > 0) {
            DefaultListModel<String> groupModel = new DefaultListModel<>();

            for (String groupName : database.getGroupNames()) {
                groupModel.addElement(groupName);
            }

            lstGroups_Manage.setModel(groupModel);
        }
    }
    ///// End refreshGroups_Manage() ///// </editor-fold>

    ///// refreshSelectedGroup_Manage() ///// <editor-fold desc="~refreshSelectedGroup_Manage()~">
    private void refreshSelectedGroup_Manage () {
        lstGroups_Manage.setSelectedIndex(currentGroup_Manage);
        txtGroupName_Manage.setText(database.getGroupNamesIndex(currentGroup_Manage));
    }
    ///// End refreshSelectedGroup_Manage() ///// </editor-fold>

    ///// refreshEntries_Manage() ///// <editor-fold desc="~refreshEntries_Manage()~">
    private void refreshEntries_Manage () {
        List<List<Entry>> groups = database.getGroups();
        if (groups.get(currentGroup_Manage).size() > 0) {
            //DefaultListModel<String> entryModel = (DefaultListModel<String>) lstEntries_Manage.getModel();
            DefaultListModel<String> entryModel = new DefaultListModel<>();

            for (int i = 0; i < groups.get(currentGroup_Manage).size(); i++) {
                entryModel.addElement(groups.get(currentGroup_Manage).get(i).getName());
            }

            lstEntries_Manage.setModel(entryModel);
            lstEntries_Manage.setSelectedIndex(currentEntry_Manage);
        }
    }
    ///// End refreshEntries_Manage() ///// </editor-fold>

    ///// refreshSelectedEntry_Manage() ///// <editor-fold desc="~refreshSelectedEntry_Manage()~">
    private void refreshSelectedEntry_Manage () {
        txtUsername_Manage.setText(database.getEntry(currentGroup_Manage, currentEntry_Manage).getUsername());
        txtEmail_Manage.setText(database.getEntry(currentGroup_Manage, currentEntry_Manage).getEmail());
        pwdPassword_Manage.setText(database.getEntry(currentGroup_Manage, currentEntry_Manage).getPassword());
        txtURL_Manage.setText(database.getEntry(currentGroup_Manage, currentEntry_Manage).getUrl());
        txtNotes_Manage.setText(database.getEntry(currentGroup_Manage, currentEntry_Manage).getNotes());
        txtEntryName_Manage.setText(database.getEntry(currentGroup_Manage, currentEntry_Manage).getName());
    }
    ///// End refreshSelectedEntry_Manage() ///// </editor-fold>

    ///// loadDatabase_Manage() ///// <editor-fold desc="~loadDatabase_Manage()~">
    private void loadDatabase_Manage (File loadDirectory) throws Exception {
        Scanner scanner = new Scanner(loadDirectory);
        StringBuilder encryptedJson = new StringBuilder();
        while (scanner.hasNextLine()) {
            encryptedJson.append(scanner.nextLine());
        }
        scanner.close();

        Encryption crypt = new Encryption(databasePassword);
        String jsonData = crypt.decryptString(encryptedJson.toString());

        Gson gson = new Gson();
        database = gson.fromJson(jsonData, Database.class);
    }
    ///// End loadDatabase_Manage() ///// </editor-fold>

    ///// saveDatabase_Manage() ///// <editor-fold desc="~saveDatabase_Manage()~">
    private boolean saveDatabase_Manage (File saveDirectory, String databasePassword) throws Exception {
        Gson gson = new Gson();
        if (saveDirectory.exists()) { if (!saveDirectory.delete()) return false; }
        else { if (!saveDirectory.createNewFile()) return false; }

        String databaseJson = gson.toJson(new Database());
        String encryptedDatabaseJson = new Encryption(databasePassword).encryptString(databaseJson);

        FileWriter dataWriter = new FileWriter(saveDirectory);
        dataWriter.write(encryptedDatabaseJson);
        dataWriter.close();
        return true;
    }
    private boolean saveDatabase_Manage (File saveDirectory, Database newDatabase) throws Exception {
        Gson gson = new Gson();
        if (saveDirectory.exists()) { if (!saveDirectory.delete()) return false; }
        else { if (!saveDirectory.createNewFile()) return false; }

        String databaseJson = gson.toJson(newDatabase);
        String encryptedDatabaseJson = new Encryption(databasePassword).encryptString(databaseJson);

        FileWriter dataWriter = new FileWriter(saveDirectory);
        dataWriter.write(encryptedDatabaseJson);
        dataWriter.close();
        return true;
    }
    ///// End saveDatabase_Manage() ///// </editor-fold>
}
