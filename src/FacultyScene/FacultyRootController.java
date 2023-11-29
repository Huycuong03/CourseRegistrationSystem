package FacultyScene;

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
import javafx.scene.layout.Pane;
import entity.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class FacultyRootController implements Initializable{
    private User faculty;
    
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

    @FXML
    private Pane homePane;

    @FXML
    private Button registerButton;

    @FXML
    private ComboBox<String> searchComboBox1;

    @FXML
    private ComboBox<String> searchComboBox2;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<Course, String> userCourseIdColumn;

    @FXML
    private TableColumn<Course, String> userCourseInstructorColumn;

    @FXML
    private TableColumn<Course, String> userCourseNameColumn;

    @FXML
    private TableView<Course> userCourseTableView;

    @FXML
    private Label userNameLabel;

    @FXML
    private Button withdrawButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initConnection();
        initSearchComboBox();
    }    
    
    public void initLater(User user){
        faculty = user;
        userNameLabel.setText(faculty.getName().toUpperCase());
        initCourseTableView();
        initUserCourseTableView();
        resetTableView("");
    }
    
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
    
    public void resetTableView(String condition){
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        try {
            state = con.prepareStatement("select * from course where instructor is null or instructor <> ?"+condition);
            state.setString(1, faculty.getName());
            res = state.executeQuery();
            while(res.next()){
                courseList.add(new Course(res));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        courseTableView.setItems(courseList);
        
        ObservableList<Course> userCourseList = FXCollections.observableArrayList();
        try {
            state = con.prepareStatement("select * from course where instructor = ?");
            state.setString(1, faculty.getName());
            res = state.executeQuery();
            while(res.next()){
                userCourseList.add(new Course(res));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        userCourseTableView.setItems(userCourseList);
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
    }
    
    public void initUserCourseTableView(){
        userCourseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userCourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        userCourseInstructorColumn.setCellValueFactory(new PropertyValueFactory<>("instructor"));      
    }
    
    public void searchCourseTableView(){
        String searchKeyword = searchField.getText();
        String condition = "";

        if(!searchKeyword.equals("")){
            switch(searchComboBox1.getSelectionModel().getSelectedIndex()){
                case 0-> condition = " and courseId# ";
                case 1-> condition = " and cname ";
                case 2-> condition = " and instructor ";
                case 3-> condition = " and clocation ";
            }      
            condition += "like '%"+searchKeyword+"%'";
        }  

        int searchComboBox2OptionIndex = searchComboBox2.getSelectionModel().getSelectedIndex();
        if(searchComboBox2OptionIndex>0){
            condition += " and ";
            condition += (searchComboBox2OptionIndex==1)?"maxStu=nStu":"maxStu>nStu";
        }
        resetTableView(condition);
    }
    
    private Course selectedCourse = null;
    
    public void courseTableViewClicked(){
        selectedCourse = courseTableView.getSelectionModel().getSelectedItem();        
        if(selectedCourse!=null){
            withdrawButton.setDisable(true);
        }
        registerButton.setDisable(false);
    }
    
    public void userCourseTableViewClicked(){
        selectedCourse = userCourseTableView.getSelectionModel().getSelectedItem();        
        if(selectedCourse!=null){
            registerButton.setDisable(true);
        }        
        withdrawButton.setDisable(false);
    }
    
    public void homePaneClicked(){
        selectedCourse = null;
        registerButton.setDisable(false);
        withdrawButton.setDisable(false);
        searchField.requestFocus();
    }
    
    public void registerCourse(){
        if(selectedCourse==null) return;
        try {
            state = con.prepareStatement("update course set instructor = ? where courseId# = ?");
            state.setString(1, faculty.getName());
            state.setString(2, selectedCourse.getId());
            state.execute();
            withdrawButton.setDisable(false);
            resetTableView("");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }     
    }
    
        public void withdrawCourse(){
        if(selectedCourse==null) return;
        try {
            state = con.prepareStatement("update course set instructor = null where courseId# = ?");
            state.setString(1, selectedCourse.getId());
            state.execute();
            registerButton.setDisable(false);
            resetTableView("");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }     
    }
}
