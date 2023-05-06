package elc.florian.other;

public class InfoCity {
    private int habs_nb;
    private String habs;
    private String maire;
    private int lv;

    private String spawn_world;
    private double spawn_x;
    private double spawn_y;
    private double spawn_z;
    private float spawn_yaw;
    private float spawn_pitch;

    public int getHabs_nb() {
        return habs_nb;
    }

    public String getHabs() {
        return habs;
    }

    public String getMaire() {
        return maire;
    }

    public int getLv() {
        return lv;
    }


    public String getSpawn_world() {
        return spawn_world;
    }

    public double getSpawn_x() {
        return spawn_x;
    }

    public double getSpawn_y() {
        return spawn_y;
    }

    public double getSpawn_z() {
        return spawn_z;
    }

    public float getSpawn_yaw() {
        return spawn_yaw;
    }

    public float getSpawn_pitch() {
        return spawn_pitch;
    }

    public InfoCity(int habs_nb, String habs, String maire, int lv, String spawn_world, double spawn_x, double spawn_y, double spawn_z, float spawn_yaw, float spawn_pitch) {
        this.habs_nb = habs_nb;
        this.habs = habs;
        this.maire = maire;
        this.lv = lv;
        this.spawn_world = spawn_world;
        this.spawn_x = spawn_x;
        this.spawn_y = spawn_y;
        this.spawn_z = spawn_z;
        this.spawn_yaw = spawn_yaw;
        this.spawn_pitch = spawn_pitch;
    }

    public void setHabs_nb(int habs_nb) {
        this.habs_nb = habs_nb;
    }

    public void setHabs(String habs) {
        this.habs = habs;
    }
}
