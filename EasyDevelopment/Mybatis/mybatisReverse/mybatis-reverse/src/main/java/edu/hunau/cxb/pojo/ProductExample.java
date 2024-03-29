package edu.hunau.cxb.pojo;

import java.util.ArrayList;
import java.util.List;

public class ProductExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ProductExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andPidIsNull() {
            addCriterion("pid is null");
            return (Criteria) this;
        }

        public Criteria andPidIsNotNull() {
            addCriterion("pid is not null");
            return (Criteria) this;
        }

        public Criteria andPidEqualTo(Integer value) {
            addCriterion("pid =", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotEqualTo(Integer value) {
            addCriterion("pid <>", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThan(Integer value) {
            addCriterion("pid >", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThanOrEqualTo(Integer value) {
            addCriterion("pid >=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThan(Integer value) {
            addCriterion("pid <", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThanOrEqualTo(Integer value) {
            addCriterion("pid <=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidIn(List<Integer> values) {
            addCriterion("pid in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotIn(List<Integer> values) {
            addCriterion("pid not in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidBetween(Integer value1, Integer value2) {
            addCriterion("pid between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotBetween(Integer value1, Integer value2) {
            addCriterion("pid not between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andPnameIsNull() {
            addCriterion("pname is null");
            return (Criteria) this;
        }

        public Criteria andPnameIsNotNull() {
            addCriterion("pname is not null");
            return (Criteria) this;
        }

        public Criteria andPnameEqualTo(String value) {
            addCriterion("pname =", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameNotEqualTo(String value) {
            addCriterion("pname <>", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameGreaterThan(String value) {
            addCriterion("pname >", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameGreaterThanOrEqualTo(String value) {
            addCriterion("pname >=", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameLessThan(String value) {
            addCriterion("pname <", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameLessThanOrEqualTo(String value) {
            addCriterion("pname <=", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameLike(String value) {
            addCriterion("pname like", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameNotLike(String value) {
            addCriterion("pname not like", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameIn(List<String> values) {
            addCriterion("pname in", values, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameNotIn(List<String> values) {
            addCriterion("pname not in", values, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameBetween(String value1, String value2) {
            addCriterion("pname between", value1, value2, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameNotBetween(String value1, String value2) {
            addCriterion("pname not between", value1, value2, "pname");
            return (Criteria) this;
        }

        public Criteria andPpassword1IsNull() {
            addCriterion("ppassword1 is null");
            return (Criteria) this;
        }

        public Criteria andPpassword1IsNotNull() {
            addCriterion("ppassword1 is not null");
            return (Criteria) this;
        }

        public Criteria andPpassword1EqualTo(String value) {
            addCriterion("ppassword1 =", value, "ppassword1");
            return (Criteria) this;
        }

        public Criteria andPpassword1NotEqualTo(String value) {
            addCriterion("ppassword1 <>", value, "ppassword1");
            return (Criteria) this;
        }

        public Criteria andPpassword1GreaterThan(String value) {
            addCriterion("ppassword1 >", value, "ppassword1");
            return (Criteria) this;
        }

        public Criteria andPpassword1GreaterThanOrEqualTo(String value) {
            addCriterion("ppassword1 >=", value, "ppassword1");
            return (Criteria) this;
        }

        public Criteria andPpassword1LessThan(String value) {
            addCriterion("ppassword1 <", value, "ppassword1");
            return (Criteria) this;
        }

        public Criteria andPpassword1LessThanOrEqualTo(String value) {
            addCriterion("ppassword1 <=", value, "ppassword1");
            return (Criteria) this;
        }

        public Criteria andPpassword1Like(String value) {
            addCriterion("ppassword1 like", value, "ppassword1");
            return (Criteria) this;
        }

        public Criteria andPpassword1NotLike(String value) {
            addCriterion("ppassword1 not like", value, "ppassword1");
            return (Criteria) this;
        }

        public Criteria andPpassword1In(List<String> values) {
            addCriterion("ppassword1 in", values, "ppassword1");
            return (Criteria) this;
        }

        public Criteria andPpassword1NotIn(List<String> values) {
            addCriterion("ppassword1 not in", values, "ppassword1");
            return (Criteria) this;
        }

        public Criteria andPpassword1Between(String value1, String value2) {
            addCriterion("ppassword1 between", value1, value2, "ppassword1");
            return (Criteria) this;
        }

        public Criteria andPpassword1NotBetween(String value1, String value2) {
            addCriterion("ppassword1 not between", value1, value2, "ppassword1");
            return (Criteria) this;
        }

        public Criteria andPpassword2IsNull() {
            addCriterion("ppassword2 is null");
            return (Criteria) this;
        }

        public Criteria andPpassword2IsNotNull() {
            addCriterion("ppassword2 is not null");
            return (Criteria) this;
        }

        public Criteria andPpassword2EqualTo(String value) {
            addCriterion("ppassword2 =", value, "ppassword2");
            return (Criteria) this;
        }

        public Criteria andPpassword2NotEqualTo(String value) {
            addCriterion("ppassword2 <>", value, "ppassword2");
            return (Criteria) this;
        }

        public Criteria andPpassword2GreaterThan(String value) {
            addCriterion("ppassword2 >", value, "ppassword2");
            return (Criteria) this;
        }

        public Criteria andPpassword2GreaterThanOrEqualTo(String value) {
            addCriterion("ppassword2 >=", value, "ppassword2");
            return (Criteria) this;
        }

        public Criteria andPpassword2LessThan(String value) {
            addCriterion("ppassword2 <", value, "ppassword2");
            return (Criteria) this;
        }

        public Criteria andPpassword2LessThanOrEqualTo(String value) {
            addCriterion("ppassword2 <=", value, "ppassword2");
            return (Criteria) this;
        }

        public Criteria andPpassword2Like(String value) {
            addCriterion("ppassword2 like", value, "ppassword2");
            return (Criteria) this;
        }

        public Criteria andPpassword2NotLike(String value) {
            addCriterion("ppassword2 not like", value, "ppassword2");
            return (Criteria) this;
        }

        public Criteria andPpassword2In(List<String> values) {
            addCriterion("ppassword2 in", values, "ppassword2");
            return (Criteria) this;
        }

        public Criteria andPpassword2NotIn(List<String> values) {
            addCriterion("ppassword2 not in", values, "ppassword2");
            return (Criteria) this;
        }

        public Criteria andPpassword2Between(String value1, String value2) {
            addCriterion("ppassword2 between", value1, value2, "ppassword2");
            return (Criteria) this;
        }

        public Criteria andPpassword2NotBetween(String value1, String value2) {
            addCriterion("ppassword2 not between", value1, value2, "ppassword2");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}