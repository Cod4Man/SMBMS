package cn.smbms.service.role;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.smbms.dao.role.RoleDao;
import cn.smbms.pojo.Role;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleDao roleDao;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Role> getRoleList() {
        return roleDao.getRoleList();
    }

	/*private SqlSession sqlSession;
	@Override
	public List<Role> getRoleList() {
        List<Role> roleList = new ArrayList<>();
        sqlSession = MyBatisUtil.getSqlSession();
        try {
             roleList = sqlSession.getMapper(RoleDao.class).getRoleList();
             sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (sqlSession != null) {
                sqlSession.rollback();
            }
        } finally {
            if (sqlSession != null) {
                MyBatisUtil.closeSqlSession(sqlSession);
            }
        }
		return roleList;
	}*/
}
