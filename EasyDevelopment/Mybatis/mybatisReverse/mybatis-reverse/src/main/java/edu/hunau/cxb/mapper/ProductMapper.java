package edu.hunau.cxb.mapper;

import edu.hunau.cxb.pojo.Product;
import edu.hunau.cxb.pojo.ProductExample;
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

public interface ProductMapper {
    @SelectProvider(type=ProductSqlProvider.class, method="countByExample")
    long countByExample(ProductExample example);

    @DeleteProvider(type=ProductSqlProvider.class, method="deleteByExample")
    int deleteByExample(ProductExample example);

    @Delete({
        "delete from producer",
        "where pid = #{pid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer pid);

    @Insert({
        "insert into producer (pid, pname, ",
        "ppassword1, ppassword2)",
        "values (#{pid,jdbcType=INTEGER}, #{pname,jdbcType=VARCHAR}, ",
        "#{ppassword1,jdbcType=VARCHAR}, #{ppassword2,jdbcType=VARCHAR})"
    })
    int insert(Product record);

    @InsertProvider(type=ProductSqlProvider.class, method="insertSelective")
    int insertSelective(Product record);

    @SelectProvider(type=ProductSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="pid", property="pid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="pname", property="pname", jdbcType=JdbcType.VARCHAR),
        @Result(column="ppassword1", property="ppassword1", jdbcType=JdbcType.VARCHAR),
        @Result(column="ppassword2", property="ppassword2", jdbcType=JdbcType.VARCHAR)
    })
    List<Product> selectByExample(ProductExample example);

    @Select({
        "select",
        "pid, pname, ppassword1, ppassword2",
        "from producer",
        "where pid = #{pid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="pid", property="pid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="pname", property="pname", jdbcType=JdbcType.VARCHAR),
        @Result(column="ppassword1", property="ppassword1", jdbcType=JdbcType.VARCHAR),
        @Result(column="ppassword2", property="ppassword2", jdbcType=JdbcType.VARCHAR)
    })
    Product selectByPrimaryKey(Integer pid);

    @UpdateProvider(type=ProductSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Product record, @Param("example") ProductExample example);

    @UpdateProvider(type=ProductSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Product record, @Param("example") ProductExample example);

    @UpdateProvider(type=ProductSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Product record);

    @Update({
        "update producer",
        "set pname = #{pname,jdbcType=VARCHAR},",
          "ppassword1 = #{ppassword1,jdbcType=VARCHAR},",
          "ppassword2 = #{ppassword2,jdbcType=VARCHAR}",
        "where pid = #{pid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Product record);
}