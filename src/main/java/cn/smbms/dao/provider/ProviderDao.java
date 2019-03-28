package cn.smbms.dao.provider;

import java.sql.Connection;
import java.util.List;
import cn.smbms.pojo.Provider;
import org.apache.ibatis.annotations.Param;

public interface ProviderDao {
	
	/**
	 * 增加供应商
	 * @param connection
	 * @param provider
	 * @return
	 * @
	 */
	public int add(Provider provider);


	/**
	 * 通过供应商名称、编码获取供应商列表-模糊查询-providerList
	 * @param connection
	 * @param proName
	 * @return
	 * @
	 */
	public List<Provider> getProviderList(@Param("proName") String proName,
										  @Param("proCode") String proCode);
	
	/**
	 * 通过proId删除Provider
	 * @param delId
	 * @return
	 * @
	 */
	public int deleteProviderById(@Param("delId") String delId);
	
	
	/**
	 * 通过proId获取Provider
	 * @param connection
	 * @param id
	 * @return
	 * @
	 */
	public Provider getProviderById(@Param("id") String id);
	
	/**
	 * 修改用户信息
	 * @param connection
	 * @param user
	 * @return
	 * @
	 */
	public int modify(Provider provider);
	
	
}
