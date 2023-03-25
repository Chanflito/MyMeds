package MyMeds.App;

import jakarta.persistence.*;
@MappedSuperclass
class User{
    @Id
   private Integer id;
    @Column
   private String username;
    @Column
   private String password;
   public User(Integer id, String username, String password){
    this.id = id;
    this.username = username;
    this.password = password;
   }

    public void setId(Integer id) {
        this.id = id;
    }

    public User(){}

   public Integer getPrimarykey(){
    return this.id;
   }

   public String getUsername(){
    return this.username;
   }

   public String getPassword(){
    return this.password;
   }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}