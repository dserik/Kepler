package testpac;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Student {

    @Id
    public int id;

    public String firstName;

    public String middleName;

    public String lastName;

    public Date birthDate;

}
