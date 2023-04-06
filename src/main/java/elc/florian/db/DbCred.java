package elc.florian.db;

public class DbCred {
    private String host;
    private String user;
    private String password;
    private String name;
    private int port;

    public DbCred(String host, String user, String password, String name, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.name = name;
        this.port = port;
    }

    public String toURL(){
        final StringBuilder sb = new StringBuilder();

        sb.append("jdbc:mysql://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(name);
        return sb.toString();
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
