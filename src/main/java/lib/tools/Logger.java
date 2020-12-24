package lib.tools;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.JoinPoint;
import lib.Action;
import lib.Operation;

@Aspect
public class Logger {
	
	@Before("execution(* client.ChatApplicationClient.main(..))")
	public void LogBeforStartClient() {
		
		System.out.println("Client Starting");
		
	}
	
	@Before("execution(* server.ChatApplicationServer.main(..))")
	public void LogBeforStartServer() {
		
		System.out.println("Server Starting");
		
	}
	
	@AfterReturning(value = "call(* fetch())", returning = "transferredData")
	public void LogAfterReadingData(JoinPoint jp, Action transferredData) {			
		if(jp.getSourceLocation().getWithinType().equals(server.ChatApplicationServer.class))
			System.out.println("Client sends: " + transferredData);
		else if(jp.getSourceLocation().getWithinType().equals(client.ChatApplicationClient.class)) {
			System.out.println("Server sends: " + transferredData);
			if(transferredData.getOperation().equals(Operation.SUCCESS))
				if(transferredData.getTarget() != null)
					System.out.println("User map: " + transferredData.getTarget());
				else
					System.out.println("Signed in!");
			else if(transferredData.getOperation().equals(Operation.ALREADY_CONNECTED))
				System.out.println("You are already signed in!");					
		}		
	}	
	
	@AfterReturning(value = "call(* putIfAbsent(..))", returning = "autorizationState")
	public void LogServerAfterAutorization(Object autorizationState) {			
		if(autorizationState == null)
			System.out.println("Client is signed in!");
		else
			System.out.println("Refuse! Client is already signed in!");		
	}	
	
	@After("execution(* client.ChatApplicationClient.main(..))")
	public void LogAfterEndClient() {
		
		System.out.println("Client Ending");
		
	}	
	
	@After("execution(* server.ChatApplicationServer.main(..))")
	public void LogAfterEndServer() {
		
		System.out.println("Server Ending");
		
	}	
}
