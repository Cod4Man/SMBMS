package user;
import java.util.Date;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.Role;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.service.role.RoleServiceImpl;
import cn.smbms.service.user.UserService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.ClongUtil;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import cn.smbms.pojo.User;

public class UserServiceTest {
	private UserService userService;
	private ProviderService providerService ;
	@Test
	public void userLogin() {
	    userService = new UserServiceImpl();
	    User user = userService.login("admin", "1234567");
	    System.out.println("====" + user.getAddress());
	}
	@Test
	public void deleteProvider() {
        providerService = new ProviderServiceImpl();
        if (providerService.deleteProviderById("20") >0) {
            System.out.println("删除成功！");
        }
	}
	@Test
	public void getProviderList() {
	    providerService = new ProviderServiceImpl();
	    List<Provider> list = providerService.getProviderList("北京", null);
	    list.forEach(System.out::println);
    System.out.println("长度为： " + list.size());
	}
	@Test
	public void modifyProvider() {
        providerService = new ProviderServiceImpl();
        Provider provider = new Provider();
        provider.setId(20);
        provider.setProCode("FJ_GYS001");
        provider.setProName("王国维");
        provider.setCreatedBy(1);
        provider.setModifyDate(new Date());

        if (providerService.modify(provider) ) {
            System.out.println("修改数据成功！");
        }
	}

	@Test
	public void getProviderById() {
        providerService = new ProviderServiceImpl();
        Provider provider = providerService.getProviderById("1");
        System.out.println("+++++++" + provider);
    }

	@Test
	public void addProvider() {
        providerService = new ProviderServiceImpl();
        Provider provider = providerService.getProviderById("1");
        provider.setId(998);
        try {
            Provider provider1 = ClongUtil.clong(provider);
            if (providerService.add(provider1)) {
                System.out.println("添加成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

	}

	@Test
	public void getRoleList() {
         List<Role> roleList = new RoleServiceImpl().getRoleList();
         roleList.forEach(System.out::println);
    }
	@Before
	public void setUp() throws Exception {
		userService = new UserServiceImpl();
	}

	@Test
	public void testAdd() {
		User user = new User();
		user.setUserCode("111");
		user.setUserName("222");
		boolean result = userService.add(user);
		//result = false;
		//断言
		Assert.assertTrue("增加失败", result);
		
		
	}
	
	@Test 
	public void testGetUserList(){
		List<User> userList = new ArrayList<User>();
		userList = userService.getUserList("",0,1,5);
		Assert.assertEquals(7, userList.size());
	}

}
