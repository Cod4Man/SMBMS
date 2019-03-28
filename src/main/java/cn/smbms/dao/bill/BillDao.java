package cn.smbms.dao.bill;

import java.sql.Connection;
import java.util.List;

import cn.smbms.pojo.Bill;
import org.apache.ibatis.annotations.Param;

public interface BillDao {
	/**
	 * 增加订单
	 * @param connection
	 * @param bill
	 * @return
	 * @
	 */
	public int add(Bill bill);


	/**
	 * 通过查询条件获取供应商列表-模糊查询-getBillList
	 * @param connection
	 * @param bill
	 * @return
	 * @
	 */
	public List<Bill> getBillList(Bill bill);
	
	/**
	 * 通过delId删除Bill
	 * @param connection
	 * @param delId
	 * @return
	 * @
	 */
	public int deleteBillById(@Param("delId") String delId);
	
	
	/**
	 * 通过billId获取Bill
	 * @param connection
	 * @param id
	 * @return
	 * @
	 */
	public Bill getBillById(@Param("id") String id);
	
	/**
	 * 修改订单信息
	 * @param connection
	 * @param bill
	 * @return
	 * @
	 */
	public int modify(Bill bill);

	/**
	 * 根据供应商ID查询订单数量
	 * @param connection
	 * @param providerId
	 * @return
	 * @
	 */
	public int getBillCountByProviderId(@Param("providerId") String providerId);

}
