package fr.ancelotow.catfacar.technique;

import fr.ancelotow.catfacar.entities.User;

public class Session {

    private static Session session = null;
    private User user;

    private Session(User user){
        super();
        this.user = user;
    }

    public static void ouvrir(User user){
        if(session == null){
            session = new Session(user);
        }
    }

    public static void fermer(){
      session = null;
    }

    public static Session getSession(){
        return session;
    }

    public User getUser(){
        return user;
    }

}
