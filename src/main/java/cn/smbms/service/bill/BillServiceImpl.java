package cn.smbms.service.bill;

import java.util.List;

import cn.smbms.dao.bill.BillDao;
import cn.smbms.pojo.Bill;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BillServiceImpl implements BillService {
	@Autowired
	private BillDao billDao;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean add(Bill bill) {
		boolean result = false;
		if (billDao.add(bill) > 0) {
			result = true;
		}
		return result;
	}

    @Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<Bill> getBillList(Bill bill) {
		return billDao.getBillList(bill);
	}

    @Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean deleteBillById(String delId) {
		boolean result = false;
		if (billDao.deleteBillById(delId) > 0) {
			result = true;
		}
		return result;
	}

	@Override
    @Transactional(propagation = Propagation.SUPPORTS)
	public Bill getBillById(String id) {
		return billDao.getBillById(id);
	}

	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public boolean modify(Bill bill) {
		boolean result = false;
		if (billDao.modify(bill) > 0) {
			result = true;
		}
		return result;
	}
/*
	private SqlSession sqlSession;
	public BillServiceImpl(){
	}
	@Override
	public boolean add(Bill bill) {
		boolean flag = false;
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			if(sqlSession.getMapper(BillDao.class).add(bill) > 0) {
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
	}

	@Override
	public List<Bill> getBillList(Bill bill) {
		List<Bill> billList = null;
		System.out.println("query productName ---- > " + bill.getProductName());
		System.out.println("query providerId ---- > " + bill.getProviderId());
		System.out.println("query isPayment ---- > " + bill.getIsPayment());
		
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			billList = sqlSession.getMapper(BillDao.class).getBillList( bill);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (sqlSession != null) {
				sqlSession.rollback();
			}
		}finally{
			MyBatisUtil.closeSqlSession(sqlSession);
		}
		return billList;
	}

	@Override
	public boolean deleteBillById(String delId) {
		boolean flag = false;
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			if(sqlSession.getMapper(BillDao.class).deleteBillById(delId) > 0) {
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
	}

	@Override
	public Bill getBillById(String id) {
		Bill bill = null;
		try{
			sqlSession = MyBatisUtil.getSqlSession();
			bill = sqlSession.getMapper(BillDao.class).getBillById(id);
			sqlSession.commit();
		}catch (Exception e) {
			e.printStackTrace();
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			bill = null;
		}finally{
			MyBatisUtil.closeSqlSession(sqlSession);
		}
		return bill;
	}

	@Override
	public boolean modify(Bill bill) {
		boolean flag = false;
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			if(sqlSession.getMapper(BillDao.class).modify(bill) > 0) {
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
	}
*/

}
