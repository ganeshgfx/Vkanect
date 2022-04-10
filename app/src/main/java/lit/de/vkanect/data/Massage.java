package lit.de.vkanect.data;

import java.util.Arrays;

public class Massage {

    public static final String TYPE_NOTICE = "0";
    public static final String TYPE_WORK = "1";

    public String text;
    public String link;
    public String channel;
    public String sender;
    public String type;
    public String img;
    public String tags[];
    public String key;

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

    public Massage setKey(String key) {
        this.key = key;
        return this;
    }
    public Massage setText(String text) {
        this.text = text;
        return this;
    }

    public Massage setLink(String link) {
        this.link = link;
        return this;
    }

    public Massage setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public Massage setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public Massage setType(String type) {
        this.type = type;
        return this;
    }

    public Massage setImg(String img) {
        this.img = img;
        return this;
    }

    public Massage setTags(String[] tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public String toString() {
        return "Massage{" +
                "text='" + text + '\'' +
                ", link='" + link + '\'' +
                ", channel='" + channel + '\'' +
                ", sender='" + sender + '\'' +
                ", type='" + type + '\'' +
                ", img='" + img + '\'' +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}
