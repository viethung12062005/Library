package categoryModel;

public class Category {    // danh mục

	String id;
	String categoryName;
	public Category() {
		super();
	}
	public Category(String id, String categoryName) {
		super();
		this.id = id;
		this.categoryName = categoryName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	@Override
	public String toString() {
		return "CategoryModel [id=" + id + ", categoryName=" + categoryName + "]";
	}
	
	

}
