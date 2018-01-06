package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import asg.cliche.Param;
import models.Activity;
import models.Location;
import models.User;
import models.Message;

public class PacemakerAPI {

  private Map<String, Message> messageIndex = new HashMap<>();
  private Map<String, Message> recipientIndex = new HashMap<>();
  private Map<String, User> friendIndex = new HashMap<>();
  private Map<String, User> emailIndex = new HashMap<>();
  private Map<String, User> userIndex = new HashMap<>();
  private Map<String, Activity> activitiesIndex = new HashMap<>();

  public PacemakerAPI() 
  {
  
  }
  
  public Collection<User> getUsers() 
  {
    return userIndex.values();
  }

  public void deleteUsers() 
  {
    userIndex.clear();
    emailIndex.clear();
  }

  public User createUser(String firstName, String lastName, String email, String password) 
  {
    User user = new User(firstName, lastName, email, password);
    emailIndex.put(email, user);
    userIndex.put(user.id, user);
    return user;
  }
  
  // lu (login-User)
  
  // l  (log-out)
  
  public Activity createActivity(String id, String type, String location, double distance) 
  {
    Activity activity = null;
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if (user.isPresent()) {
      activity = new Activity(type, location, distance);
      user.get().activities.put(activity.id, activity);
      activitiesIndex.put(activity.id, activity);
    }
    return activity;
  }

  public Activity getActivity(String id) 
  {
    return activitiesIndex.get(id);
  }

  public Collection<Activity> getActivities(String id) {
    Collection<Activity> activities = null;
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if (user.isPresent()) {
      activities = user.get().activities.values();
    }
    return activities;
  }

  public List<Activity> listActivities(String userId, String sortBy) 
  {
    List<Activity> activities = new ArrayList<>();
    activities.addAll(userIndex.get(userId).activities.values());
    switch (sortBy) 
    {
      case "type":
        activities.sort((a1, a2) -> a1.type.compareTo(a2.type));
        break;
      case "location":
        activities.sort((a1, a2) -> a1.location.compareTo(a2.location));
        break;
      case "distance":
        activities.sort((a1, a2) -> Double.compare(a1.distance, a2.distance));
        break;
    }
    return activities;
  }

  public void addLocation(String id, double latitude, double longitude) 
  {
    Optional<Activity> activity = Optional.fromNullable(activitiesIndex.get(id));
    if (activity.isPresent()) 
    {
      activity.get().route.add(new Location(latitude, longitude));
    }
  }

  public User getUserByEmail(String email) 
  {
    return emailIndex.get(email);
  }
  
  public User getFriendByEmail(String email) 
  {
    return friendIndex.get(email);
  }

  public User getUser(String id) 
  {
    return userIndex.get(id);
  }
  
  public User deleteUser(String id) 
  {
    User user = userIndex.remove(id);
    return emailIndex.remove(user.email);
  }
  
  public void deleteActivities(String id) {
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if (user.isPresent()) {
      user.get().activities.values().forEach(activity -> activitiesIndex.remove(activity.getId()));
      user.get().activities.clear();
    }
  }
  
  public List<Location> listActivityLocations(String id) {
    return activitiesIndex.get(id).route;
  }

  public User Follow(String email)
  {
    User friend = emailIndex.get(email);
    friendIndex.put(email, friend);
    return friend;
  }
  
  public Collection<User> listFriends()
  {
    return friendIndex.values();
  }

  public User unFollow(String email)
  {
    User friend = emailIndex.get(email);
    return friendIndex.remove(friend.email);
  }
  
  public Collection<Message> getMessages() 
  {
    return messageIndex.values();
  }
  
  public void deleteMessages() 
  {
    messageIndex.clear();
    recipientIndex.clear();
  }

  public Message messageFriend(String sender, String recipient,  String message) 
  {
    Message msg = new Message(sender, recipient, message, "");
    messageIndex.put(msg.id, msg);
    recipientIndex.put(recipient, msg);
    return msg;
  }
  
  // Keep/ Delete?
  public Message getMessageByRecipient(String recipientemail) 
  {
    return recipientIndex.get(recipientemail);
  }

  public Message getMessage(String id) 
  {
    return recipientIndex.get(id);
  }
  
  public Message deleteMessage(String id) 
  {
    Message message = messageIndex.remove(id);
    return recipientIndex.remove(message.id);
  }

  public List<Message> listMessages(String recipientemail)
  {

/*      List<Activity> reportActivities = new ArrayList<>();
        Collection<Activity> usersActivities = paceApi.getActivities(user.get().id);
        usersActivities.forEach(a -> {if (a.type.equals(type)) reportActivities.add(a);});
        reportActivities.sort((a1, a2) -> {if (a1.distance >= a2.distance) return -1; else return 1; });
        console.renderActivities(reportActivities);*/
    // for each item, add to collection if it is for recipientemail
    //Collection<Message> recipientMessages = null;
    List<Message> messageArray = null;
    recipientIndex.values().forEach(a -> {if (a.recipient.equals(recipientemail)) messageArray.add(a);});
    return messageArray;
    
    // Collection<Activity> activities = null;
    // activities = user.get().activities.values();
    
    //return messageIndex.values();
  }  

}