package model;

import org.hibernate.*;
import org.hibernate.Query;
import org.hibernate.cfg.Configuration;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "login_bean")
@SessionScoped
@Entity
public class Usercnx implements Serializable {


    @Id
    @Column
            //(name = "EMAIL", table = "USERCNX")
    private String email;
    @Basic
    @Column
            //(name = "NOM", table = "USERCNX")
    private String nom;
    @Basic
    @Column
            //(name = "PASSWORD", table = "USERCNX")
    private String password;
    @Basic
    @Column
            //(name = "PRENOM", table = "USERCNX")
    private String prenom;
    @Basic
    @Column
            //(name = "CIN", table = "USERCNX")
    private String cin;
    @Basic
    @Column
            //(name = "GENRE", table = "USERCNX")
    private String genre;
    @Basic
    @Column
            //(name = "NATIONALITE", table = "USERCNX")
    private String nationalite;

    public Usercnx() {
    }

    public Usercnx(String email, String password, String nom, String prenom, String cin, String genre, String nationalite) {
        this.email = email;
        this.nom = nom;
        this.password = password;
        this.prenom = prenom;
        this.cin = cin;
        this.genre = genre;
        this.nationalite = nationalite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usercnx usercnx = (Usercnx) o;

        if (email != null ? !email.equals(usercnx.email) : usercnx.email != null) return false;
        if (nom != null ? !nom.equals(usercnx.nom) : usercnx.nom != null) return false;
        if (prenom != null ? !prenom.equals(usercnx.prenom) : usercnx.prenom != null) return false;
        if (cin != null ? !cin.equals(usercnx.cin) : usercnx.cin != null) return false;
        if (genre != null ? !genre.equals(usercnx.genre) : usercnx.genre != null) return false;
        if (nationalite != null ? !nationalite.equals(usercnx.nationalite) : usercnx.nationalite != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (prenom != null ? prenom.hashCode() : 0);
        result = 31 * result + (cin != null ? cin.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (nationalite != null ? nationalite.hashCode() : 0);
        return result;
    }
    public String logging() {
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Usercnx where email=:email and password=:password ");
            query.setString("email", email);
            query.setString("password", password);
            List list = query.list();
            if (list.size() == 1) {
                String userName=getUsernameFromEmail(email);
                System.out.println(userName);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", userName);

                return "succes.xhtml";

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid login or password"));
                // return null to stay on the same page
                return null;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return "index.xhtml";
    }
    public String getUsernameFromEmail(String email) {
        String hql = "SELECT nom  FROM Usercnx   WHERE  email = :email";
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        String username = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("email", email);
            username = (String) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return username;
    }

    public String addUser() {
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            if(!UserExiste()) {
                session.save(new Usercnx(email, password, nom, prenom, cin, genre, nationalite));
                session.beginTransaction().commit();
                return "registerSucces.xhtml";
            }
            else {
                //FacesMessage errorMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email already existe");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Email already existe"));
                // Add the error message to the FacesContext
              //  FacesContext facesContext = FacesContext.getCurrentInstance();
               // facesContext.addMessage(null, errorMessage);
             //   return "ErroPage.xhtml";
                return  null;

            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return "index.xhtml";
    }
    public void validateEmail(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String email = (String) value;
        String pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (!email.matches(pattern)) {
            FacesMessage message = new FacesMessage(" format Invalid .");
            throw new ValidatorException(message);
        }
    }

    public boolean UserExiste(){
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Usercnx where email=:email ");
            query.setString("email", email);
            List list = query.list();
            if (list.size() == 1)
                return true;
            return false;
        }
        catch(Exception e) {
            System.out.println(e);
            return false;
        }


    }
    public String doLogout() {
        // Invalidate the session and redirect to login page
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml";
    }

}
