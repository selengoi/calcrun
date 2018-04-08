package ru.corp.az.azrun.azcalc.dao;


public class AzCalcData {
 
	private Long     idTask     = null;
	private String   typeName   = "";
    private String   dcName     = "";
    private String   wsName     = "";   
    private String   algName    = "";
    private Long     idTrace    = null;
    private Integer  priority   = null;
		
	public Long getIdTask() {
		return idTask;
	}
	public void setIdTask(Long idTask) {
		this.idTask = idTask;
	}

	public String getDcName() {
		return dcName;
	}
	public void setDcName(String dcName) {
		this.dcName = dcName;
	}
	public String getWsName() {
		return wsName;
	}
	public void setWsName(String wsName) {
		this.wsName = wsName;
	}
	public String getAlgName() {
		return algName;
	}
	public void setAlgName(String algName) {
		this.algName = algName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Long getIdTrace() {
		return idTrace;
	}
	public void setIdTrace(Long idTrace) {
		this.idTrace = idTrace;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

}
