package bookcopyModel;

import publisherModel.Publisher;

public class BookcopyModel {

	private String id;
	private String copyNumber;
	private String title;
	private String author;
	private String publisher;
	private double price;
	private String category;
	private int status;
	private String type;
	
	
	
	  private BookcopyModel(BookcopyBuilder builder) {
	        this.id = builder.id;
	        this.copyNumber = builder.copyNumber;
	        this.title = builder.title;
	        this.author = builder.author;
	        this.publisher = builder.publisher;
	        this.price = builder.price;
	        this.category = builder.category;
	        this.status = builder.status;
	        this.type = builder.type;
	    }
	
	  
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getCopyNumber() {
		return copyNumber;
	}



	public void setCopyNumber(String copyNumber) {
		this.copyNumber = copyNumber;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getAuthor() {
		return author;
	}



	public void setAuthor(String author) {
		this.author = author;
	}



	public String getPublisher() {
		return publisher;
	}



	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}



	public double getPrice() {
		return price;
	}



	public void setPrice(double price) {
		this.price = price;
	}



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
	}



	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public static class BookcopyBuilder{
		private String id;
		private String copyNumber;
		private String title;
		private String author;
		private String publisher;
		private double price;
		private String category;
		private int status;
		private String type;
		
		public BookcopyBuilder setId(String id) {
			this.id=id;
			return this;
		}
		
		public BookcopyBuilder setCopyNumber(String copyNumber) {
			this.copyNumber=copyNumber;
			return this;
		}

        public BookcopyBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public BookcopyBuilder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public BookcopyBuilder setPublisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public BookcopyBuilder setPrice(double price) {
            this.price = price;
            return this;
        }

        public BookcopyBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public BookcopyBuilder setStatus(int status) {
            this.status = status;
            return this;
        }

        public BookcopyBuilder setType(String type) {
            this.type = type;  
            return this;
        }
        
        public BookcopyModel build() {
            return new BookcopyModel(this);
        }

        
	}

}
