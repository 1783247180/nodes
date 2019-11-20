package edu.hunau.cxb.pojo;

import java.util.ArrayList;
import java.util.List;

public class UserExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserExample() {
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

        public Criteria andUseridIsNull() {
            addCriterion("userid is null");
            return (Criteria) this;
        }

        public Criteria andUseridIsNotNull() {
            addCriterion("userid is not null");
            return (Criteria) this;
        }

        public Criteria andUseridEqualTo(Integer value) {
            addCriterion("userid =", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotEqualTo(Integer value) {
            addCriterion("userid <>", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThan(Integer value) {
            addCriterion("userid >", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThanOrEqualTo(Integer value) {
            addCriterion("userid >=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThan(Integer value) {
            addCriterion("userid <", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThanOrEqualTo(Integer value) {
            addCriterion("userid <=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridIn(List<Integer> values) {
            addCriterion("userid in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotIn(List<Integer> values) {
            addCriterion("userid not in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridBetween(Integer value1, Integer value2) {
            addCriterion("userid between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotBetween(Integer value1, Integer value2) {
            addCriterion("userid not between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNull() {
            addCriterion("username is null");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNotNull() {
            addCriterion("username is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameEqualTo(String value) {
            addCriterion("username =", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotEqualTo(String value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(String value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(String value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(String value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLike(String value) {
            addCriterion("username like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotLike(String value) {
            addCriterion("username not like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<String> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<String> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(String value1, String value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(String value1, String value2) {
            addCriterion("username not between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUserpassword1IsNull() {
            addCriterion("userpassword1 is null");
            return (Criteria) this;
        }

        public Criteria andUserpassword1IsNotNull() {
            addCriterion("userpassword1 is not null");
            return (Criteria) this;
        }

        public Criteria andUserpassword1EqualTo(String value) {
            addCriterion("userpassword1 =", value, "userpassword1");
            return (Criteria) this;
        }

        public Criteria andUserpassword1NotEqualTo(String value) {
            addCriterion("userpassword1 <>", value, "userpassword1");
            return (Criteria) this;
        }

        public Criteria andUserpassword1GreaterThan(String value) {
            addCriterion("userpassword1 >", value, "userpassword1");
            return (Criteria) this;
        }

        public Criteria andUserpassword1GreaterThanOrEqualTo(String value) {
            addCriterion("userpassword1 >=", value, "userpassword1");
            return (Criteria) this;
        }

        public Criteria andUserpassword1LessThan(String value) {
            addCriterion("userpassword1 <", value, "userpassword1");
            return (Criteria) this;
        }

        public Criteria andUserpassword1LessThanOrEqualTo(String value) {
            addCriterion("userpassword1 <=", value, "userpassword1");
            return (Criteria) this;
        }

        public Criteria andUserpassword1Like(String value) {
            addCriterion("userpassword1 like", value, "userpassword1");
            return (Criteria) this;
        }

        public Criteria andUserpassword1NotLike(String value) {
            addCriterion("userpassword1 not like", value, "userpassword1");
            return (Criteria) this;
        }

        public Criteria andUserpassword1In(List<String> values) {
            addCriterion("userpassword1 in", values, "userpassword1");
            return (Criteria) this;
        }

        public Criteria andUserpassword1NotIn(List<String> values) {
            addCriterion("userpassword1 not in", values, "userpassword1");
            return (Criteria) this;
        }

        public Criteria andUserpassword1Between(String value1, String value2) {
            addCriterion("userpassword1 between", value1, value2, "userpassword1");
            return (Criteria) this;
        }

        public Criteria andUserpassword1NotBetween(String value1, String value2) {
            addCriterion("userpassword1 not between", value1, value2, "userpassword1");
            return (Criteria) this;
        }

        public Criteria andUsermoneyIsNull() {
            addCriterion("usermoney is null");
            return (Criteria) this;
        }

        public Criteria andUsermoneyIsNotNull() {
            addCriterion("usermoney is not null");
            return (Criteria) this;
        }

        public Criteria andUsermoneyEqualTo(Integer value) {
            addCriterion("usermoney =", value, "usermoney");
            return (Criteria) this;
        }

        public Criteria andUsermoneyNotEqualTo(Integer value) {
            addCriterion("usermoney <>", value, "usermoney");
            return (Criteria) this;
        }

        public Criteria andUsermoneyGreaterThan(Integer value) {
            addCriterion("usermoney >", value, "usermoney");
            return (Criteria) this;
        }

        public Criteria andUsermoneyGreaterThanOrEqualTo(Integer value) {
            addCriterion("usermoney >=", value, "usermoney");
            return (Criteria) this;
        }

        public Criteria andUsermoneyLessThan(Integer value) {
            addCriterion("usermoney <", value, "usermoney");
            return (Criteria) this;
        }

        public Criteria andUsermoneyLessThanOrEqualTo(Integer value) {
            addCriterion("usermoney <=", value, "usermoney");
            return (Criteria) this;
        }

        public Criteria andUsermoneyIn(List<Integer> values) {
            addCriterion("usermoney in", values, "usermoney");
            return (Criteria) this;
        }

        public Criteria andUsermoneyNotIn(List<Integer> values) {
            addCriterion("usermoney not in", values, "usermoney");
            return (Criteria) this;
        }

        public Criteria andUsermoneyBetween(Integer value1, Integer value2) {
            addCriterion("usermoney between", value1, value2, "usermoney");
            return (Criteria) this;
        }

        public Criteria andUsermoneyNotBetween(Integer value1, Integer value2) {
            addCriterion("usermoney not between", value1, value2, "usermoney");
            return (Criteria) this;
        }

        public Criteria andUserpassword2IsNull() {
            addCriterion("userpassword2 is null");
            return (Criteria) this;
        }

        public Criteria andUserpassword2IsNotNull() {
            addCriterion("userpassword2 is not null");
            return (Criteria) this;
        }

        public Criteria andUserpassword2EqualTo(String value) {
            addCriterion("userpassword2 =", value, "userpassword2");
            return (Criteria) this;
        }

        public Criteria andUserpassword2NotEqualTo(String value) {
            addCriterion("userpassword2 <>", value, "userpassword2");
            return (Criteria) this;
        }

        public Criteria andUserpassword2GreaterThan(String value) {
            addCriterion("userpassword2 >", value, "userpassword2");
            return (Criteria) this;
        }

        public Criteria andUserpassword2GreaterThanOrEqualTo(String value) {
            addCriterion("userpassword2 >=", value, "userpassword2");
            return (Criteria) this;
        }

        public Criteria andUserpassword2LessThan(String value) {
            addCriterion("userpassword2 <", value, "userpassword2");
            return (Criteria) this;
        }

        public Criteria andUserpassword2LessThanOrEqualTo(String value) {
            addCriterion("userpassword2 <=", value, "userpassword2");
            return (Criteria) this;
        }

        public Criteria andUserpassword2Like(String value) {
            addCriterion("userpassword2 like", value, "userpassword2");
            return (Criteria) this;
        }

        public Criteria andUserpassword2NotLike(String value) {
            addCriterion("userpassword2 not like", value, "userpassword2");
            return (Criteria) this;
        }

        public Criteria andUserpassword2In(List<String> values) {
            addCriterion("userpassword2 in", values, "userpassword2");
            return (Criteria) this;
        }

        public Criteria andUserpassword2NotIn(List<String> values) {
            addCriterion("userpassword2 not in", values, "userpassword2");
            return (Criteria) this;
        }

        public Criteria andUserpassword2Between(String value1, String value2) {
            addCriterion("userpassword2 between", value1, value2, "userpassword2");
            return (Criteria) this;
        }

        public Criteria andUserpassword2NotBetween(String value1, String value2) {
            addCriterion("userpassword2 not between", value1, value2, "userpassword2");
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