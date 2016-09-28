package game.construction;

/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public class CommonConstruction {
    private int life;
    private String name;

    public CommonConstruction(int life, String name){
        setName(name);
        setLife(life);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public String getName() {
        return name;
    }

    public int getLife() {
        return life;
    }
}
