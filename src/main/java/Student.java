import java.io.Serializable;

public class Student implements Serializable{

    public String firstName, middleName, lastName, address, city, state, email, status;
    public int zipCode;
    private int studentNumber;

    public Student (String fn, String mn, String ln, String add, String c, String st,
                    int zc, String em, int sn) {

        studentNumber = sn;
        status = "inactive";
        firstName = fn;
        middleName = mn;
        lastName = ln;
        address = add;
        city = c;
        state = st;
        zipCode = zc;
        email = em;
    }

    public Student (String fn, String mn, String ln, String add, String c, String st,
                    int zc, String em, int sn, String s) {
        studentNumber = sn;
        status = s;
        firstName = fn;
        middleName = mn;
        lastName = ln;
        address = add;
        city = c;
        state = st;
        zipCode = zc;
        email = em;
    }

    public Student() {

    }

    /*
   Formats student number into a 5 digit String.
    */
    public String reformatStudentNum() {
        return String.format("%05d",(studentNumber));
    }

    public void updateStudent(String fn, String mn, String ln, String add, String c, String st,
                              int zc, String em) {
        firstName = fn;
        middleName = mn;
        lastName = ln;
        address = add;
        city = c;
        state = st;
        zipCode = zc;
        email = em;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getEmail() {
        return email;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStatus(boolean s) {
        if (s) status = "active";
        else status = "inactive";
    }

    public String getStatus() {
        return status;
    }
}