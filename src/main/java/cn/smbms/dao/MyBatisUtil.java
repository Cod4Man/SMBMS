/*
package cn.smbms.dao;

*/
/** MyBatis的SqlSession工具类
 * Created with IntelliJ IDEA.
 *
 * @author 张鸿杰
 * Date：2019-03-21
 * Time:12:11
 *//*

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisUtil {
    private static SqlSessionFactory factory;

    static{//在静态代码块下，factory只会被创建一次
        System.out.println("static factory===============");
        try {
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            factory = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession(){
        return factory.openSession(false);//true 为自动提交事务
    }

    public static void closeSqlSession(SqlSession sqlSession){
        if(null != sqlSession) {
            sqlSession.close();
        }
    }
}
*/
