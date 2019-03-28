package cn.smbms.service.provider;

import java.util.List;

import cn.smbms.dao.bill.BillDao;
import cn.smbms.dao.provider.ProviderDao;
import cn.smbms.pojo.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProviderServiceImpl implements ProviderService {
	@Autowired
	private ProviderDao providerDao;
	@Autowired
	private BillDao billDao;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean add(Provider provider) {
		boolean result = false;
		if (providerDao.add(provider) > 0 ) {
			result = true;
		}
		return result;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<Provider> getProviderList(String proName, String proCode) {
		return providerDao.getProviderList(proName, proCode);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public int deleteProviderById(String delId) {
		int billCount = -1;
		billCount = billDao.getBillCountByProviderId(delId);
		if (billCount == 0) {
			 providerDao.deleteProviderById(delId);
		}
		return billCount;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Provider getProviderById(String id) {
		return providerDao.getProviderById(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean modify(Provider provider) {
		boolean result = false;
		if (providerDao.modify(provider) > 0 ) {
			result = true;
		}
		return result;
	}

	/*private SqlSession sqlSession = null;
	public ProviderServiceImpl(){
	}
	@Override
	public boolean add(Provider provider) {
		boolean flag = false;
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			if(sqlSession.getMapper(ProviderDao.class).add(provider) > 0) {
				flag = true;
			}
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (sqlSession != null) {
				sqlSession.rollback();
			}
//			try {
//				System.out.println("rollback==================");
//				connection.rollback();
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		}finally{
			//在service层进行connection连接的关闭
			MyBatisUtil.closeSqlSession(sqlSession);
		}
		return flag;
	}

	@Override
	public List<Provider> getProviderList(String proName,String proCode) {
		List<Provider> providerList = null;
		System.out.println("query proName ---- > " + proName);
		System.out.println("query proCode ---- > " + proCode);
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			providerList = sqlSession.getMapper(ProviderDao.class).getProviderList(proName, proCode);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (sqlSession != null) {
				sqlSession.rollback();
			}
		}finally{
			MyBatisUtil.closeSqlSession(sqlSession);
		}
		return providerList;
	}

	*//**
	 * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
	 * 若订单表中无该供应商的订单数据，则可以删除
	 * 若有该供应商的订单数据，则不可以删除
	 * 返回值billCount
	 * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
	 * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
	 * 
	 * ---判断
	 * 如果billCount = -1 失败
	 * 若billCount >= 0 成功
	 *//*
	@Override
	public int deleteProviderById(String delId) {
		int billCount = -1;
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			billCount = sqlSession.getMapper(BillDao.class).getBillCountByProviderId(delId);
			if(billCount == 0){
				sqlSession.getMapper(ProviderDao.class).deleteProviderById(delId);
//				throw new RuntimeException("测试回滚");
			}
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			billCount = -1;
            if (sqlSession != null) {
                sqlSession.rollback();
            }
		}finally{
			MyBatisUtil.closeSqlSession(sqlSession);
		}
		return billCount;
	}

	@Override
	public Provider getProviderById(String id) {
		Provider provider = null;
		try{
			sqlSession = MyBatisUtil.getSqlSession();
			provider = sqlSession.getMapper(ProviderDao.class).getProviderById(id);
			sqlSession.commit();
		}catch (Exception e) {
			e.printStackTrace();
			provider = null;
			if (sqlSession != null) {
				sqlSession.rollback();
			}
		}finally{
			MyBatisUtil.closeSqlSession(sqlSession);
		}
		return provider;
	}

	@Override
	public boolean modify(Provider provider) {
		boolean flag = false;
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			if(sqlSession.getMapper(ProviderDao.class).modify(provider) > 0) {
				flag = true;
			}
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (sqlSession != null) {
				sqlSession.rollback();
			}
		}finally{
			MyBatisUtil.closeSqlSession(sqlSession);
		}
		return flag;
	}*/

}
