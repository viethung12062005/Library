package test;

import java.util.ArrayList;

import categoryDao.MysqlCategoryDao;
import categoryModel.Category;



public class testCategory {

public static void main(String[] args) {
	ArrayList<Category> arr=(ArrayList<Category>) MysqlCategoryDao.getInstance().getAll();
	for(Category c : arr) {
		System.out.println(c.toString());
	}
}

}
