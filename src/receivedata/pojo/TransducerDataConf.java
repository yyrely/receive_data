package receivedata.pojo;

public class TransducerDataConf {
	private Long id;

    private String boardId;

    private String transducerType;

    private String transducerId;

    private String applicationFlag;

    private Double transducerMax;

    private Double transducerMin;
    
    private Double transducerErrornum;

    private Double transducerLevel;

    private Long transducerWarntime;

    private String updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBoardId() {
		return boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public String getTransducerType() {
		return transducerType;
	}

	public void setTransducerType(String transducerType) {
		this.transducerType = transducerType;
	}

	public String getTransducerId() {
		return transducerId;
	}

	public void setTransducerId(String transducerId) {
		this.transducerId = transducerId;
	}

	public String getApplicationFlag() {
		return applicationFlag;
	}

	public void setApplicationFlag(String applicationFlag) {
		this.applicationFlag = applicationFlag;
	}

	public Double getTransducerMax() {
		return transducerMax;
	}

	public void setTransducerMax(Double transducerMax) {
		this.transducerMax = transducerMax;
	}

	public Double getTransducerMin() {
		return transducerMin;
	}

	public void setTransducerMin(Double transducerMin) {
		this.transducerMin = transducerMin;
	}

	public Double getTransducerLevel() {
		return transducerLevel;
	}

	public void setTransducerLevel(Double transducerLevel) {
		this.transducerLevel = transducerLevel;
	}

	public Long getTransducerWarntime() {
		return transducerWarntime;
	}

	public void setTransducerWarntime(Long transducerWarntime) {
		this.transducerWarntime = transducerWarntime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Double getTransducerErrornum() {
		return transducerErrornum;
	}

	public void setTransducerErrornum(Double transducerErrornum) {
		this.transducerErrornum = transducerErrornum;
	}
    
    
}
