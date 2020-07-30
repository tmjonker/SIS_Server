import java.io.Serializable;

public class Course implements Serializable {

    private String name;
    private int id;
    private String days;
    private String startTime;
    private String endTime;

    public Course(int ci, String cn, String cd, String cst, String cet) {
        name = cn;
        id = ci;
        days = cd;
        startTime = cst;
        endTime = cet;
    }

    public Course() {

    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDays() {
        return days;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}