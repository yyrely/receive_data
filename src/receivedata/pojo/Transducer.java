package receivedata.pojo;

import java.util.Date;

public class Transducer {
	private Long id;

    private String applicationFlag;

    private String boardId;

    private String transducerType;

    private String transducerId;

    private Integer transducerStatus;

    private Double transducerNowdata;

    private Date transducerTime;

    private String transducerDescription;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApplicationFlag() {
		return applicationFlag;
	}

	public void setApplicationFlag(String applicationFlag) {
		this.applicationFlag = applicationFlag;
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

	public Integer getTransducerStatus() {
		return transducerStatus;
	}

	public void setTransducerStatus(Integer transducerStatus) {
		this.transducerStatus = transducerStatus;
	}

	public Double getTransducerNowdata() {
		return transducerNowdata;
	}

	public void setTransducerNowdata(Double transducerNowdata) {
		this.transducerNowdata = transducerNowdata;
	}

	public Date getTransducerTime() {
		return transducerTime;
	}

	public void setTransducerTime(Date transducerTime) {
		this.transducerTime = transducerTime;
	}

	public String getTransducerDescription() {
		return transducerDescription;
	}

	public void setTransducerDescription(String transducerDescription) {
		this.transducerDescription = transducerDescription;
	}
    
    
}
