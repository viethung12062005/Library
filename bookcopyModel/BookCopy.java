package bookCopyModel;

import publisherModel.Publisher;

public class BookCopy {

	private String id;
	private String copyNumber;
	private String title;
	private String author;
	private String publisher;
	private double price;
	private String category;
	private int status;
	private String type;
	
	
	
	  private BookCopy(BookCopyBuilder builder) {
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





	public String getCopyNumber() {
		return copyNumber;
	}





	public String getTitle() {
		return title;
	}





	public String getAuthor() {
		return author;
	}





	public String getPublisher() {
		return publisher;
	}





	public double getPrice() {
		return price;
	}





	public String getCategory() {
		return category;
	}





	public int getStatus() {
		return status;
	}





	public String getType() {
		return type;
	}





	public static class BookCopyBuilder{
		private String id;
		private String copyNumber;
		private String title;
		private String author;
		private String publisher;
		private double price;
		private String category;
		private int status;
		private String type;
		
		public BookCopyBuilder setId(String id) {
			this.id=id;
			return this;
		}
		
		public BookCopyBuilder setCopyNumber(String copyNumber) {
			this.copyNumber=copyNumber;
			return this;
		}

        public BookCopyBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public BookCopyBuilder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public BookCopyBuilder setPublisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public BookCopyBuilder setPrice(double price) {
            this.price = price;
            return this;
        }

        public BookCopyBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public BookCopyBuilder setStatus(int status) {
            this.status = status;
            return this;
        }

        public BookCopyBuilder setType(String type) {
            this.type = type;  
            return this;
        }
        
        public BookCopy build() {
            return new BookCopy(this);
        }

        
	}

}
