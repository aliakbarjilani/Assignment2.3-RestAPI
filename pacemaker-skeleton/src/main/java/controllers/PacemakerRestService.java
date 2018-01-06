package controllers;

import io.javalin.Context;
import models.Activity;
import models.Credentials;
import models.Location;
import models.Message;
import models.User;
import static models.Fixtures.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;

import asg.cliche.Command;

public class PacemakerRestService {

  PacemakerAPI pacemaker = new PacemakerAPI();
  private User loggedInUser = null;
  
  PacemakerRestService() {
    users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
  }

  public void listUsers(Context ctx) {
    ctx.json(pacemaker.getUsers());
    System.out.println("list users requested");
  }
  
  public void createUser(Context ctx) {
    User user = ctx.bodyAsClass(User.class);
    User newUser = pacemaker
        .createUser(user.firstname, user.lastname, user.email, user.password);
    ctx.json(newUser);
    System.out.println(user.email + " Users created");
    }
  
  public void loginUser(Context ctx) {
    Credentials credentials = ctx.bodyAsClass(Credentials.class);

    if (credentials != null) 
    {  
      if (credentials.email != null) 
      { 
        User user = pacemaker.getUserByEmail(credentials.email);
        if (user.getPassword().equals(credentials.password)) 
        {
          loggedInUser = user;
          ctx.json(204); System.out.println( loggedInUser.email + " Logged in");
        } else {ctx.status(404); System.out.println("Error in loging in " + credentials.email);}
      } else {ctx.status(404); System.out.println("Login failed" );}
    }  else {ctx.status(404); System.out.println("Login failed");}

  }

  public void logout(Context ctx) {
    loggedInUser = null;
    ctx.json(204); System.out.println( "User loggedout");
  }

  public void listUser(Context ctx) {
    String id = ctx.param("id");
    ctx.json(pacemaker.getUser(id));
    System.out.println( "User details requested");
  }
  
  // 
  public void getActivities(Context ctx) {
    String id = ctx.param("id");
    User user = pacemaker.getUser(id);
    if (user != null) {
      ctx.json(user.activities.values());
      System.out.println( "User activity details requested");
    } else {
      ctx.status(404);
      System.out.println( "User activity details request failed");
    }
  }

  // addActivity              aa (type, location, distance)
  public void createActivity(Context ctx) {
    String id = ctx.param("id");
    User user = pacemaker.getUser(id);
    if (user != null) {
      Activity activity = ctx.bodyAsClass(Activity.class);
      Activity newActivity = pacemaker
          .createActivity(id, activity.type, activity.location, activity.distance);
      ctx.json(newActivity);
      System.out.println( "User activity added");
    } else {
      ctx.status(404);
      System.out.println( "Add activity failed");
    }
  }
  
  //
  public void getActivity(Context ctx) {
    String id = ctx.param("activityId");
    Activity activity = pacemaker.getActivity(id);
    if (activity != null) {
      ctx.json(activity);
    } else {
      ctx.status(404);
    }
  }
  
  // addLocation              al (activity-id, longitude, latitude)
  public void addLocation(Context ctx) {
    String id = ctx.param("activityId");
    Activity activity = pacemaker.getActivity(id);
    if (activity != null) {
      Location location = ctx.bodyAsClass(Location.class);
      activity.route.add(location);
      ctx.json(activity.route);
    } else {
      ctx.status(404);
    }
  }

  // activityReport           ar
  public void activityReport(Context ctx) {
    if (loggedInUser != null)
    {
      User user = loggedInUser;
      ctx.json(user.activities.values());
      System.out.println( "User activity details requested");
    } else {ctx.status(404); System.out.println("Activity report failed");}
  }
  
  // activityReport           ar (byType: type)
  public void activityReportSorted(Context ctx) {
    if (loggedInUser != null)
    {
      User user = loggedInUser;
      ctx.json(pacemaker.listActivities(loggedInUser.getId(), ctx.param("byType")));
      System.out.println( "User activity (sorted) details requested");
    } else {ctx.status(404); System.out.println("Activity sorted report failed");}
  }
  
  // listActivityLocations    lal (activity-id)
  public void getActivityLocations(Context ctx) {
    String id = ctx.param("activityId");
    Activity activity = pacemaker.getActivity(id);
    if (activity != null) {
      ctx.json(activity.route);
    } else {
      ctx.status(404);
    }
  }
  
  public void deleteUser(Context ctx) {
    String id = ctx.param("id");
    ctx.json(pacemaker.deleteUser(id));
  }

