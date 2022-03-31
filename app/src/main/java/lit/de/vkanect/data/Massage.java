package lit.de.vkanect.data;

public class Massage {
    public String text;
    public String link;
    public String channel;
    public String sender;
    public Massage(String text, String link){
        this.link = link;
        this.text = text;
    }
    public Massage(String text){
        this.text = text;
    }
    public Massage(){

    }
}
