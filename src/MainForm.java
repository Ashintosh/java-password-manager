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
    ///// Panels ///// <editor-fold desc="~Panels~">
    private JPanel mainFormPanel;
    private JPanel createDatabaseMain;
    private JPanel loadDatabaseMain;
    private JPanel welcomeMain;
    private JPanel manageDatabaseMain;
    private JPanel settingsMain;
    ///// End Panels ///// </editor-fold>
    ///// JButtons ///// <editor-fold desc="~JButtons~">
    /// Main ///
    private JButton btnLoad_Main;
    private JButton btnCreate_Main;

    /// Create ///
    private JButton btnBrowse_Create;
    private JButton btnCreate_Create;
    private JButton btnBack_Create;

    /// Load ///
    private JButton btnBrowse_Load;
    private JButton btnLoad_Load;
    private JButton btnBack_Load;

    /// Manage ///
    private JButton btnShow_Manage;
    private JButton btnCopy_Manage;
    private JButton btnAddGroup_Manage;
    private JButton btnAddEntry_Manage;
    private JButton btnDeleteGroup_Manage;
    private JButton btnDeleteEntry_Manage;
    private JButton btnSave_Manage;
    private JButton btnSaveGroupName_Manage;
    private JButton btnSaveEntryName_Manage;
    private JButton btnSettings_Manage;

    /// Settings ///
    private JButton btnSavePassword_Settings;
    private JButton btnBack_Settings;
    ///// End JButtons ///// </editor-fold>
    ///// JLabels ///// <editor-fold desc="~JLabels~">
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
    private JLabel labelNewPassword_Settings;
    private JLabel labelCurrentPassword_Settings;
    private JLabel frameLabel_Settings;
    private JLabel labelConfirmNewPassword_Settings;
    ///// End JLabels ///// </editor-fold>
    ///// JTextField ///// <editor-fold desc="~JTextField~">
    private JTextField txtDirectory_Create;
    private JTextField txtDirectory_Load;
    private JTextField txtUsername_Manage;
    private JTextField txtURL_Manage;
    private JTextField txtEmail_Manage;
    private JTextField txtGroupName_Manage;
    private JTextField txtEntryName_Manage;
    ///// End JTextField ///// </editor-fold>
    ///// JPasswordField ///// <editor-fold desc="~JPasswordField~">
    private JPasswordField pwdConfirm_Create;
    private JPasswordField pwdPassword_Create;
    private JPasswordField pwdPassword_Load;
    private JPasswordField pwdPassword_Manage;
    private JPasswordField pwdPassword_Settings;
    private JPasswordField pwdCurrentPassword_Settings;
    private JPasswordField pwdConfirm_Settings;
    ///// End JPasswordField ///// </editor-fold>
    ///// JList<String> ///// <editor-fold desc="~JList<String>~">
    private JList<String> lstGroups_Manage;
    private JList<String> lstEntries_Manage;
    ///// End JList<String> ///// </editor-fold>
    ///// JTextArea ///// <editor-fold desc="~JTextArea~">
    private JTextArea txtNotes_Manage;
    ///// End JTextArea ///// </editor-fold>
    ///// JScrollPane ///// <editor-fold desc="~JScrollPane~">
    private JScrollPane notesScrollPane_Manage;
    private JScrollPane groupsScrollPane_Manage;
    private JScrollPane entriesScrollPane_Manage;
    ///// End JScrollPane ///// </editor-fold>

    ///// Manage Panel Vars ///// <editor-fold desc="~Manage Panel Vars~"
    private static Database database;

    private boolean showPassword_Manage = false;

    private int currentGroup_Manage = 0;
    private int currentEntry_Manage = 0;

    private File fileDirectory;
    private String databasePassword;
    ///// End Manage Panel Vars ///// </editor-fold>

    public MainForm () {
        super("Secure Password Manager");                 // Set window title
        setContentPane(mainFormPanel);                        // Set current content pane to mainFormPanel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       // Set default close option to close on window exit
        setSize(870, 500);                       // Set window size to 870x500
        setResizable(false);                                 // Disable resizable window
        setVisible(true);                                    // Show window
        createDatabaseMain.setVisible(false);                // Hide create database panel
        loadDatabaseMain.setVisible(false);                  // Hide load database panel
        manageDatabaseMain.setVisible(false);                // Hide manage database panel
        settingsMain.setVisible(false);                      // Hide settings panel
        welcomeMain.setVisible(true);                        // Show welcome panel

        initialize();                                        // Initialize form
    }

    private void initialize () {
        ///// Action Listeners ///// <editor-fold desc="~Action Listeners~">
        ActionListener actionListener = e -> {
            /// Welcome Panel ///
            // btnLoad_Main // <editor-fold desc="~btnLoad_Main~">
            if (e.getSource() == btnLoad_Main) {
                // Show load database panel and hide welcome panel
                loadDatabaseMain.setVisible(true);
                welcomeMain.setVisible(false);
            }
            // End btnLoad_Main // </editor-fold>
            // btnCreate_Main // <editor-fold desc="~btnCreate_Main~">
            else if (e.getSource() == btnCreate_Main) {
                // Show create database panel and hide welcome panel
                createDatabaseMain.setVisible(true);
                welcomeMain.setVisible(false);
            }
            // End btnCreate_Main // </editor-fold>

            /// Create Panel ///
            // btnBack_Create // <editor-fold desc="~btnBack_Create~">
            else if (e.getSource() == btnBack_Create) {
                // Show welcome panel and hide create database panel
                welcomeMain.setVisible(true);
                createDatabaseMain.setVisible(false);
            }
            // End btnBack_Create // </editor-fold>
            // btnBrowse_Create // <editor-fold desc="~btnBrowse_Create~">
            else if (e.getSource() == btnBrowse_Create) {
                // Create file chooser; Set dialog title; Set dialog to only show files with .sdb extension
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Database File Directory");
                fileChooser.setFileFilter(new FileNameExtensionFilter("Secure Database File", "sdb"));

                // Get user response from dialog
                int response = fileChooser.showSaveDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) { // If user selected directory
                    // Check if selected file contains .sdb extension
                    String currentFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                    if (currentFilePath.contains(".sdb")) { // If selected directory contains .sdb extension
                        // Set txtDirectory_Create to user selected directory
                        txtDirectory_Create.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    } else {
                        // Set txtDirectory_Create to user selected directory + .sdb extension
                        txtDirectory_Create.setText(fileChooser.getSelectedFile().getAbsolutePath() + ".sdb");
                    }
                }
            }
            // End btnBrowse_Create // </editor-fold>
            // btnCreate_Create // <editor-fold desc="~btnCreate_Create~">
            else if (e.getSource() == btnCreate_Create) {
                if (txtDirectory_Create.getText().length() < 1) { // If no directory is selected
                    JOptionPane.showMessageDialog(createDatabaseMain, "Select database file directory.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (pwdPassword_Create.getPassword().length < 6) { // If pwdPassword_Create is less than 6 chars
                    JOptionPane.showMessageDialog(createDatabaseMain, "Password must be at least 6 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (!Arrays.equals(pwdPassword_Create.getPassword(), pwdConfirm_Create.getPassword())) { // If pwdPassword_Create and pwdConfirm_Create  do not match
                    JOptionPane.showMessageDialog(createDatabaseMain, "Passwords do not match.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else { // If all requirements met
                    try {
                        // Encrypt and save new database to directory in txtDirectory_Create
                        if (saveDatabase_Manage(new File(txtDirectory_Create.getText()), Encryption.toSha256(charToString(pwdPassword_Create.getPassword())))) {
                            txtDirectory_Create.setText("");
                            pwdPassword_Create.setText("");
                            pwdConfirm_Create.setText("");
                            JOptionPane.showMessageDialog(createDatabaseMain, "Database has been created.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            // If could not save database
                            JOptionPane.showMessageDialog(createDatabaseMain, "Could not create database.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(createDatabaseMain, "Could not create database.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            // End btnCreate_Create // </editor-fold>

            /// Load Panel ///
            // btnBack_Load // <editor-fold desc="~btnBack_Load~">
            else if (e.getSource() == btnBack_Load) {
                // Show welcome panel and hide load database panel
                welcomeMain.setVisible(true);
                loadDatabaseMain.setVisible(false);
            }
            // End btnBack_Load // </editor-fold>
            // btnBrowse_Load // <editor-fold desc="~btnBrowse_Load~">
            else if (e.getSource() == btnBrowse_Load) {
                try {
                    // Create file chooser; Set dialog title; Set dialog to only show files with .sdb extension
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Secure Database File", "sdb");
                    fileChooser.setDialogTitle("Database File Directory");
                    fileChooser.setFileFilter(filter);

                    // Get user response from dialog
                    int response = fileChooser.showOpenDialog(null);
                    if (response == JFileChooser.APPROVE_OPTION) { // If user selected directory
                        // Check if selected file contains .sdb extension
                        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                        if (filePath.contains(".sdb")) {
                            // Set txtDirectory_Create to user selected directory
                            txtDirectory_Load.setText(fileChooser.getSelectedFile().getAbsolutePath());
                        } else  {
                            // Set txtDirectory_Create to user selected directory + .sdb extension
                            txtDirectory_Load.setText(filePath + ".sdb");
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(loadDatabaseMain,"Could not set directory.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // End btnBrowse_Load // </editor-fold>
            // btnLoad_load <editor-fold desc="~btnLoad_Load~">
            else if (e.getSource() == btnLoad_Load) {
                try {
                    // Set global fileDirectory; Set global databasePassword
                    fileDirectory = new File(txtDirectory_Load.getText());
                    databasePassword = Encryption.toSha256(charToString(pwdPassword_Load.getPassword()));

                    // Load database from txtDirectory_Load directory
                    loadDatabase_Manage(new File(txtDirectory_Load.getText()));
                    // Refresh groups and entries UI elements
                    refreshGroups_Manage();
                    refreshSelectedGroup_Manage();
                    refreshEntries_Manage();
                    refreshSelectedEntry_Manage();

                    txtDirectory_Load.setText("");
                    pwdPassword_Load.setText("");

                    //setSize(860, 500);
                    // Show manage panel and hide load database panel
                    loadDatabaseMain.setVisible(false);
                    manageDatabaseMain.setVisible(true);
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(loadDatabaseMain,"Could not open database.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(loadDatabaseMain,"Could not decrypt database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // End btnLoad_Load // </editor-fold>

            /// Manage Panel ///
            // btnCopy_Manage // <editor-fold desc="~btnCopy_Manage~">
            else if (e.getSource() == btnCopy_Manage) {
                try {
                    // Get default clipboard toolkit; Copy pwdPassword_Manage to clipboard
                    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(new StringSelection(charToString(pwdPassword_Manage.getPassword())), null);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(manageDatabaseMain, "Could not copy password clipboard.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // End btnCopy_Manage // </editor-fold>
            // btnShow_Manage // <editor-fold desc="~btnShow_Manage~">
            else if (e.getSource() == btnShow_Manage) {
                Icon icon;
                if (showPassword_Manage) {
                    // Set icon object to eye; Set echo char to a dot
                    icon = new ImageIcon("resources/eye-unchecked-30.png");
                    pwdPassword_Manage.setEchoChar('•');
                } else {
                    // Set icon object to eye with check; Clear echo char
                    icon = new ImageIcon("resources/eye-checked-30.png");
                    pwdPassword_Manage.setEchoChar((char)0);
                }
                // Set btnShow_Manage icon to icon object; Change showPassword_Manage
                btnShow_Manage.setIcon(icon);
                showPassword_Manage = !showPassword_Manage;
            }
            // End // </editor-fold>
            // btnSave_Manage // <editor-fold desc="~btnSave_Manage~">
            else if (e.getSource() == btnSave_Manage) {
                try {
                    // Add entry with new data excluding entry name
                    database.addEntry(new Entry(database.getGroups().get(currentGroup_Manage).get(currentEntry_Manage).getName(), txtURL_Manage.getText(), txtUsername_Manage.getText(), charToString(pwdPassword_Manage.getPassword()), txtEmail_Manage.getText(), txtNotes_Manage.getText()), currentGroup_Manage, currentEntry_Manage);

                    // Save database to current directory
                    if (saveDatabase_Manage(fileDirectory, database)) {
                        // Refresh entries UI elements
                        refreshEntries_Manage();
                        refreshSelectedEntry_Manage();

                        JOptionPane.showMessageDialog(manageDatabaseMain, "Entry has been saved.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(manageDatabaseMain, "Could not save entry.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(manageDatabaseMain, "Could not save entry.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // End btnSave_Manage // </editor-fold>
            // btnAddEntry_Manage // <editor-fold desc="~btnAddEntry_Manage~">
            else if (e.getSource() == btnAddEntry_Manage) {
                try {
                    // Create new entry with default name and empty data; Add entry to database
                    Entry newEntry = new Entry("New Entry", "", "", "", "", "");
                    database.addEntry(newEntry, currentGroup_Manage);

                    // Save database to current directory; Load database from current directory
                    saveDatabase_Manage(fileDirectory, database);
                    loadDatabase_Manage(fileDirectory);

                    // Refresh groupsS and entries UI elements
                    refreshGroups_Manage();
                    refreshSelectedGroup_Manage();
                    refreshEntries_Manage();
                    refreshSelectedEntry_Manage();
                } catch (Exception ex) { JOptionPane.showMessageDialog(manageDatabaseMain, "Could not add entry.", "Error", JOptionPane.ERROR_MESSAGE); }
            }
            // End btnAddEntry_Manage // </editor-fold>
            // btnAddGroup_Manage // <editor-fold desc="~btnAddGroup_Manage~">
            else if (e.getSource() == btnAddGroup_Manage) {
                try {
                    // Add group to database
                    database.addGroup();

                    // Save database to current directory; Load database from current directory
                    saveDatabase_Manage(fileDirectory, database);
                    loadDatabase_Manage(fileDirectory);

                    // Refresh groups and entries UI elements
                    refreshGroups_Manage();
                    refreshSelectedGroup_Manage();
                    refreshEntries_Manage();
                    refreshSelectedEntry_Manage();
                } catch (Exception ex) { JOptionPane.showMessageDialog(manageDatabaseMain, "Could not add group.", "Error", JOptionPane.ERROR_MESSAGE); }
            }
            // End btnAddGroup_Manage // </editor-fold>
            // btnDeleteEntry_Manage // <editor-fold desc="~btnDeleteEntry_Manage~">
            else if (e.getSource() == btnDeleteEntry_Manage) {
                // Show confirmation dialog and store user response
                int result = JOptionPane.showConfirmDialog(mainFormPanel, "Are you sure you want to delete this entry?", "Confirm Deletion.",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) { // If user response is yes
                    try {
                        // Remove database entry from current group and current selected index; Change currentEntry_Manage
                        database.removeEntry(currentGroup_Manage, currentEntry_Manage);
                        currentEntry_Manage -= currentEntry_Manage;

                        // Save database to current directory; Load database from current directory
                        saveDatabase_Manage(fileDirectory, database);
                        loadDatabase_Manage(fileDirectory);

                        // Refresh groups and entries UI elements
                        refreshGroups_Manage();
                        refreshSelectedGroup_Manage();
                        refreshEntries_Manage();
                        refreshSelectedEntry_Manage();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(manageDatabaseMain, "Could not delete entry.", "Error", JOptionPane.ERROR_MESSAGE); }
                }
            }
            // End btnDeleteEntry_Manage // </editor-fold>
            // btnDeleteGroup_Manage // <editor-fold desc="~btnDeleteGroup_Manage~">
            else if (e.getSource() == btnDeleteGroup_Manage) {
                // Show confirmation dialog and store user response
                int result = JOptionPane.showConfirmDialog(mainFormPanel, "Are you sure you want to delete this group?", "Confirm Deletion.",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) { // If user response is yes
                    try {
                        // Remove database group from current group; Change currentGroup_Manage
                        database.removeGroup(currentGroup_Manage);
                        currentGroup_Manage -= currentGroup_Manage;

                        // Save database to current directory; Load database from current directory
                        saveDatabase_Manage(fileDirectory, database);
                        loadDatabase_Manage(fileDirectory);

                        // Refresh groups and entries UI elements
                        refreshGroups_Manage();
                        refreshSelectedGroup_Manage();
                        refreshEntries_Manage();
                        refreshSelectedEntry_Manage();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(manageDatabaseMain, "Could not delete group.", "Error", JOptionPane.ERROR_MESSAGE); }
                }
            }
            // End btnDeleteEntry_Manage // </editor-fold>
            // btnSaveEntryName_Manage // <editor-fold desc="~btnSaveEntryName_Manage~"?
            else if (e.getSource() == btnSaveEntryName_Manage) {
                try {
                    // Create new entry with data from JTextFields
                    Entry newEntry = new Entry(
                            txtEntryName_Manage.getText(),
                            database.getEntry(currentGroup_Manage, currentEntry_Manage).getUrl(),
                            database.getEntry(currentGroup_Manage, currentEntry_Manage).getUsername(),
                            database.getEntry(currentGroup_Manage, currentEntry_Manage).getPassword(),
                            database.getEntry(currentGroup_Manage, currentEntry_Manage).getEmail(),
                            database.getEntry(currentGroup_Manage, currentEntry_Manage).getNotes()
                    );
                    // Add entry to database
                    database.addEntry(newEntry, currentGroup_Manage, currentEntry_Manage);

                    // Save database to current directory; Load database from current directory
                    saveDatabase_Manage(fileDirectory, database);
                    loadDatabase_Manage(fileDirectory);

                    // Refresh groups and entries UI elements
                    refreshGroups_Manage();
                    refreshSelectedGroup_Manage();
                    refreshEntries_Manage();
                    refreshSelectedEntry_Manage();
                } catch (Exception ex) { JOptionPane.showMessageDialog(manageDatabaseMain, "Could not save entry name.", "Error", JOptionPane.ERROR_MESSAGE); }
            }
            // End btnSaveEntryName_Manage // </editor-fold>
            // btnSaveGroupName_Manage // <editor-fold desc="~btnSaveGroupName_Manage~"?
            else if (e.getSource() == btnSaveGroupName_Manage) {
                try {
                    // Change currently selected groups name to text from txtGroupName_Manage
                    database.setGroupNamesIndex(currentGroup_Manage, txtGroupName_Manage.getText());

                    // Save database to current file directory; Load database from current directory
                    saveDatabase_Manage(fileDirectory, database);
                    loadDatabase_Manage(fileDirectory);

                    // Refresh groups and entries UI elements
                    refreshGroups_Manage();
                    refreshSelectedGroup_Manage();
                    refreshEntries_Manage();
                    refreshSelectedEntry_Manage();
                } catch (Exception ex) { JOptionPane.showMessageDialog(manageDatabaseMain, "Could not save group name. " + ex, "Error", JOptionPane.ERROR_MESSAGE); }
            }
            // End btnSaveGroupName_Manage // </editor-fold>
            // btnSettings_Manage // <editor-fold desc="~btnSettings_Manage~">
            else if (e.getSource() == btnSettings_Manage) {
                settingsMain.setVisible(true);
                manageDatabaseMain.setVisible(false);
            }
            // End btnSettings_Manage // </editor-fold>

            /// Settings Panel ///
            // btnSavePassword_Settings // <editor-fold desc="~btnSavePassword_Settings~">
            else if (e.getSource() == btnSavePassword_Settings) {
                try {
                    String selectedHash = Encryption.toSha256(charToString(pwdCurrentPassword_Settings.getPassword()));
                    assert selectedHash != null;
                    if (!selectedHash.equals(databasePassword)) {
                        JOptionPane.showMessageDialog(settingsMain, "Current password is not correct.", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else if (!Arrays.equals(pwdPassword_Settings.getPassword(), pwdConfirm_Settings.getPassword()))  {
                        JOptionPane.showMessageDialog(settingsMain, "New passwords do not match.", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else if (pwdPassword_Settings.getPassword().length < 6) {
                        JOptionPane.showMessageDialog(settingsMain, "Password must be at least 6 characters.", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        databasePassword = Encryption.toSha256(charToString(pwdPassword_Settings.getPassword()));
                        saveDatabase_Manage(fileDirectory, databasePassword);
                        loadDatabase_Manage(fileDirectory);

                        refreshGroups_Manage();
                        refreshSelectedGroup_Manage();
                        refreshEntries_Manage();
                        refreshSelectedEntry_Manage();

                        JOptionPane.showMessageDialog(settingsMain, "Database password changed.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(settingsMain, "Could not change database password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // End btnSavePassword_Settings // </editor-fold>
            // btnBack_Settings // <editor-fold desc="~btnBack_Settings~">
            else if (e.getSource() == btnBack_Settings) {
                manageDatabaseMain.setVisible(true);
                settingsMain.setVisible(false);
            }
            // End btnBack_Settings // </editor-fold>
        };

        /// Welcome Panel ///
        btnLoad_Main.addActionListener(actionListener);
        btnCreate_Main.addActionListener(actionListener);

        /// Create Panel ///
        btnBack_Create.addActionListener(actionListener);
        btnBrowse_Create.addActionListener(actionListener);
        btnCreate_Create.addActionListener(actionListener);

        /// Load Panel ///
        btnBack_Load.addActionListener(actionListener);
        btnBrowse_Load.addActionListener(actionListener);
        btnLoad_Load.addActionListener(actionListener);

        /// Manage Panel ///
        btnCopy_Manage.addActionListener(actionListener);
        btnShow_Manage.addActionListener(actionListener);
        btnSave_Manage.addActionListener(actionListener);
        btnAddEntry_Manage.addActionListener(actionListener);
        btnAddGroup_Manage.addActionListener(actionListener);
        btnDeleteEntry_Manage.addActionListener(actionListener);
        btnDeleteGroup_Manage.addActionListener(actionListener);
        btnSaveEntryName_Manage.addActionListener(actionListener);
        btnSaveGroupName_Manage.addActionListener(actionListener);
        btnSettings_Manage.addActionListener(actionListener);

        /// Settings Panel ///
        btnSavePassword_Settings.addActionListener(actionListener);
        btnBack_Settings.addActionListener(actionListener);
        ///// End Action Listeners ///// </editor-fold>

        // List Selection Manager // <editor-fold desc="~List Selection Manager~">
        ListSelectionListener listSelectionListener = e -> { // When change in list's selected index
            // lstGroups_Manage // <editor-fold desc="~lstGroups_Manage~">
            if (e.getSource() == lstGroups_Manage) {
                if (lstGroups_Manage.getSelectedIndex() >= 0) {
                    currentGroup_Manage = lstGroups_Manage.getSelectedIndex();

                    // Refresh selected group and entries UI elements
                    refreshSelectedGroup_Manage();
                    refreshEntries_Manage();
                }
            }
            // End lstGroups_Manage // </editor-fold>
            // lstEntries_Manage // <editor-fold desc="~lstEntries_Manage~">
            else if (e.getSource() == lstEntries_Manage) {
                if (lstEntries_Manage.getSelectedIndex() >= 0) {
                    currentEntry_Manage = lstEntries_Manage.getSelectedIndex();

                    // Refresh selected entry UI element
                    refreshSelectedEntry_Manage();
                }
            }
            // End lstEntries_Manage // </editor-fold>
        };

        // Manage Panel //
        lstGroups_Manage.addListSelectionListener(listSelectionListener);
        lstEntries_Manage.addListSelectionListener(listSelectionListener);
        // End List Selection Manager // </editor-fold>

        ///// Mouse Events ///// <editor-fold desc="~Mouse Events~">
        // Default Buttons //
        Color defaultButtonColor = new Color(77, 81, 83);
        Color hoveredButtonColor = new Color(87, 91, 93);

        // Manage Panel Buttons //
        Color defaultManageButtonColor = new Color(30, 30, 30);
        Color hoveredManageButtonColor = new Color(28, 28, 28);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (
                        e.getComponent() == btnAddGroup_Manage    ||
                        e.getComponent() == btnAddEntry_Manage    ||
                        e.getComponent() == btnDeleteGroup_Manage ||
                        e.getComponent() == btnDeleteEntry_Manage ||
                        e.getComponent() == btnSettings_Manage
                )  { // If button is misc button
                    e.getComponent().setBackground(hoveredManageButtonColor);
                }
                else { // If other button
                    e.getComponent().setBackground(hoveredButtonColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (
                        e.getComponent() == btnAddGroup_Manage    ||
                        e.getComponent() == btnAddEntry_Manage    ||
                        e.getComponent() == btnDeleteGroup_Manage ||
                        e.getComponent() == btnDeleteEntry_Manage ||
                        e.getComponent() == btnSettings_Manage
                ) { // If button is misc button
                    e.getComponent().setBackground(defaultManageButtonColor);
                }
                else { // If other button
                    e.getComponent().setBackground(defaultButtonColor);
                }
            }
        };

        /// Main Panel ///
        btnLoad_Main.addMouseListener(mouseAdapter);
        btnCreate_Main.addMouseListener(mouseAdapter);

        /// Create Panel ///
        btnBrowse_Create.addMouseListener(mouseAdapter);
        btnCreate_Create.addMouseListener(mouseAdapter);

        /// Load Panel ///
        btnBrowse_Load.addMouseListener(mouseAdapter);
        btnLoad_Load.addMouseListener(mouseAdapter);

        /// Manage Panel ///
        btnSave_Manage.addMouseListener(mouseAdapter);
        btnAddGroup_Manage.addMouseListener(mouseAdapter);
        btnAddEntry_Manage.addMouseListener(mouseAdapter);
        btnDeleteGroup_Manage.addMouseListener(mouseAdapter);
        btnDeleteEntry_Manage.addMouseListener(mouseAdapter);
        btnSaveGroupName_Manage.addMouseListener(mouseAdapter);
        btnSaveEntryName_Manage.addMouseListener(mouseAdapter);
        btnSettings_Manage.addMouseListener(mouseAdapter);

        /// Settings Panel ///
        btnSavePassword_Settings.addMouseListener(mouseAdapter);
        ///// End Mouse Events ///// </editor-fold>

        ///// Set Borders ///// <editor-fold desc="~Set Borders~">
        /// Create Panel ///
        txtDirectory_Create.setBorder(BorderFactory.createEtchedBorder());
        pwdPassword_Create.setBorder(BorderFactory.createEtchedBorder());
        pwdConfirm_Create.setBorder(BorderFactory.createEtchedBorder());

        /// Load Panel ///
        txtDirectory_Load.setBorder(BorderFactory.createEtchedBorder());
        pwdPassword_Load.setBorder(BorderFactory.createEtchedBorder());

        /// Manage Panel ///
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
        entriesScrollPane_Manage.setBorder(BorderFactory.createEtchedBorder());
        notesScrollPane_Manage.setBorder(BorderFactory.createEtchedBorder());

        /// Settings Panel ///
        pwdPassword_Settings.setBorder(BorderFactory.createEtchedBorder());
        pwdCurrentPassword_Settings.setBorder(BorderFactory.createEtchedBorder());
        pwdConfirm_Settings.setBorder(BorderFactory.createEtchedBorder());
        ///// End Set Borders ///// </editor-fold>
    }

    ///// refreshGroups_Manage() ///// <editor-fold desc="~refreshGroups_Manage()~">
    private void refreshGroups_Manage () {
        List<List<Entry>> groups = database.getGroups();

        // If groups exists
        if (groups.size() > 0) {
            DefaultListModel<String> groupModel = new DefaultListModel<>();

            // For each group name, add name to groupModel
            for (String groupName : database.getGroupNames()) {
                groupModel.addElement(groupName);
            }

            // Set lstGroups_Manage model to groupModel
            lstGroups_Manage.setModel(groupModel);
        }
    }
    ///// End refreshGroups_Manage() ///// </editor-fold>

    ///// refreshSelectedGroup_Manage() ///// <editor-fold desc="~refreshSelectedGroup_Manage()~">
    private void refreshSelectedGroup_Manage () {
        // Set lstGroups_Manage selected index to current selected group index; Set txtGroupName_Manage text to current group name
        lstGroups_Manage.setSelectedIndex(currentGroup_Manage);
        txtGroupName_Manage.setText(database.getGroupNamesIndex(currentGroup_Manage));
    }
    ///// End refreshSelectedGroup_Manage() ///// </editor-fold>

    ///// refreshEntries_Manage() ///// <editor-fold desc="~refreshEntries_Manage()~">
    private void refreshEntries_Manage () {
        List<List<Entry>> groups = database.getGroups();

        // If entries exists in group
        if (groups.get(currentGroup_Manage).size() > 0) {
            //DefaultListModel<String> entryModel = (DefaultListModel<String>) lstEntries_Manage.getModel();
            DefaultListModel<String> entryModel = new DefaultListModel<>();

            // For each entry in current group, add name to entryModel
            for (int i = 0; i < groups.get(currentGroup_Manage).size(); i++) {
                entryModel.addElement(groups.get(currentGroup_Manage).get(i).getName());
            }

            // Set lstEntries_Manage mode to entryModel; Set lstEntries_Manage selected index to currentEntry_Manage
            lstEntries_Manage.setModel(entryModel);
            lstEntries_Manage.setSelectedIndex(currentEntry_Manage);
        }
    }
    ///// End refreshEntries_Manage() ///// </editor-fold>

    ///// refreshSelectedEntry_Manage() ///// <editor-fold desc="~refreshSelectedEntry_Manage()~">
    private void refreshSelectedEntry_Manage () {
        // Set JTextField in manage panel to data in current selected group and entry
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
        // Create Scanner for loadDirectory
        Scanner scanner = new Scanner(loadDirectory);
        // Read file from loadDirectory and write to encryptedJson StringBuilder string
        StringBuilder encryptedJson = new StringBuilder();
        while (scanner.hasNextLine()) {
            encryptedJson.append(scanner.nextLine());
        }
        scanner.close(); // Close Scanner

        // Create encryption object with current database password; Decrypt encryptedJson
        Encryption crypt = new Encryption(databasePassword);
        String jsonData = crypt.decryptString(encryptedJson.toString());

        // Set current database to Gson converted jsonData
        Gson gson = new Gson();
        database = gson.fromJson(jsonData, Database.class);
    }
    ///// End loadDatabase_Manage() ///// </editor-fold>

    ///// saveDatabase_Manage() ///// <editor-fold desc="~saveDatabase_Manage()~">
    private boolean saveDatabase_Manage (File saveDirectory, String databasePassword) throws Exception {
        Gson gson = new Gson();
        if (saveDirectory.exists()) { // If file exists
            if (!saveDirectory.delete()) { // If file could not be deleted
                return false;
            }
        } else { // If file does not exist
            if (!saveDirectory.createNewFile()) { // If could not create new file
                return false;
            }
        }

        // Create new database converted with Gson; Encrypt JSON string with encryption object
        String databaseJson = gson.toJson(new Database());
        String encryptedDatabaseJson = new Encryption(databasePassword).encryptString(databaseJson);

        // Write encrypted JSON to directory
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

    ///// charToString() ///// <editor-fold desc="~charToString()~">
    private String charToString (char[] charArray) {
        StringBuilder newString = new StringBuilder();
        // For each char in charArray, append to newString
        for (char c : charArray) {
            newString.append(c);
        }
        return newString.toString();
    }
    ///// End charToString() ///// </editor-fold>
}