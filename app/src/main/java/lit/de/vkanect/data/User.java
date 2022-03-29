package lit.de.vkanect.data;

public class User {
    public String email,name,type;
    public User(){
    }

    public User(String email,String name,String type){
        this.email = email;
        this.name = name;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
