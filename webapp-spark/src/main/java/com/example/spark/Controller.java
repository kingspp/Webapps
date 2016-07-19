package com.example.spark;

import com.example.spark.config.Configuration;
import com.example.spark.pojo.Response;
import com.example.spark.pojo.User;
import com.example.spark.service.UserService;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;
import spark.Request;

import java.io.FileNotFoundException;
import java.util.List;


/**
 * Created by prathyushsp on 18/07/16.
 */
public class Controller {

    final static Logger logger = LoggerFactory.getLogger(Controller.class);

    public static void main(String args[]){
        if (args.length != 1) {
            System.err.println("Environment not specified.\nUsage: \n\tjava -jar app.jar <environment>");
            System.exit(1);
        }
        new Configuration(args[0]);
        Spark.port(Integer.parseInt(Configuration.getInstance().getProperty(Configuration.ConfigKey.WEBSPARK_PORT)));
        Spark.staticFileLocation(Configuration.getInstance().getProperty(Configuration.ConfigKey.WEBSPARK_STATICFILES));

        Spark.before("/*", (request, response) -> {
            if (request.uri().equals("/")) {
                logger.info(request.uri() + " - Is / - Redirecting to index.html");
                response.redirect(request.uri() + "login.html");
                return;
            }
            if (shouldIgnoreRequestFilter(request, new String[]{"css", "js", "images", "login"})) {
                return;
            } else {
                logger.info(request.uri() + " - Not / - Redirecting to " + request.uri());
                Object userID = request.session().attribute(Configuration.userSessionKey);
                logger.info("UserID: " + userID);
                logger.info("Session ID: " + request.session().id());
                if (userID == null) Spark.halt(401, "You are not welcome here <a href='/login.html'>Login</a><script>window.location.href='/login.html'</script>");
                else
                    return;
            }
        });

        Spark.post("/login", (req, res) -> {
            String username = req.queryParams("name");
            String password = req.queryParams("password");
            String status = "Login Failed";
            List<User> list = UserService.getUserList();
            for (User usr : list) {
                if (usr.getName().equals(username) && usr.getPassword().equals(password)) {
                    spark.Session session = req.session(true);
                    session.attribute(Configuration.userSessionKey, usr.getId());
                    status = "Login Success";
                }
            }
            Response resp = new Response();
            resp.setResult(status);
            return new GsonBuilder().create().toJson(resp);
        });

        Spark.get("/hello", (req,res) -> "Hello World");
    }


    private static boolean shouldIgnoreRequestFilter(Request request, String[] ignoreList) {
        for (String i : ignoreList)
            if (request.uri().toLowerCase().contains(i.toLowerCase()))
                return true;
        return false;
    }
}
