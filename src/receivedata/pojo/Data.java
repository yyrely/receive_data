package receivedata.pojo;

import java.util.Date;

public class Data {
	private String transducerId;

    private String boardId;
    
	private String transducerType;

    private Double transducerData;

    private Date dataTime;

	public String getTransducerId() {
		return transducerId;
	}

	public void setTransducerId(String transducerId) {
		this.transducerId = transducerId;
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

	public Double getTransducerData() {
		return transducerData;
	}

	public void setTransducerData(Double transducerData) {
		this.transducerData = transducerData;
	}

	public Date getDataTime() {
		return dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}
    
    
    
    
}
