package evolution.dao.impl;

import java.util.LinkedList;

import org.springframework.stereotype.Repository;

import evolution.dao.AnyDao;

@Repository
public class AnyDaoImpl implements AnyDao {
	@Override
	public <T> LinkedList<T> anyMethod() {
		return null;
	}
}
