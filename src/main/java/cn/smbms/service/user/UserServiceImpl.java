package cn.smbms.service.user;

import java.util.List;
import java.util.Objects;

import cn.smbms.dao.user.UserDao;
import cn.smbms.pojo.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * service层捕获异常，进行事务处理
 * 事务处理：调用不同dao的多个方法，必须使用同一个connection（connection作为参数传递）
 * 事务完成之后，需要在service层进行connection的关闭，在dao层关闭（PreparedStatement和ResultSet对象）
 * @author Administrator
 *
 */
@Service
@Transactional
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean add(User user) {
		boolean result = false;
		if (userDao.add(user) > 0) {
			result = true;
		}
		return result;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public User login(String userCode, String userPassword) {
		User user = userDao.getLoginUser(userCode);
		if (user != null && Objects.equals(userPassword, user.getUserPassword())) {
			return user;
		}
		return null;
	}

    @Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
		return userDao.getUserList(queryUserName, queryUserRole, new RowBounds((currentPageNo-1)*pageSize, pageSize));
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public int getUserCount(String queryUserName, int queryUserRole) {
		return userDao.getUserCount(queryUserName, queryUserRole);
	}

    @Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public User selectUserCodeExist(String userCode) {
		return userDao.getLoginUser(userCode);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean deleteUserById(Integer delId) {
		boolean result = false;
		if (userDao.deleteUserById(delId) > 0) {
			result = true;
		}
		return result;
	}

	@Override
	public User getUserById(String id) {
		return userDao.getUserById(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean modify(User user) {
		boolean result = false;
		if (userDao.modify(user) > 0) {
			result = true;
		}
		return result;
	}

	@Override
	public boolean updatePwd(int id, String pwd) {
		boolean result = false;
		if (userDao.updatePwd(id, pwd) > 0) {
			result = true;
		}
		return result;
	}


	/*private SqlSession sqlSession;
	public UserServiceImpl(){
	}
	@Override
	public boolean add(User user) {
		boolean flag = false;
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			int updateRows = sqlSession.getMapper(UserDao.class).add(user);
			if(updateRows > 0){
				flag = true;
				System.out.println("add success!");
			}else{
				System.out.println("add failed!");
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
	public User login(String userCode, String userPassword) {
		User user = null;
		try {
		    System.out.println("测试，MyBatisUtil调用前");
            sqlSession = MyBatisUtil.getSqlSession();
			System.out.println("测试");
			user = sqlSession.getMapper(UserDao.class).getLoginUser(userCode);
			//匹配密码

			if(null != user){
				if(!user.getUserPassword().equals(userPassword)) {
					user = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			MyBatisUtil.closeSqlSession(sqlSession);
		}
		return user;
	}
	@Override
	public List<User> getUserList(String queryUserName,int queryUserRole,int currentPageNo, int pageSize) {
		List<User> userList = null;
		System.out.println("queryUserName ---- > " + queryUserName);
		System.out.println("queryUserRole ---- > " + queryUserRole);
		System.out.println("currentPageNo ---- > " + currentPageNo);
		System.out.println("pageSize ---- > " + pageSize);
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			userList = sqlSession.getMapper(UserDao.class).
					getUserList(queryUserName,queryUserRole,new RowBounds((currentPageNo-1)*pageSize,pageSize));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
            MyBatisUtil.closeSqlSession(sqlSession);
		}
		return userList;
	}
	@Override
	public User selectUserCodeExist(String userCode) {
		User user = null;
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			user = sqlSession.getMapper(UserDao.class).getLoginUser(userCode);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			MyBatisUtil.closeSqlSession(sqlSession);
		}
		return user;
	}
	@Override
	public boolean deleteUserById(Integer delId) {
		boolean flag = false;
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			if(sqlSession.getMapper(UserDao.class).deleteUserById(delId) > 0) {
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
	public User getUserById(String id) {
		User user = null;
		try{
			sqlSession = MyBatisUtil.getSqlSession();
			user = sqlSession.getMapper(UserDao.class).getUserById(id);
		}catch (Exception e) {
			e.printStackTrace();
			user = null;
		}finally{
			MyBatisUtil.closeSqlSession(sqlSession);
		}
		return user;
	}
	@Override
	public boolean modify(User user) {
		boolean flag = false;
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			if(sqlSession.getMapper(UserDao.class).modify(user) > 0) {
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
	public boolean updatePwd(int id, String pwd) {
		boolean flag = false;
		try{
			sqlSession = MyBatisUtil.getSqlSession();
			if(sqlSession.getMapper(UserDao.class).updatePwd(id,pwd) > 0) {
                flag = true;
            }
            sqlSession.commit();
		}catch (Exception e) {
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
	public int getUserCount(String queryUserName, int queryUserRole) {
		int count = 0;
		System.out.println("queryUserName ---- > " + queryUserName);
		System.out.println("queryUserRole ---- > " + queryUserRole);
		try {
			sqlSession = MyBatisUtil.getSqlSession();
			count = sqlSession.getMapper(UserDao.class).getUserCount(queryUserName,queryUserRole);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			MyBatisUtil.closeSqlSession(sqlSession);
		}
		return count;
	}*/
	
}
