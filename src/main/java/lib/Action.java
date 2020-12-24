package lib;

import java.io.Serializable;

public class Action implements Serializable {
	
	private static final long serialVersionUID = -5277267620567763963L;
	private Operation type;
	private Object target;
	
	public Action(Operation type, Object target) {
		this.type = type;
		this.target = target;
	}
		
	public Operation getOperation() {
		return type;
	}

	public void setOperation(Operation type) {
		this.type = type;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}
	
	public void setAction(Operation type, Object target) {
		this.type = type;
		this.target = target;
	}

	@Override
	public String toString() {
		return "Action [operation=" + type + ", target=" + target + "]";
	}	
}
