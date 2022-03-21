package database;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<String> groupNames;
    private List<List<Entry>> groups;

    public Database (List<List<Entry>> pGroups, List<String> pGroupNames) {
        groups = pGroups;
        groupNames = pGroupNames;
    }

    public Database () {
        List<Entry> rootEntries = new ArrayList<>();

        groupNames = new ArrayList<>();
        groupNames.add("root");

        rootEntries.add(new Entry("New Entry", "", "", "", "", ""));

        groups = new ArrayList<>();
        groups.add(rootEntries);
    }

    ///// Get Functions ///// <editor-fold desc="~Get Functions~">
    public List<List<Entry>> getGroups () {
        return groups;
    }

    public List<String> getGroupNames () {
        return groupNames;
    }

    public String getGroupNamesIndex (int nameIndex) {
        return groupNames.get(nameIndex);
    }

    public Entry getEntry (int groupIndex, int entryIndex) {
        return groups.get(groupIndex).get(entryIndex);
    }
    ///// End Get Functions ///// </editor-fold>

    ///// Set Functions ///// <editor-fold desc="~Set Functions~">
    public void setGroupNames (List<String> pGroupNames) {
        groupNames = pGroupNames;
    }

    public void setGroupNamesIndex (int groupNamesIndex, String pName) {
        groupNames.set(groupNamesIndex, pName);
    }

    public void setGroups (List<List<Entry>> pGroups) {
        groups = pGroups;
    }
    ///// End Set Functions ///// </editor-fold>


    ///// addEntry() ///// <editor-fold desc="~addEntry()~">
    public void addEntry (Entry pEntry, int groupIndex) {
        groups.get(groupIndex).add(pEntry);
    }
    public void addEntry (Entry pEntry, int groupIndex, int entryIndex) {
        groups.get(groupIndex).set(entryIndex, pEntry);
    }
    ///// End addEntry() ///// </editor-fold>

    ///// addGroup() ///// <editor-fold desc="~addGroup()~">
    public void addGroup () {
        List<Entry> tmpEntry = new ArrayList<>();
        tmpEntry.add(new Entry("New Entry", "", "", "", "", ""));
        groupNames.add("New Group");
        groups.add(tmpEntry);
    }
    ///// End addGroup() ///// </editor-fold>


    ///// removeEntry() ///// <editor-fold desc="~removeEntry()~">
    public void removeEntry (int groupIndex, int entryIndex) {
        groups.get(groupIndex).remove(entryIndex);
    }
    ///// End removeEntry() ///// </editor-fold>

    ///// removeGroup() ///// <editor-fold desc="~removeGroup()~">
    public void removeGroup (int groupIndex) {
        groupNames.remove(groupIndex);
        groups.remove(groupIndex);
    }
    ///// End removeGroup() ///// </editor-fold>
}
