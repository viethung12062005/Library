package publisherModel;

import java.util.List;

import publisherDao.MysqlPublisherDao;

public class Publisher {

	private String id;
	private String publisherName;
	public Publisher() {
		super();
	}
	public Publisher(String id, String publisherName) {
		super();
		this.id = id;
		this.publisherName = publisherName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPublisherName() {
		return publisherName;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	@Override
	public String toString() {
		 return "PublisherModel{id='" + id + "', publisherName='" + publisherName + "'}";
	}
	

}