  public void deleteUsers(Context ctx) {
    pacemaker.deleteUsers();
    ctx.json(204);
  }
  
  public void deleteActivities(Context ctx) {
    String id = ctx.param("id");
    pacemaker.deleteActivities(id);
    ctx.json(204);
  }
  
  public void listActivityLocations(Context ctx) {
    String id = ctx.param("activity-id");
    if (id != null)
    {
    ctx.json(pacemaker.listActivityLocations(id));
    System.out.println( "Activity locations listing (lal) requested");
    } else { ctx.json(404); System.out.println( "List activity locations (lal) failed");}
  }
  
  // follow                   f (email)
  public void follow(Context ctx) {
    User user = loggedInUser;
    if (loggedInUser != null)
    {
      //String email = ctx.bodyAsClass(String.class);
      String email = ctx.param("email");
      
      if (email != null) {
        ctx.json(pacemaker.Follow(pacemaker.getUserByEmail(email).email));
        System.out.println( "Follow requested");
      } else {ctx.status(404); System.out.println("Follow request failed : please login");}
    }
  }
  
  // unfollow                 uf (email)  
  public void unfollow(Context ctx) {
    User user = loggedInUser;
    if (loggedInUser != null)
    {
      String email = ctx.param("email");
      
      if (email != null) {
        ctx.json(pacemaker.unFollow(pacemaker.getUserByEmail(email).email));
        System.out.println( "Unfollow requested");
      } else {ctx.status(404); System.out.println("Unfollow request failed : please login");}
    }
  }
  
  // listfriends              lf ()
  public void listFriends(Context ctx) {
    User user = loggedInUser;
    if (loggedInUser != null)
    {
        ctx.json(pacemaker.listFriends());
        System.out.println( "Friends listing requested");
    } else {ctx.status(404); System.out.println("Friends listing request failed : please login");}
  }
  
  // friend-activity-report   far
  // friend-activity-report   far (email)
  //app.get("/far", ctx -> {service.friendActivityReport(ctx);});          // far
  //app.get("/far/:email", ctx -> {service.friendActivityReport(ctx);});   // far (email)
  public void friendActivityReport(Context ctx) {
    User user = loggedInUser;
    String email = ctx.param("email");
    
    if (loggedInUser != null)
    {
      User friend = pacemaker.getFriendByEmail(email);
      if (friend != null)
      {  
        ctx.json(pacemaker.listActivities(friend.getId(), "type"));
        System.out.println( "Friends listing requested");
      } else {ctx.json(pacemaker.listActivities(user.getId(), "type"));}
    } else {ctx.status(404); System.out.println("Friends listing request failed : please login");}
  }
  
  // messageFriend            mf (email, message)
  //app.post("/messageFriend/", ctx -> {service.messageFriend(ctx);});      // 
  public void messageFriend(Context ctx) {
    User user = loggedInUser;
    if (loggedInUser != null){
      Message message = ctx.bodyAsClass(Message.class);
      // check if recipient is a valid friend. getFriendByEmail()
      if (pacemaker.getFriendByEmail(message.recipient)!=null){
        ctx.json(pacemaker.messageFriend(message.getSender(), message.getRecipient(), message.getMessage()));
        System.out.println( "Message friend requested");
      } else {ctx.status(404); System.out.println("Message friend request failed : unknown recipient");}
    } else {ctx.status(404); System.out.println("Message friend request failed : please login");}
  }

  // listMessages             lm
  ////////////////////////////Fallback: Works for logged in user (all sent messages: need to be improved to show only messages for logged in user.
  public void listMessages(Context ctx) {
    User user = loggedInUser;
    if (loggedInUser != null)
    {
      // filter messages for loggedin user..
        List<Message> messageList = new ArrayList<>();
        Collection<Message> userMessages = pacemaker.getMessages();
        ctx.json(userMessages);
        System.out.println( "Message listing requested");
    } else {ctx.status(404); System.out.println("Message listing request failed : please login");}
  }

  //app.get("/listMessages", ctx -> {service.listMessages(ctx);});         // 
  /////////////////////////// Needs improvements. Incomplete.
  /*public void listMessages(Context ctx) {
    User user = loggedInUser;
    if (loggedInUser != null)
    {
      // filter messages for loggedin user..
        //List<Message> messageList = new ArrayList<>();
        //Collection<Message> userMessages = pacemaker.listMessages(loggedInUser.getEmail());
        ctx.json(pacemaker.listMessages(loggedInUser.getEmail()));
        System.out.println( "Message listing requested");
    } else {ctx.status(404); System.out.println("Message listing request failed : please login");}
  }*/

}
