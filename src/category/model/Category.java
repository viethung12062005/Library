package category.model;

import category.dao.CategoryDao;
import category.dao.MysqlCategoryDao;

import java.util.List;

public class Category {

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat() {
        return category;
    }

    public void setCat(String category) {
        this.category = category;
    }

    public Category() {
    }

    public Category(String id, String category) {
        this.id = id;
        this.category = category;
    }
    String category;

    public List<Category> getAll() {
        return cardDao().getAll();
    }

    private CategoryDao cardDao() {
        return MysqlCategoryDao.getInstance();
    }
}
