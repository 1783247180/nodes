package com.yy.pojo;

import java.util.ArrayList;
import java.util.List;

public class JobsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public JobsExample() {
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

        public Criteria andJobIdIsNull() {
            addCriterion("JOB_ID is null");
            return (Criteria) this;
        }

        public Criteria andJobIdIsNotNull() {
            addCriterion("JOB_ID is not null");
            return (Criteria) this;
        }

        public Criteria andJobIdEqualTo(String value) {
            addCriterion("JOB_ID =", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotEqualTo(String value) {
            addCriterion("JOB_ID <>", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdGreaterThan(String value) {
            addCriterion("JOB_ID >", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdGreaterThanOrEqualTo(String value) {
            addCriterion("JOB_ID >=", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdLessThan(String value) {
            addCriterion("JOB_ID <", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdLessThanOrEqualTo(String value) {
            addCriterion("JOB_ID <=", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdLike(String value) {
            addCriterion("JOB_ID like", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotLike(String value) {
            addCriterion("JOB_ID not like", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdIn(List<String> values) {
            addCriterion("JOB_ID in", values, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotIn(List<String> values) {
            addCriterion("JOB_ID not in", values, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdBetween(String value1, String value2) {
            addCriterion("JOB_ID between", value1, value2, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotBetween(String value1, String value2) {
            addCriterion("JOB_ID not between", value1, value2, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobTitleIsNull() {
            addCriterion("JOB_TITLE is null");
            return (Criteria) this;
        }

        public Criteria andJobTitleIsNotNull() {
            addCriterion("JOB_TITLE is not null");
            return (Criteria) this;
        }

        public Criteria andJobTitleEqualTo(String value) {
            addCriterion("JOB_TITLE =", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleNotEqualTo(String value) {
            addCriterion("JOB_TITLE <>", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleGreaterThan(String value) {
            addCriterion("JOB_TITLE >", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleGreaterThanOrEqualTo(String value) {
            addCriterion("JOB_TITLE >=", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleLessThan(String value) {
            addCriterion("JOB_TITLE <", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleLessThanOrEqualTo(String value) {
            addCriterion("JOB_TITLE <=", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleLike(String value) {
            addCriterion("JOB_TITLE like", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleNotLike(String value) {
            addCriterion("JOB_TITLE not like", value, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleIn(List<String> values) {
            addCriterion("JOB_TITLE in", values, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleNotIn(List<String> values) {
            addCriterion("JOB_TITLE not in", values, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleBetween(String value1, String value2) {
            addCriterion("JOB_TITLE between", value1, value2, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andJobTitleNotBetween(String value1, String value2) {
            addCriterion("JOB_TITLE not between", value1, value2, "jobTitle");
            return (Criteria) this;
        }

        public Criteria andMinSalaryIsNull() {
            addCriterion("MIN_SALARY is null");
            return (Criteria) this;
        }

        public Criteria andMinSalaryIsNotNull() {
            addCriterion("MIN_SALARY is not null");
            return (Criteria) this;
        }

        public Criteria andMinSalaryEqualTo(Integer value) {
            addCriterion("MIN_SALARY =", value, "minSalary");
            return (Criteria) this;
        }

        public Criteria andMinSalaryNotEqualTo(Integer value) {
            addCriterion("MIN_SALARY <>", value, "minSalary");
            return (Criteria) this;
        }

        public Criteria andMinSalaryGreaterThan(Integer value) {
            addCriterion("MIN_SALARY >", value, "minSalary");
            return (Criteria) this;
        }

        public Criteria andMinSalaryGreaterThanOrEqualTo(Integer value) {
            addCriterion("MIN_SALARY >=", value, "minSalary");
            return (Criteria) this;
        }

        public Criteria andMinSalaryLessThan(Integer value) {
            addCriterion("MIN_SALARY <", value, "minSalary");
            return (Criteria) this;
        }

        public Criteria andMinSalaryLessThanOrEqualTo(Integer value) {
            addCriterion("MIN_SALARY <=", value, "minSalary");
            return (Criteria) this;
        }

        public Criteria andMinSalaryIn(List<Integer> values) {
            addCriterion("MIN_SALARY in", values, "minSalary");
            return (Criteria) this;
        }

        public Criteria andMinSalaryNotIn(List<Integer> values) {
            addCriterion("MIN_SALARY not in", values, "minSalary");
            return (Criteria) this;
        }

        public Criteria andMinSalaryBetween(Integer value1, Integer value2) {
            addCriterion("MIN_SALARY between", value1, value2, "minSalary");
            return (Criteria) this;
        }

        public Criteria andMinSalaryNotBetween(Integer value1, Integer value2) {
            addCriterion("MIN_SALARY not between", value1, value2, "minSalary");
            return (Criteria) this;
        }

        public Criteria andMaxSalaryIsNull() {
            addCriterion("MAX_SALARY is null");
            return (Criteria) this;
        }

        public Criteria andMaxSalaryIsNotNull() {
            addCriterion("MAX_SALARY is not null");
            return (Criteria) this;
        }

        public Criteria andMaxSalaryEqualTo(Integer value) {
            addCriterion("MAX_SALARY =", value, "maxSalary");
            return (Criteria) this;
        }

        public Criteria andMaxSalaryNotEqualTo(Integer value) {
            addCriterion("MAX_SALARY <>", value, "maxSalary");
            return (Criteria) this;
        }

        public Criteria andMaxSalaryGreaterThan(Integer value) {
            addCriterion("MAX_SALARY >", value, "maxSalary");
            return (Criteria) this;
        }

        public Criteria andMaxSalaryGreaterThanOrEqualTo(Integer value) {
            addCriterion("MAX_SALARY >=", value, "maxSalary");
            return (Criteria) this;
        }

        public Criteria andMaxSalaryLessThan(Integer value) {
            addCriterion("MAX_SALARY <", value, "maxSalary");
            return (Criteria) this;
        }

        public Criteria andMaxSalaryLessThanOrEqualTo(Integer value) {
            addCriterion("MAX_SALARY <=", value, "maxSalary");
            return (Criteria) this;
        }

        public Criteria andMaxSalaryIn(List<Integer> values) {
            addCriterion("MAX_SALARY in", values, "maxSalary");
            return (Criteria) this;
        }

        public Criteria andMaxSalaryNotIn(List<Integer> values) {
            addCriterion("MAX_SALARY not in", values, "maxSalary");
            return (Criteria) this;
        }

        public Criteria andMaxSalaryBetween(Integer value1, Integer value2) {
            addCriterion("MAX_SALARY between", value1, value2, "maxSalary");
            return (Criteria) this;
        }

        public Criteria andMaxSalaryNotBetween(Integer value1, Integer value2) {
            addCriterion("MAX_SALARY not between", value1, value2, "maxSalary");
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