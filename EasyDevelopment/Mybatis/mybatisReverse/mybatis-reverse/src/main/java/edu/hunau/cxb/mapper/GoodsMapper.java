package edu.hunau.cxb.mapper;

import edu.hunau.cxb.pojo.Goods;
import edu.hunau.cxb.pojo.GoodsExample;
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

public interface GoodsMapper {
    @SelectProvider(type=GoodsSqlProvider.class, method="countByExample")
    long countByExample(GoodsExample example);

    @DeleteProvider(type=GoodsSqlProvider.class, method="deleteByExample")
    int deleteByExample(GoodsExample example);

    @Delete({
        "delete from goods",
        "where goodsid = #{goodsid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer goodsid);

    @Insert({
        "insert into goods (goodsid, gname, ",
        "gprice, pid, gcount)",
        "values (#{goodsid,jdbcType=INTEGER}, #{gname,jdbcType=VARCHAR}, ",
        "#{gprice,jdbcType=INTEGER}, #{pid,jdbcType=INTEGER}, #{gcount,jdbcType=INTEGER})"
    })
    int insert(Goods record);

    @InsertProvider(type=GoodsSqlProvider.class, method="insertSelective")
    int insertSelective(Goods record);

    @SelectProvider(type=GoodsSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="gname", property="gname", jdbcType=JdbcType.VARCHAR),
        @Result(column="gprice", property="gprice", jdbcType=JdbcType.INTEGER),
        @Result(column="pid", property="pid", jdbcType=JdbcType.INTEGER),
        @Result(column="gcount", property="gcount", jdbcType=JdbcType.INTEGER)
    })
    List<Goods> selectByExample(GoodsExample example);

    @Select({
        "select",
        "goodsid, gname, gprice, pid, gcount",
        "from goods",
        "where goodsid = #{goodsid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="goodsid", property="goodsid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="gname", property="gname", jdbcType=JdbcType.VARCHAR),
        @Result(column="gprice", property="gprice", jdbcType=JdbcType.INTEGER),
        @Result(column="pid", property="pid", jdbcType=JdbcType.INTEGER),
        @Result(column="gcount", property="gcount", jdbcType=JdbcType.INTEGER)
    })
    Goods selectByPrimaryKey(Integer goodsid);

    @UpdateProvider(type=GoodsSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Goods record, @Param("example") GoodsExample example);

    @UpdateProvider(type=GoodsSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Goods record, @Param("example") GoodsExample example);

    @UpdateProvider(type=GoodsSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Goods record);

    @Update({
        "update goods",
        "set gname = #{gname,jdbcType=VARCHAR},",
          "gprice = #{gprice,jdbcType=INTEGER},",
          "pid = #{pid,jdbcType=INTEGER},",
          "gcount = #{gcount,jdbcType=INTEGER}",
        "where goodsid = #{goodsid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Goods record);
}