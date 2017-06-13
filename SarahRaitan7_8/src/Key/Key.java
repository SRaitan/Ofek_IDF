package Key;
import java.io.Serializable;

public class Key <T> implements Serializable {
    public T key;

    public Key() {
    }

    public Key(T key) {

        this.key = key;
    }

    public T getKey(){return key;}
    public void setKey(T key ){this.key=key;}
}