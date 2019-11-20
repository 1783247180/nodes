package edu.hunau.cxb.mapper;

import edu.hunau.cxb.pojo.Goods;
import edu.hunau.cxb.pojo.GoodsExample.Criteria;
import edu.hunau.cxb.pojo.GoodsExample.Criterion;
import edu.hunau.cxb.pojo.GoodsExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class GoodsSqlProvider {

    public String countByExample(GoodsExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("goods");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String deleteByExample(GoodsExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("goods");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String insertSelective(Goods record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("goods");
        
        if (record.getGoodsid() != null) {
            sql.VALUES("goodsid", "#{goodsid,jdbcType=INTEGER}");
        }
        
        if (record.getGname() != null) {
            sql.VALUES("gname", "#{gname,jdbcType=VARCHAR}");
        }
        
        if (record.getGprice() != null) {
            sql.VALUES("gprice", "#{gprice,jdbcType=INTEGER}");
        }
        
        if (record.getPid() != null) {
            sql.VALUES("pid", "#{pid,jdbcType=INTEGER}");
        }
        
        if (record.getGcount() != null) {
            sql.VALUES("gcount", "#{gcount,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    public String selectByExample(GoodsExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("goodsid");
        } else {
            sql.SELECT("goodsid");
        }
        sql.SELECT("gname");
        sql.SELECT("gprice");
        sql.SELECT("pid");
        sql.SELECT("gcount");
        sql.FROM("goods");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        Goods record = (Goods) parameter.get("record");
        GoodsExample example = (GoodsExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("goods");
        
        if (record.getGoodsid() != null) {
            sql.SET("goodsid = #{record.goodsid,jdbcType=INTEGER}");
        }
        
        if (record.getGname() != null) {
            sql.SET("gname = #{record.gname,jdbcType=VARCHAR}");
        }
        
        if (record.getGprice() != null) {
            sql.SET("gprice = #{record.gprice,jdbcType=INTEGER}");
        }
        
        if (record.getPid() != null) {
            sql.SET("pid = #{record.pid,jdbcType=INTEGER}");
        }
        
        if (record.getGcount() != null) {
            sql.SET("gcount = #{record.gcount,jdbcType=INTEGER}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("goods");
        
        sql.SET("goodsid = #{record.goodsid,jdbcType=INTEGER}");
        sql.SET("gname = #{record.gname,jdbcType=VARCHAR}");
        sql.SET("gprice = #{record.gprice,jdbcType=INTEGER}");
        sql.SET("pid = #{record.pid,jdbcType=INTEGER}");
        sql.SET("gcount = #{record.gcount,jdbcType=INTEGER}");
        
        GoodsExample example = (GoodsExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Goods record) {
        SQL sql = new SQL();
        sql.UPDATE("goods");
        
        if (record.getGname() != null) {
            sql.SET("gname = #{gname,jdbcType=VARCHAR}");
        }
        
        if (record.getGprice() != null) {
            sql.SET("gprice = #{gprice,jdbcType=INTEGER}");
        }
        
        if (record.getPid() != null) {
            sql.SET("pid = #{pid,jdbcType=INTEGER}");
        }
        
        if (record.getGcount() != null) {
            sql.SET("gcount = #{gcount,jdbcType=INTEGER}");
        }
        
        sql.WHERE("goodsid = #{goodsid,jdbcType=INTEGER}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, GoodsExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }
}