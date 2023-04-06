package model;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "NationalityBean")
@ApplicationScoped
public class NationalityBean {

    private List<String> nationalities;

    public NationalityBean() {
        nationalities = new ArrayList<String>();
        nationalities.add("Morroco");
        nationalities.add("USA");
        nationalities.add("Canada");
        nationalities.add("Mexico");
        nationalities.add("Japan");
        nationalities.add("China");
    }

    public List<String> getNationalities() {
        return nationalities;
    }

}
