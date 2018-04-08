package ru.corp.az.azrun.common.taskx;

import java.util.Date;

/*
 * должна обеспечиваться видимость изменений, достигается с помощью volatile
 */
public class TaskDescriptor {
	
	private volatile Integer     id           ;
	private volatile String      name         = "";
	private volatile TaskStatus  status       ;
	private volatile TaskType    type         ;
	private volatile String      action       = "";
	private volatile String      label        = "";
	private volatile Date        startTime    ;
	private volatile Date        lastStartTime;
	private volatile Date        endTime      ;
		
	@Override 
    public boolean equals(Object obj) {
    	if(obj != null && obj instanceof TaskDescriptor && ((TaskDescriptor)obj).id.equals(this.id)) 
    		return true;
    	else
    		return false;
    }
    
    @Override
    public int hashCode() {
    	return new Integer(this.id).hashCode();
    }
    	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public TaskType getType() {
		return type;
	}
	public void setType(TaskType type) {
		this.type = type;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getLastStartTime() {
		return lastStartTime;
	}
	public void setLastStartTime(Date lastStartTime) {
		this.lastStartTime = lastStartTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}	
	
}
