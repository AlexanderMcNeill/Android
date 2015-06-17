package bit.mcnear1.agilemanager;

/**
 * Created by alexmcneill on 12/06/15.
 */
public class ScrumItem {
    private String date;
    private String teamName;
    private String comment;
    private int scrumID;

    public ScrumItem(String date, String teamName, String comment, int scrumID)
    {
        this.date = date;
        this.teamName = teamName;
        this.comment = comment;
        this.scrumID = scrumID;
    }

    public String getDate() {
        return date;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getComment() {
        return comment;
    }

    public int getScrumID() {
        return scrumID;
    }
}
