package elc.florian.other;

import java.util.UUID;

public class Info_player {
    private UUID uuid;
    private String grade;
    private String ville;
    private int lv;
    private String travail;


    public Info_player(UUID uuid, String grade, String ville, int lv, String travail) {
        this.uuid = uuid;
        this.grade = grade;
        this.ville = ville;
        this.lv = lv;
        this.travail = travail;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getGrade() {
        return grade;
    }

    public String getVille() {
        return ville;
    }

    public int getLv() {
        return lv;
    }

    public String getTravail() {
        return travail;
    }
}
