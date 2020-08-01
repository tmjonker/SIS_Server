import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Database {

    Connection con;

    public Database() {

    }

    private String enterPassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter database password to continue.");
        return scanner.nextLine();
    }

    public boolean connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/students?characterEncoding=latin1";
            String user = "root";
            String pw = enterPassword();
            con = DriverManager.getConnection(url, user, pw);
            System.out.println("Connected to database.");
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage() + " Class not found Exception");
            System.exit(0);
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " SQLException");
            return false;
        }
        return false;
    }

    public void addStudent(Student s){

        try {
            Statement stmt = con.createStatement();
            String insert1 = "insert into studentInfo "
                    + "(studentId, status, firstName, middleName, lastName, street, city, state, zipCode, email) "
                    + "values ("
                    + s.getStudentNumber() +", "
                    + "'" + s.getStatus() + "', "
                    + "'" + s.getFirstName() + "', "
                    + "'" + s.getMiddleName() + "', "
                    + "'" + s.getLastName() + "', "
                    + "'" + s.getAddress() + "', "
                    + "'" + s.getCity() + "', "
                    + "'" + s.getState() + "', "
                    + s.getZipCode() + ", "
                    + "'" + s.getEmail() + "')";
            int i = stmt.executeUpdate(insert1);
            if (i==1) {
                System.out.println("User added to database.");
            } else {
                System.out.println("Failure.");
            }

            String insert2 = "update numberOfStudents set numberStudents = " + s.getStudentNumber() +
                " where id = 1";
            int j = stmt.executeUpdate(insert2);
            if (j==1) {
                System.out.println("Number of students updated.");
            } else {
                System.out.println("Failure.");
            }

        } catch (SQLException e) {
                updateStudentInfo(s);
        }
    }

    private void updateStudentInfo(Student s) {
        try {
            Statement stmt = con.createStatement();
            String update1 = "update studentInfo "
                    + "set firstName = '" + s.getFirstName() + "', "
                    + "middleName = '" + s.getMiddleName() + "', "
                    + "lastName = '" + s.getLastName() + "', "
                    + "street = '" + s.getAddress() + "', "
                    + "city = '" + s.getCity() + "', "
                    + "state = '" + s.getState() + "', "
                    + "zipCode = " + s.getZipCode() + ", "
                    + "email = '" + s.getEmail() + "' "
                    + "where studentId = " + s.getStudentNumber();
            System.out.println(update1);
            int i = stmt.executeUpdate(update1);
            if (i == 1) {
                System.out.println("User information has been updated.");
            } else {
                System.out.println("Failure.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getNumberStudents() {
        try {
            Statement s = con.createStatement();
            String select = "SELECT numberStudents FROM numberOfStudents WHERE id = 1";
            ResultSet row;
            row = s.executeQuery(select);
            row.next();
            return row.getInt("numberStudents");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public Student getStudent(int studentNumber) {
        Student student = new Student();
        try {
            Statement s = con.createStatement();
            String select = "SELECT * FROM studentInfo WHERE studentId = " + studentNumber;
            ResultSet row = s.executeQuery(select);
            row.next();
            student = new Student(row.getString("firstName"),
                    row.getString("middleName"),
                    row.getString("lastName"), row.getString("street"),
                    row.getString("city"), row.getString("state"),
                    row.getInt("zipCode"), row.getString("email"),
                    row.getInt("studentId"), row.getString("status"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return student;
    }

    public ArrayList<Course> getCourses() {
        ArrayList<Course> courseList = new ArrayList<>();
        try {
            Statement s = con.createStatement();
            String select = "SELECT * FROM classes";
            ResultSet row = s.executeQuery(select);

            while (row.next()) {
                courseList.add(new Course(row.getInt("classID"),
                        row.getString("className"),
                        row.getString("classDays"),
                        row.getString("classStartTime"),
                        row.getString("classEndTime")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return courseList;
    }

    /*
    Pull the courses that the student(studentId) is taking and store them in an ArrayList.
     */
    public ArrayList<Course> getStudentCourses(int studentId) {
        ArrayList<Course> courseList = new ArrayList<>();
        try {
            Statement s1 = con.createStatement();
            String select1 = "SELECT classid FROM studentschedule WHERE studentid = " + studentId;
            ResultSet row1 = s1.executeQuery(select1);
            ArrayList<Integer> tempList = new ArrayList<>();

            while (row1.next()) {
                tempList.add(row1.getInt("classId"));
            }

            for (Integer integer : tempList) {
                Statement s2 = con.createStatement();
                String select2 = "SELECT * FROM classes WHERE classid = " + integer;
                ResultSet row2 = s2.executeQuery(select2);
                row2.next();
                courseList.add(new Course(row2.getInt("classID"),
                        row2.getString("className"),
                        row2.getString("classDays"),
                        row2.getString("classStartTime"),
                        row2.getString("classEndTime")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return courseList;
    }

    public boolean addClassStudent(int studentId, int classId) {
        try {
            Statement s = con.createStatement();
            String insert = "INSERT INTO studentSchedule "
                    + "(studentId, classId) "
                    + "values ("
                    + studentId + ", " + classId + ")";
            s.executeUpdate(insert);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean dropClassStudent(int studentId, int classId) {
        try {
            Statement s = con.createStatement();
            String insert = "DELETE FROM studentSchedule "
                    + "WHERE classId = "
                    + classId + " AND studentId = "
                    + studentId;
            s.executeUpdate(insert);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
