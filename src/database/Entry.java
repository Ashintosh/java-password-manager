package database;

import java.util.Arrays;
import java.util.List;

public class Entry {
    private String name;
    private String url;
    private String username;
    private String password;
    private String email;
    private String notes;

    public Entry (String pName, String pUrl, String pUsername, String pPassword, String pEmail, String pNotes) {
        name = pName;
        url = pUrl;
        username = pUsername;
        password = pPassword;
        email = pEmail;
        notes = pNotes;
    }

    /// Get Functions /// <editor-fold desc="~Get Functions~">
    public String getName()      { return name; }
    public String getUrl()       { return url; }
    public String getUsername()  { return username; }
    public String getPassword()  { return password; }
    public String getEmail()     { return email; }
    public String getNotes()     { return notes; }
    public String[] getEntryArray () { return new String[] { name, url, username, password, email, notes }; }
    public List<String> getEntryList () { return Arrays.asList(getEntryArray()); }
    /// End Get Functions /// </editor-fold>

    /// Set Functions /// <editor-fold desc="~Set Functions~">
    public void setName (String pName) { name = pName; }
    public void setUrl (String pUrl) { url = pUrl; }
    public void setUsername (String pUsername) { username = pUsername; }
    public void setPassword (String pPassword) { password = pPassword; }
    public void setEmail (String pEmail) { email = pEmail; }
    public void setNotes (String pNotes) { notes = pNotes; }
    public void setEntry (String pName, String pUrl, String pUsername, String pPassword, String pEmail, String pNotes) {
        name = pName;
        url = pUrl;
        username = pUsername;
        password = pPassword;
        email = pEmail;
        notes = pNotes;
    }
    /// End Set Functions /// </editor-fold>
}
