package entity;

public class User {
    public static final int LEN = 6;
    final String id;
    final String name;
    final String username;
    final String password;
    
    public User(java.sql.ResultSet userInfo)throws java.sql.SQLException{
        this.id = userInfo.getString("id#");
        this.name = userInfo.getString("full_name");
        this.username = userInfo.getString("username");
        this.password = userInfo.getString("pin");
    }

    public String getId() {
        return id;
    }
    
    public String getName(){
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    @Override
    public String toString() {
        return (id+" "+name);
    }
}
