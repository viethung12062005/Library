package test;

import java.util.ArrayList;

import publisherDao.MysqlPublisherDao;
import publisherModel.Publisher;

public class TestPublisher {

	public static void main(String[] args) {
		ArrayList<Publisher> arr=(ArrayList<Publisher>) MysqlPublisherDao.getInstance().getAll();
		for(Publisher pub : arr) {
			System.out.println(pub.toString());
		}
	}

}
