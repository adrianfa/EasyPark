package com.easypark;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ManageOwnerAccountServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	
		boolean duplicate = false;
	 	UserService userService = UserServiceFactory.getUserService();
		User currentUser = userService.getCurrentUser();
	    String account_name = req.getParameter("account");
	    if((currentUser != null) && (account_name != null)) {
	    	OwnerAccount s = new OwnerAccount(currentUser.getUserId(), currentUser.getNickname(), account_name, 0L);
			if (!s.isAlreadyInList(s))
				// persist to datastore
				ofy().save().entity(s).now();
			else
				duplicate = true;
	    }

	    StringBuilder builder = new StringBuilder("/Edit.jsp");
	    if (duplicate) {
	        builder.append("?")
	            .append("duplicate_account")
	            .append("=")
	            .append("1");
	      }
	    
	    resp.sendRedirect(builder.toString());

		
	}

}
