package controllers;

import io.javalin.Javalin;

public class RestMain {

  public static void main(String[] args) throws Exception {
    Javalin app = Javalin.create();
    app.port(getAssignedPort());
    app.start();
    PacemakerRestService service = new PacemakerRestService();
    configRoutes(app, service);
  }
  
  static void configRoutes(Javalin app, PacemakerRestService service) {

    // listUsers                gu ()
    app.get("/users", ctx -> {service.listUsers(ctx);});    // gu
    
    // register                 ru (firstname, lastname, email, password)
    app.post("/users", ctx -> {service.createUser(ctx);});  // ru
     
    // lu (login-User)
    app.post("/login/credentials", ctx -> {service.loginUser(ctx);});
    
    // l  (log-out)
    app.post("/logout", ctx -> {service.logout(ctx);});
    
    //
    app.get("/users/:id", ctx -> {service.listUser(ctx);}); // tested
    
    // listActivities           la
    app.get("/users/:id/activities", ctx -> {service.getActivities(ctx);});   // tested

    // addActivity              aa (type, location, distance)
    app.post("/users/:id/activities", ctx -> {service.createActivity(ctx);}); // tested 

    //
    app.get("/users/:id/activities/:activityId", ctx -> {service.getActivity(ctx);}); //

    // addLocation              al (activity-id, longitude, latitude)
    app.post("/users/activities/:activityId/", ctx -> {service.addLocation(ctx);});   // tested

    // listActivityLocations    lal (activity-id)
    app.get("/users/activities/:activityId/locations", ctx -> {service.listActivityLocations(ctx);});  // tested
    
    // activityReport           ar
    app.get("/activityReport",  ctx -> {service.activityReport(ctx);});    // tested
    
    // activityReport           ar (byType: type)
    app.get("/activityReport/:byType/",  ctx -> {service.activityReportSorted(ctx);});  // tested
    
    // follow                   f (email)
    app.post("/users/follow/:email",  ctx -> {service.follow(ctx);});      // tested
    
    // listfriends              lf ()
    app.get("/listfriends",  ctx -> {service.listFriends(ctx);});          // tested
    
    // unfollow                 uf (email)    
    app.post("/users/unfollow/:email",  ctx -> {service.unfollow(ctx);});  // tested 
    
    // friend-activity-report   far
    app.get("/far", ctx -> {service.friendActivityReport(ctx);});          // tested

    // friend-activity-report   far (email)
    app.get("/far/:email", ctx -> {service.friendActivityReport(ctx);});   // tested
    
    // messageFriend            mf (email, message)
    app.post("/messageFriend/", ctx -> {service.messageFriend(ctx);});     // tested
    
    // listMessages             lm
    app.get("/listMessages", ctx -> {service.listMessages(ctx);});         // working
    
    // distanceLeaderBoard      dlb
    app.get("/users", ctx -> {service.deleteUsers(ctx);});                 // 
    
    // distanceLeaderBoardByType(byType: type)
    app.get("/users", ctx -> {service.deleteUsers(ctx);});                 // 
    
    // messageAllFriends        (message)
    app.post("/users", ctx -> {service.deleteUsers(ctx);});                // 
    
    // locationLeaderBoard      llb (location)
    app.get("/users", ctx -> {service.deleteUsers(ctx);});                 // 

    app.delete("/users", ctx -> {service.deleteUsers(ctx);});              // tested

    //
    app.delete("/users/:id", ctx -> {service.deleteUser(ctx);});           // tested

    //
    app.delete("/users/:id/activities", ctx -> {service.deleteActivities(ctx);}); // tested
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /*
     * register                 ru (firstname, lastname, email, password)             Done
     * listUsers                gu ()                                                 Done
     * login                    l (email, password)                                   Done
     * logout                   l                                                     Done
     * addActivity              aa (type, location, distance)                         Done
     * listActivities           la                                                    Not in Scope
     * addLocation              al (activity-id, longitude, latitude)                 Done
     * activityReport           ar                                                    Done
     * activityReport           ar (byType: type/distance/location)                   Done
     * listActivityLocations    lal (activity-id)                                     Done
     * followfriend             f (email)                                             Done
     * unfollowFriend           uf (email)                                            Done
     * listFriends              lf                                                    Done
     * friend-activity-report   far (email)                                           Done
     * friend-activity-report   far                                                   Done
     * messageFriend            mf (email, message)                                   Done
     * listMessages             lm                                                    Working
     * distanceLeaderBoard      dlb
     * distanceLeaderBoardByType(byType: type)
     * messageAllFriends        (message)
     * locationLeaderBoard      llb (location)
     */
 
    
     
    app.get("/users",  ctx -> {});  // ar activity-report()
     
     // login                    l (email, password)
     // logout                   l
     // addActivity              aa (type, location, distance)
     // listActivities           la                                                    Not in scope
     // addLocation              al (activity-id, longitude, latitude)
     // activityReport           ar
     // activityReport           ar (byType: type)
     // listActivityLocations    lal (activity-id)
     // followfriend             f (email)
     // unfollowFriend           uf (email)
     // listFriends              lf
     // friend-activity-report   far (email)
     // friend-activity-report   far
     // messageFriend            mf (email, message)
     // listMessages             lm
     // distanceLeaderBoard      dlb
     // distanceLeaderBoardByType(byType: type)
     // messageAllFriends        (message)
     // locationLeaderBoard      llb (location)
     
  }

  private static int getAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 7000;
  }
  
}
