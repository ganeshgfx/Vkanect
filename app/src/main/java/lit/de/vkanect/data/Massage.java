package lit.de.vkanect.data;

public class Massage {

    public static final String TYPE_NOTICE = "0";

    public String text;
    public String link;
    public String channel;
    public String sender;
    public String type;
    public String img;

    //for massage
    public Massage(String text,String sender,String type){
        this.text = text;
        this.sender = sender;
        this.type = type;
    }
    //void
    public Massage(){

    }
    //notice


    @Override
    public String toString() {
        return "Massage{" +
                "text='" + text + '\'' +
                ", link='" + link + '\'' +
                ", channel='" + channel + '\'' +
                ", sender='" + sender + '\'' +
                ", type='" + type + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
