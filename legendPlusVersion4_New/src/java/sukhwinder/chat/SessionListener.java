package sukhwinder.chat;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

/**
It is used by Chat Application for listening to session events.
* @author Sukhwinder Singh
*/
public class SessionListener implements HttpSessionAttributeListener 
{  
	public void attributeAdded(HttpSessionBindingEvent hsbe)
	{
		//System.out.println("Attribute has been bound");
	}

	public void attributeReplaced(HttpSessionBindingEvent hsbe)
	{
		//System.out.println("Attribute has been replaced");
	}

	/** This is the method we are interested in. It deletes a user from this list of users when his session
		expires.
	*/
	public void attributeRemoved(HttpSessionBindingEvent hsbe)
	{
		System.out.println("--------------SessionListener--------------");     
		String name = hsbe.getName();
//		System.out.println("----------name-------------"+name); 
		HttpSession session = hsbe.getSession();
		String nickname = (String)session.getAttribute("nickname");
		 
			if (nickname != null)
			{
//				System.out.println("----------nickname-------------"+nickname); 
				ServletContext application = session.getServletContext(); 
				if (application != null)
				{
//					System.out.println("--------------application--------------"); 
					Object o = application.getAttribute("chatroomlist");		
					if (o != null)
					{
//						System.out.println("--------------0--------------"); 
						ChatRoomList roomList = (ChatRoomList)o;
						ChatRoom room = roomList.getRoomOfChatter(nickname);
						if (room != null)
						{
//							System.out.println("--------------room--------------"); 
							Object chatter = room.removeChatter(nickname);
//							System.out.println("--------------room-2-------------"); 
							if (chatter != null)
							{
//								System.out.println("--------------chatter-------------"); 
								String n = ((Chatter)chatter).getName();
							}
							
						}
					}
				}
				else
				{
					System.out.println("ServletContext is null");
				}					
			}
		 		
	}
}