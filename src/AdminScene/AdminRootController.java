package AdminScene;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.*;
import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class AdminRootController implements Initializable{
    
    private Connection con;
    private PreparedStatement state;
    private ResultSet res;
    
    public void initConnection(){
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sys as sysdba", "huycuong03");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @FXML
    private TableColumn<Course, String> courseIdColumn;

    @FXML
    private TableColumn<Course, String> courseInstructorColumn;

    @FXML
    private TableColumn<Course, String> courseLocationColumn;

    @FXML
    private TableColumn<Course, Integer> courseMaxStuColumn;

    @FXML
    private TableColumn<Course, Integer> courseNSectionColumn;

    @FXML
    private TableColumn<Course, Integer> courseNStuColumn;

    @FXML
    private TableColumn<Course, String> courseNameColumn;

    @FXML
    private TableView<Course> courseTableView;
    
    public void resetCourseTableView(String queryCondition){
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        try {
            state = con.prepareStatement("select * from course"+queryCondition);
            res = state.executeQuery();
            while(res.next()){
                courseList.add(new Course(res));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        courseTableView.setItems(courseList);
    }
    
    public void initCourseTableView(){

        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseMaxStuColumn.setCellValueFactory(new PropertyValueFactory<>("maxStu"));
        courseNStuColumn.setCellValueFactory(new PropertyValueFactory<>("nStu"));
        courseInstructorColumn.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        courseNSectionColumn.setCellValueFactory(new PropertyValueFactory<>("nSection"));
        courseLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        
        courseIdColumn.setSortable(false);
        courseNameColumn.setSortable(false);
        courseMaxStuColumn.setSortable(false);
        courseNStuColumn.setSortable(false);
        courseInstructorColumn.setSortable(false);
        courseNSectionColumn.setSortable(false);
        courseLocationColumn.setSortable(false);
        
        resetCourseTableView("");
    }

    @FXML
    private ComboBox<String> idComboBox;
    
    public void initIdComboBox(){
        String[] idOptions = {"INT","SKD","BAS","ELE"};
        ObservableList<String> idItems = FXCollections.observableArrayList(idOptions);
        idComboBox.setItems(idItems);
        idComboBox.setValue("INT");
    }
    
    @FXML
    private ComboBox<String> userIdComboBox;
    
    public void initUserIdComboBox(){
        String[] idOptions = {"STU","FAC"};
        ObservableList<String> idItems = FXCollections.observableArrayList(idOptions);
        userIdComboBox.setItems(idItems);
        userIdComboBox.setValue("STU");
    }

    @FXML
    private TextField instructorField;
    
    public void focusToLocationField(){
        locationField.requestFocus();
    }

    @FXML
    private TextField locationField;
    
    public void focusToMaxStuField(){
        maxStuField.requestFocus();
    }

    @FXML
    private TextField maxStuField;
    
    public void focusToSectionField(){
        sectionField.requestFocus();
    }

    @FXML
    private TextField nameField;
    
    public void focusToInstructorField(){
        instructorField.requestFocus();
    }

    @FXML
    private Button removeButton;
    
    public void removeCourseTableView(){
        if(selectedCourse==null) return;
        
        try {
            state = con.prepareStatement("delete from course where courseId# = ?");
            state.setString(1, selectedCourse.getId());
            state.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        resetCourseTableView("");
        clearInsertField();           
    }

    @FXML
    private ComboBox<String> searchComboBox1;

    @FXML
    private ComboBox<String> searchComboBox2;
    
    public void initSearchComboBox(){
        String[] searchOptions1 = {"ID","Name","Instructor","Location"};
        ObservableList<String> searchItems = FXCollections.observableArrayList(searchOptions1);
        searchComboBox1.setItems(searchItems);
        searchComboBox1.setValue("ID");
        
        String[] searchOptions2 = {"All","Full","Avaiable"};
        searchItems = FXCollections.observableArrayList(searchOptions2);
        searchComboBox2.setItems(searchItems);
        searchComboBox2.setValue("All");
    }

    @FXML
    private TextField searchField;
    
    public void searchCourseTableView(){
        String searchKeyword = searchField.getText();
        String condition = "";

        if(!searchKeyword.equals("")){
            switch(searchComboBox1.getSelectionModel().getSelectedIndex()){
                case 0-> condition = " where courseId# like '%"+searchKeyword+"%'";
                case 1-> condition = " where cname like '%"+searchKeyword+"%'";
                case 2-> condition = " where instructor like '%"+searchKeyword+"%'";
                case 3-> condition = " where clocation like '%"+searchKeyword+"%'";
            }                            
        }  

        int searchComboBox2OptionIndex = searchComboBox2.getSelectionModel().getSelectedIndex();
        if(searchComboBox2OptionIndex>0){
            if(condition.equals("")) condition = " where ";
            else condition += " and ";
            condition += (searchComboBox2OptionIndex==1)?"maxStu=nStu":"maxStu>nStu";
        }
        resetCourseTableView(condition);
    }
    

    @FXML
    private TextField sectionField;

    @FXML
    private Button sortButton;
    
    public void sortCourseTableView(){
        FXCollections.sort(courseTableView.getItems());
    }

    @FXML
    private TableColumn<User, String> studentIdColumn;

    @FXML
    private TableColumn<User, String> studentNameColumn;
    
    @FXML
    private TableColumn<User, String> studentPasswordColumn;

    @FXML
    private TableView<User> studentTableView;

    @FXML
    private TableColumn<User, String> studentUsernameColumn;
    
    public void initStudentTableView(){
        studentTableView.setContextMenu(null);
        
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        studentPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        
        resetStudentTableView();
    }
    
    public void resetStudentTableView(){
        String studentQuery = "select * from userx";
        if(selectedCourse==null){
//            studentQuery += " where id# like 'STU%'";
        }else{
            studentQuery += " natural join courseStudent where courseId# = '"+selectedCourse.getId()+"'";
        }
        ObservableList<User> studentList = FXCollections.observableArrayList();
        try {
            res = state.executeQuery(studentQuery);
            while(res.next()){
                studentList.add(new User(res));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        studentTableView.setItems(studentList);
    }

    @FXML
    private Button updateButton;
    private Course selectedCourse;
    
    public boolean isFullInsertField(){
        if(nameField.getText().equals("")) return false;
        if(instructorField.getText().equals("")) return false;
        if(locationField.getText().equals("")) return false;
        if(maxStuField.getText().equals("")) return false;
        return !(sectionField.getText().equals(""));
    }
    
    public void clearInsertField(){       
        nameField.setText("");
        maxStuField.setText("");
        instructorField.setText("");
        sectionField.setText("");
        locationField.setText("");
        
        if(selectedCourse!=null){
            idComboBox.setDisable(false);
            selectedCourse = null;
        }
    }
    
    public void updateButtonClicked(){
        if(!isFullInsertField()) return;
        String name = nameField.getText();
        Integer maxStu = Integer.valueOf(maxStuField.getText());
        String instructor = instructorField.getText();
        Integer nSection = Integer.valueOf(sectionField.getText());
        String location = locationField.getText();
        
        try {
            if(selectedCourse==null){               
                state = con.prepareStatement("insert into course values(?,?,?,0,?,?,?)");
                state.setString(1, idComboBox.getValue()+String.format("%03d", courseTableView.getItems().size()));
                state.setString(2, name);
                state.setInt(3, maxStu);
                state.setString(4, instructor);
                state.setInt(5, nSection);
                state.setString(6, location);
            }else{
                state = con.prepareStatement("update course set cname = ?, maxStu = ?, instructor = ?, nSection = ?, clocation = ? where courseId# = ?");
                state.setString(1, name);
                state.setInt(2, maxStu);
                state.setString(3, instructor);
                state.setInt(4, nSection);
                state.setString(5, location);
                state.setString(6, selectedCourse.getId());
            }       
            state.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        resetCourseTableView("");
        clearInsertField();
    }
    
    public void courseTableViewClicked(){
        selectedCourse = (Course) courseTableView.getSelectionModel().getSelectedItem();
        
        if(selectedCourse==null) return;
        
        idComboBox.setValue(selectedCourse.getId().substring(0, 3));
        idComboBox.setDisable(true);
        
        nameField.setText(selectedCourse.getName());
        maxStuField.setText(String.valueOf(selectedCourse.getMaxStu()));
        instructorField.setText(selectedCourse.getInstructor());
        sectionField.setText(String.valueOf(selectedCourse.getNSection()));
        locationField.setText(selectedCourse.getLocation());
        
        resetStudentTableView();
    }

    @FXML
    private Label userNameLabel; 
    
    public void displayUserNameLabel(String userName){
        userNameLabel.setText(userName);
    }
    
    @FXML
    private Pane homePane;
    
    public void homePaneClicked(){
        if(selectedCourse!=null&&isFullInsertField()){
            clearInsertField();  
            resetStudentTableView();
            nameField.requestFocus();
        }
    }
    
    @FXML
    private Button createButton;
    private final String initPassword = "cntt";
    
    public void createStudentAccount(){
        String name = newStudentNameField.getText();
        if(name.equals("")) return;
        String[] splitName = name.split(" ");
        if(splitName.length<2) return;
        try {
            state = con.prepareStatement("insert into userx values(?,?,?,?)");
            state.setString(1, String.format(userIdComboBox.getSelectionModel().getSelectedItem()+"%03d",(int) (Math.random()*999)));
            state.setString(2, splitName[splitName.length-1].toLowerCase()+ splitName[0].toLowerCase());
            state.setString(3, initPassword);
            state.setString(4, name);
            state.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        resetStudentTableView();
    }
    
    @FXML
    private TextField newStudentNameField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initConnection();
        initCourseTableView();
        initStudentTableView();
        initSearchComboBox();
        initIdComboBox();
        initUserIdComboBox();
    }

}
