package evolution.dao.impl;

import org.springframework.stereotype.Repository;

import evolution.dao.AnotherDao;
import evolution.pojo.AnyPojoImpl;

@Repository
public class AnotherDaoImpl implements AnotherDao {
	@Override
	public AnyPojoImpl anyMethod() {
		return null;
	}
}
