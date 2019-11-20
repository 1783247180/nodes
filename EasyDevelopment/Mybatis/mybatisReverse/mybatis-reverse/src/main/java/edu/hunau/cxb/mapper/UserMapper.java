package edu.hunau.cxb.mapper;

import edu.hunau.cxb.pojo.User;
import edu.hunau.cxb.pojo.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface UserMapper {
    @SelectProvider(type=UserSqlProvider.class, method="countByExample")
    long countByExample(UserExample example);

    @DeleteProvider(type=UserSqlProvider.class, method="deleteByExample")
    int deleteByExample(UserExample example);

    @Delete({
        "delete from user",
        "where userid = #{userid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer userid);

    @Insert({
        "insert into user (userid, username, ",
        "userpassword1, usermoney, ",
        "userpassword2)",
        "values (#{userid,jdbcType=INTEGER}, #{username,jdbcType=VARCHAR}, ",
        "#{userpassword1,jdbcType=VARCHAR}, #{usermoney,jdbcType=INTEGER}, ",
        "#{userpassword2,jdbcType=VARCHAR})"
    })
    int insert(User record);

    @InsertProvider(type=UserSqlProvider.class, method="insertSelective")
    int insertSelective(User record);

    @SelectProvider(type=UserSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="userid", property="userid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
        @Result(column="userpassword1", property="userpassword1", jdbcType=JdbcType.VARCHAR),
        @Result(column="usermoney", property="usermoney", jdbcType=JdbcType.INTEGER),
        @Result(column="userpassword2", property="userpassword2", jdbcType=JdbcType.VARCHAR)
    })
    List<User> selectByExample(UserExample example);

    @Select({
        "select",
        "userid, username, userpassword1, usermoney, userpassword2",
        "from user",
        "where userid = #{userid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="userid", property="userid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
        @Result(column="userpassword1", property="userpassword1", jdbcType=JdbcType.VARCHAR),
        @Result(column="usermoney", property="usermoney", jdbcType=JdbcType.INTEGER),
        @Result(column="userpassword2", property="userpassword2", jdbcType=JdbcType.VARCHAR)
    })
    User selectByPrimaryKey(Integer userid);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(User record);

    @Update({
        "update user",
        "set username = #{username,jdbcType=VARCHAR},",
          "userpassword1 = #{userpassword1,jdbcType=VARCHAR},",
          "usermoney = #{usermoney,jdbcType=INTEGER},",
          "userpassword2 = #{userpassword2,jdbcType=VARCHAR}",
        "where userid = #{userid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(User record);
}