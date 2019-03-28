package cn.smbms.dao.user;

import java.sql.Connection;
import java.util.List;

import cn.smbms.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UserDao {
	/**
	 * 增加用户信息
	 * @param connection
	 * @param user
	 * @return
	 * @
	 */
	public int add(User user);

	/**
	 * 通过userCode获取User
	 * @param connection
	 * @param userCode
	 * @return
	 * @
	 */
	public User getLoginUser(@Param("userCode") String userCode);

	/**
	 * 通过条件查询-userList
	 * @param connection
	 * @param userName
	 * @param userRole
	 * @return
	 * @
	 */
	public List<User> getUserList(
			@Param("userName") String userName,
			@Param("userRole") int userRole,
			RowBounds rowBounds);
	/**
	 * 通过条件查询-用户表记录数
	 * @param connection
	 * @param userName
	 * @param userRole
	 * @return
	 * @
	 */
	public int getUserCount(@Param("userName") String userName,
							@Param("userRole") int userRole);
	
	/**
	 * 通过userId删除user
	 * @param delId
	 * @return
	 * @
	 */
	public int deleteUserById(@Param("delId") Integer delId);
	
	
	/**
	 * 通过userId获取user
	 * @param connection
	 * @param id
	 * @return
	 * @
	 */
	public User getUserById(@Param("id") String id);
	
	/**
	 * 修改用户信息
	 * @param connection
	 * @param user
	 * @return
	 * @
	 */
	public int modify(User user);
	
	
	/**
	 * 修改当前用户密码
	 * @param connection
	 * @param id
	 * @param pwd
	 * @return
	 * @
	 */
	public int updatePwd(@Param("id") int id,
						 @Param("pwd") String pwd);
	
	
}
