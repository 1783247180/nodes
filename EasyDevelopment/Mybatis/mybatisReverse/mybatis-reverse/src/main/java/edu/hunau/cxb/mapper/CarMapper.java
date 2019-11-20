package edu.hunau.cxb.mapper;

import edu.hunau.cxb.pojo.Car;
import edu.hunau.cxb.pojo.CarExample;
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

public interface CarMapper {
    @SelectProvider(type=CarSqlProvider.class, method="countByExample")
    long countByExample(CarExample example);

    @DeleteProvider(type=CarSqlProvider.class, method="deleteByExample")
    int deleteByExample(CarExample example);

    @Delete({
        "delete from car",
        "where carid = #{carid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer carid);

    @Insert({
        "insert into car (carid, userid, ",
        "gname, orderCount, ",
        "gprice)",
        "values (#{carid,jdbcType=INTEGER}, #{userid,jdbcType=INTEGER}, ",
        "#{gname,jdbcType=VARCHAR}, #{ordercount,jdbcType=INTEGER}, ",
        "#{gprice,jdbcType=INTEGER})"
    })
    int insert(Car record);

    @InsertProvider(type=CarSqlProvider.class, method="insertSelective")
    int insertSelective(Car record);

    @SelectProvider(type=CarSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="carid", property="carid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="userid", property="userid", jdbcType=JdbcType.INTEGER),
        @Result(column="gname", property="gname", jdbcType=JdbcType.VARCHAR),
        @Result(column="orderCount", property="ordercount", jdbcType=JdbcType.INTEGER),
        @Result(column="gprice", property="gprice", jdbcType=JdbcType.INTEGER)
    })
    List<Car> selectByExample(CarExample example);

    @Select({
        "select",
        "carid, userid, gname, orderCount, gprice",
        "from car",
        "where carid = #{carid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="carid", property="carid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="userid", property="userid", jdbcType=JdbcType.INTEGER),
        @Result(column="gname", property="gname", jdbcType=JdbcType.VARCHAR),
        @Result(column="orderCount", property="ordercount", jdbcType=JdbcType.INTEGER),
        @Result(column="gprice", property="gprice", jdbcType=JdbcType.INTEGER)
    })
    Car selectByPrimaryKey(Integer carid);

    @UpdateProvider(type=CarSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Car record, @Param("example") CarExample example);

    @UpdateProvider(type=CarSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Car record, @Param("example") CarExample example);

    @UpdateProvider(type=CarSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Car record);

    @Update({
        "update car",
        "set userid = #{userid,jdbcType=INTEGER},",
          "gname = #{gname,jdbcType=VARCHAR},",
          "orderCount = #{ordercount,jdbcType=INTEGER},",
          "gprice = #{gprice,jdbcType=INTEGER}",
        "where carid = #{carid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Car record);
}