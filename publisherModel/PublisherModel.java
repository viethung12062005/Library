package publisherModel;

public class PublisherModel {

	private String id;
	private String publisherName;
	public PublisherModel() {
		super();
	}
	public PublisherModel(String id, String publisherName) {
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
