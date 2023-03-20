package MyMeds.App;

import jakarta.persistence.*;
@MappedSuperclass
class User{
    @Id
   private Integer primaryKey;
    @Column
   private String userName;
    @Column
   private String password;
   public User(Integer primarykey, String username, String password){
    this.primaryKey = primarykey;
    this.userName = username;
    this.password = password;
   }
   public User(){}

   public Integer getPrimarykey(){
    return this.primaryKey;
   }

   public String getUsername(){
    return this.userName;
   }

   public String getPassword(){
    return this.password;
   }
}