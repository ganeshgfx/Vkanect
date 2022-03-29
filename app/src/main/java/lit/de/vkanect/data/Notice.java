package lit.de.vkanect.data;

public class Notice {
    public String text;
    public String link;
    public Notice(String text,String link){
        this.link = link;
        this.text = text;
    }
    public Notice(String text){
        this.text = text;
    }
    public Notice(){

    }
}
