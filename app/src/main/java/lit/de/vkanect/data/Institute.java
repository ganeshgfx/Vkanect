package lit.de.vkanect.data;

public class Institute {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String id;
    public String name;
    public String owner;
    public Institute(){

    }
    public Institute(String owner,String id, String name){
        this.owner = owner;
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Institute{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
