package user.data;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = User.NQ_GET_USER_BY_PASSWORD_AND_NAME, query = "Select u from User u where u.name = :name and u.password = :password"),
        @NamedQuery(name = User.NQ_GET_USER_BY_NAME, query = "Select u from User u where u.name = :name")
})

@Table(name = "user_")
@Entity
public class User {

    public static final String NQ_GET_USER_BY_PASSWORD_AND_NAME = "user.get.by.password.and.user";
    public static final String NQ_GET_USER_BY_NAME = "user.get.by.name";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private byte[] salt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}
