package saiernet.server.srnserver.entity;


//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@JsonIgnoreProperties(value = { "handler"}) 
public class users {
    private int id;
    private String username;
    private String password;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username){
        this.username=username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}