import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static ServerSocket ss;
    private Database db;
    private Listener listener;

    public Server () {
        boolean connected = false;
        try {
            ss = new ServerSocket(4242);
            System.out.println("Server is operational.");
            db = new Database();
            while (!connected) {
                connected = db.connect();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
    Thread class that continuously runs the determineMethod method to determine what process
    needs to be initialized next.  Basically, it continuously checks the input received from the client
    to determine what action needs to be completed.
     */
    public class Listener implements Runnable {
        private volatile boolean exit = false;
        private final Socket sock;
        public Listener(Socket s) {
            sock = s;
        }

        @Override
        public void run() {
            while (!exit) {
                    determineMethod(sock);
            }
        }

        public void stop() {
            exit = true;
        }
    }

    /*
    Connects the server to the client.
     */
    public void acceptConnection() {
        Socket sock = new Socket();
        try {
            sock = ss.accept();
            sendNumberStudents(sock);
        } catch (IOException ex) {
            System.out.println("acceptConnection." + ex);
        }
        listener = new Listener(sock);
        Thread keepAlive = new Thread(listener);
            keepAlive.start();
    }

    /*
    Looks for input from the client to determine which method/action needs to be completed.
     */
    private void determineMethod(Socket sock) {
        try {
            InputStream inputStream = sock.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            int selector = dataInputStream.readInt();
            switch (selector) {
                case -1:
                    deserialize(sock);
                    break;
                case -2:
                    pullStudentRequest(sock);
                    break;
                case -3:
                    transmitCourseList(sock);
                    break;
                case -4:
                    addCourseStudent(sock);
                    break;
                case -5:
                    transmitStudentCourseList(sock, dataInputStream.readInt());
                    break;
                case -6:
                    dropCourseStudent(sock);
                    break;
            }
        } catch (IOException ex) {
            listener.stop();
        }
    }

    /*
    Requests list of courses that the school is offering from the database server and transmits
    that list to the client.
     */
    public void transmitCourseList(Socket sock) {
        ArrayList<Course> courseList = db.getCourses();

        try {
            OutputStream outputStream = sock.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeInt(courseList.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            for (Course course : courseList) {
                OutputStream outputStream = sock.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(course);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
    Sends request to database for list of courses that are associated with the studentId
    that was received from the client.  It then transmits this list back to the client to be viewed.
     */
    private void transmitStudentCourseList(Socket sock, int studentId) {
        ArrayList<Course> courseList = db.getStudentCourses(studentId);

        try {
            OutputStream outputStream = sock.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeInt(courseList.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            for (Course course : courseList) {
                OutputStream outputStream = sock.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(course);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
    Deserializes the student object that was received from the client and then sends the data from that object
    to the database for storage.
     */
    private void deserialize(Socket sock) {

        try {
            InputStream inputStream = sock.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Student student = (Student) objectInputStream.readObject();
            db.addStudent(student);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /*
    Pulls the number of students from the database and sends that INT over to the client.
     */
    private void sendNumberStudents(Socket sock) {
        try {
            System.out.println("student numbers sent.");
            OutputStream outputStream = sock.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeInt(db.getNumberStudents());
        } catch (IOException ex) {
            listener.stop();
            ex.printStackTrace();
        }
    }

    /*
    Sends student # that was received from the client to the database to pull that student's information.
     */
    private void pullStudentRequest(Socket sock) {
        try {
            InputStream inputStream = sock.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            sendStudent(db.getStudent(dataInputStream.readInt()), sock);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
    Sends student object that was created based on data pulled from database back to the client.
     */
    private void sendStudent(Student s, Socket sock) {
        try {
            OutputStream outputStream = sock.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(s);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
    Sends request to database to add course to the student schedule database. Course ID and Student ID
    are transmitted to the server from the client.  The server sends this information to the databse to store
    it in the studentSchedule table.
     */
    private void addCourseStudent(Socket sock) {
        boolean outcome = false;

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(sock.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String data = bufferedReader.readLine();
            int studentId = Integer.parseInt(data.substring(0, data.indexOf(",")));
            int classId = Integer.parseInt(data.substring(data.indexOf(",") + 1));
            outcome = db.addClassStudent(studentId, classId);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            OutputStream outputStream = sock.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            if (outcome) {
                dataOutputStream.writeInt(1);
            } else {
                dataOutputStream.writeInt(-1);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
    Sends request to the database to remove a course from the studentSchedule database.
     */
    public void dropCourseStudent(Socket sock) {
        boolean outcome = false;

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(sock.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String data = bufferedReader.readLine();
            int studentId = Integer.parseInt(data.substring(0, data.indexOf(",")));
            int classId = Integer.parseInt(data.substring(data.indexOf(",") + 1));
            outcome = db.dropClassStudent(studentId, classId);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            OutputStream outputStream = sock.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            if (outcome) {
                dataOutputStream.writeInt(1);

            } else {
                dataOutputStream.writeInt(-1);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}