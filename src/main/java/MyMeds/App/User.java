package MyMeds.App;

class User{

   private Integer primarykey;
   private String username;
   private String password;

   public User(Integer primarykey, String username, String password){
    this.primarykey = primarykey;
    this.username = username;
    this.password = password;
   }

   public Integer getPrimarykey(){
    return this.primarykey;
   }

   public String getUsername(){
    return this.username;
   }

   public String getPassword(){
    return this.password;
   }
}