package entity;

import java.util.Objects;

public class Course implements Comparable<Course>{
    public static final int LEN = 7;
    private final String id;
    private final String name;
    private Integer maxStu;
    private Integer nStu;
    private String instructor;
    private Integer nSection;
    private String location;
    
    public Course(java.sql.ResultSet courseInfo) throws java.sql.SQLException{
        this.id = courseInfo.getString("courseId#");
        this.name = courseInfo.getString("cname");
        this.maxStu = courseInfo.getInt("maxStu");
        this.nStu = courseInfo.getInt("nStu");
        this.instructor = courseInfo.getString("instructor");
        this.nSection = courseInfo.getInt("nSection");
        this.location = courseInfo.getString("clocation");
    }

    public Course(String id, String name, Integer maxStu, Integer nStu, String instructor, Integer nSection, String location) {
        this.id = id;
        this.name = name;
        this.maxStu = maxStu;
        this.nStu = nStu;
        this.instructor = instructor;
        this.nSection = nSection;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getMaxStu() {
        return maxStu;
    }

    public Integer getNStu() {
        return nStu;
    }

    public String getInstructor() {
        return instructor;
    }
    
    public String getLocation() {
        return location;
    }

    public Integer getNSection() {
        return nSection;
    }

    public void setMaxStu(int maxStu) {
        this.maxStu = maxStu;
    }

    public void setnStu(int nStu) {
        this.nStu = nStu;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }

    public void setSection(int section) {
        this.nSection = section;
    }

    @Override
    public String toString() {
        return String.format("values('%s', '%s', %d, %d, '%s', %d, '%s')", 
                                    id, name, maxStu, nStu, instructor, nSection, location);
    } 
    
    @Override
    public int compareTo(Course A){
        if(this.nStu>A.nStu) return 1;
        if(Objects.equals(this.nStu, A.nStu)){
            if(this.maxStu>A.maxStu) return 1;
            if(Objects.equals(this.maxStu, A.maxStu)) return 0;
            return -1;
        }
        return -1;
    }
}
