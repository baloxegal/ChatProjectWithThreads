package server;

import java.net.Socket;
import lib.Connection;
import lib.Action;
import lib.Operation;
import lib.User;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.io.IOException;

public class ChatApplicationServer {
	
	public static final Integer LOCAL_PORT = 7777;
	public static Map <Socket, User> users = new HashMap<Socket, User>();
	public static Connection conn;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {		
		conn.run();
		Connection conn = new Connection(LOCAL_PORT);
		Executor execution = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
		
		Action action = new Action(null, null);
		do{
			execution.execute(conn = );
			if(users.putIfAbsent(conn.getSocket(), null) == null)
				conn.send();
		}while(true);
		
				
		
		Action action = (Action) conn.fetch();
		
		if(action.getOperation().equals(Operation.SIGN_IN)) {
			if(users.putIfAbsent(conn, (User)action.getTarget()) == null)
				conn.send(new Action(Operation.SUCCESS, null));
			else
				conn.send(new Action(Operation.ALREADY_CONNECTED, null));			
		}
		
		action = (Action) conn.fetch();
				
		if(action.getOperation().equals(Operation.USER_LIST))
			conn.send(new Action(Operation.SUCCESS, users));
	}
}
