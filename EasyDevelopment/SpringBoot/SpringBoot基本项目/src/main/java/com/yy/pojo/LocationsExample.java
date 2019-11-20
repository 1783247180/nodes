package com.yy.pojo;

import java.util.ArrayList;
import java.util.List;

public class LocationsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LocationsExample() {
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

        public Criteria andLocationIdIsNull() {
            addCriterion("LOCATION_ID is null");
            return (Criteria) this;
        }

        public Criteria andLocationIdIsNotNull() {
            addCriterion("LOCATION_ID is not null");
            return (Criteria) this;
        }

        public Criteria andLocationIdEqualTo(Short value) {
            addCriterion("LOCATION_ID =", value, "locationId");
            return (Criteria) this;
        }

        public Criteria andLocationIdNotEqualTo(Short value) {
            addCriterion("LOCATION_ID <>", value, "locationId");
            return (Criteria) this;
        }

        public Criteria andLocationIdGreaterThan(Short value) {
            addCriterion("LOCATION_ID >", value, "locationId");
            return (Criteria) this;
        }

        public Criteria andLocationIdGreaterThanOrEqualTo(Short value) {
            addCriterion("LOCATION_ID >=", value, "locationId");
            return (Criteria) this;
        }

        public Criteria andLocationIdLessThan(Short value) {
            addCriterion("LOCATION_ID <", value, "locationId");
            return (Criteria) this;
        }

        public Criteria andLocationIdLessThanOrEqualTo(Short value) {
            addCriterion("LOCATION_ID <=", value, "locationId");
            return (Criteria) this;
        }

        public Criteria andLocationIdIn(List<Short> values) {
            addCriterion("LOCATION_ID in", values, "locationId");
            return (Criteria) this;
        }

        public Criteria andLocationIdNotIn(List<Short> values) {
            addCriterion("LOCATION_ID not in", values, "locationId");
            return (Criteria) this;
        }

        public Criteria andLocationIdBetween(Short value1, Short value2) {
            addCriterion("LOCATION_ID between", value1, value2, "locationId");
            return (Criteria) this;
        }

        public Criteria andLocationIdNotBetween(Short value1, Short value2) {
            addCriterion("LOCATION_ID not between", value1, value2, "locationId");
            return (Criteria) this;
        }

        public Criteria andStreetAddressIsNull() {
            addCriterion("STREET_ADDRESS is null");
            return (Criteria) this;
        }

        public Criteria andStreetAddressIsNotNull() {
            addCriterion("STREET_ADDRESS is not null");
            return (Criteria) this;
        }

        public Criteria andStreetAddressEqualTo(String value) {
            addCriterion("STREET_ADDRESS =", value, "streetAddress");
            return (Criteria) this;
        }

        public Criteria andStreetAddressNotEqualTo(String value) {
            addCriterion("STREET_ADDRESS <>", value, "streetAddress");
            return (Criteria) this;
        }

        public Criteria andStreetAddressGreaterThan(String value) {
            addCriterion("STREET_ADDRESS >", value, "streetAddress");
            return (Criteria) this;
        }

        public Criteria andStreetAddressGreaterThanOrEqualTo(String value) {
            addCriterion("STREET_ADDRESS >=", value, "streetAddress");
            return (Criteria) this;
        }

        public Criteria andStreetAddressLessThan(String value) {
            addCriterion("STREET_ADDRESS <", value, "streetAddress");
            return (Criteria) this;
        }

        public Criteria andStreetAddressLessThanOrEqualTo(String value) {
            addCriterion("STREET_ADDRESS <=", value, "streetAddress");
            return (Criteria) this;
        }

        public Criteria andStreetAddressLike(String value) {
            addCriterion("STREET_ADDRESS like", value, "streetAddress");
            return (Criteria) this;
        }

        public Criteria andStreetAddressNotLike(String value) {
            addCriterion("STREET_ADDRESS not like", value, "streetAddress");
            return (Criteria) this;
        }

        public Criteria andStreetAddressIn(List<String> values) {
            addCriterion("STREET_ADDRESS in", values, "streetAddress");
            return (Criteria) this;
        }

        public Criteria andStreetAddressNotIn(List<String> values) {
            addCriterion("STREET_ADDRESS not in", values, "streetAddress");
            return (Criteria) this;
        }

        public Criteria andStreetAddressBetween(String value1, String value2) {
            addCriterion("STREET_ADDRESS between", value1, value2, "streetAddress");
            return (Criteria) this;
        }

        public Criteria andStreetAddressNotBetween(String value1, String value2) {
            addCriterion("STREET_ADDRESS not between", value1, value2, "streetAddress");
            return (Criteria) this;
        }

        public Criteria andPostalCodeIsNull() {
            addCriterion("POSTAL_CODE is null");
            return (Criteria) this;
        }

        public Criteria andPostalCodeIsNotNull() {
            addCriterion("POSTAL_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andPostalCodeEqualTo(String value) {
            addCriterion("POSTAL_CODE =", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeNotEqualTo(String value) {
            addCriterion("POSTAL_CODE <>", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeGreaterThan(String value) {
            addCriterion("POSTAL_CODE >", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeGreaterThanOrEqualTo(String value) {
            addCriterion("POSTAL_CODE >=", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeLessThan(String value) {
            addCriterion("POSTAL_CODE <", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeLessThanOrEqualTo(String value) {
            addCriterion("POSTAL_CODE <=", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeLike(String value) {
            addCriterion("POSTAL_CODE like", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeNotLike(String value) {
            addCriterion("POSTAL_CODE not like", value, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeIn(List<String> values) {
            addCriterion("POSTAL_CODE in", values, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeNotIn(List<String> values) {
            addCriterion("POSTAL_CODE not in", values, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeBetween(String value1, String value2) {
            addCriterion("POSTAL_CODE between", value1, value2, "postalCode");
            return (Criteria) this;
        }

        public Criteria andPostalCodeNotBetween(String value1, String value2) {
            addCriterion("POSTAL_CODE not between", value1, value2, "postalCode");
            return (Criteria) this;
        }

        public Criteria andCityIsNull() {
            addCriterion("CITY is null");
            return (Criteria) this;
        }

        public Criteria andCityIsNotNull() {
            addCriterion("CITY is not null");
            return (Criteria) this;
        }

        public Criteria andCityEqualTo(String value) {
            addCriterion("CITY =", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotEqualTo(String value) {
            addCriterion("CITY <>", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThan(String value) {
            addCriterion("CITY >", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThanOrEqualTo(String value) {
            addCriterion("CITY >=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThan(String value) {
            addCriterion("CITY <", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThanOrEqualTo(String value) {
            addCriterion("CITY <=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLike(String value) {
            addCriterion("CITY like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotLike(String value) {
            addCriterion("CITY not like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityIn(List<String> values) {
            addCriterion("CITY in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotIn(List<String> values) {
            addCriterion("CITY not in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityBetween(String value1, String value2) {
            addCriterion("CITY between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotBetween(String value1, String value2) {
            addCriterion("CITY not between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andStateProvinceIsNull() {
            addCriterion("STATE_PROVINCE is null");
            return (Criteria) this;
        }

        public Criteria andStateProvinceIsNotNull() {
            addCriterion("STATE_PROVINCE is not null");
            return (Criteria) this;
        }

        public Criteria andStateProvinceEqualTo(String value) {
            addCriterion("STATE_PROVINCE =", value, "stateProvince");
            return (Criteria) this;
        }

        public Criteria andStateProvinceNotEqualTo(String value) {
            addCriterion("STATE_PROVINCE <>", value, "stateProvince");
            return (Criteria) this;
        }

        public Criteria andStateProvinceGreaterThan(String value) {
            addCriterion("STATE_PROVINCE >", value, "stateProvince");
            return (Criteria) this;
        }

        public Criteria andStateProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("STATE_PROVINCE >=", value, "stateProvince");
            return (Criteria) this;
        }

        public Criteria andStateProvinceLessThan(String value) {
            addCriterion("STATE_PROVINCE <", value, "stateProvince");
            return (Criteria) this;
        }

        public Criteria andStateProvinceLessThanOrEqualTo(String value) {
            addCriterion("STATE_PROVINCE <=", value, "stateProvince");
            return (Criteria) this;
        }

        public Criteria andStateProvinceLike(String value) {
            addCriterion("STATE_PROVINCE like", value, "stateProvince");
            return (Criteria) this;
        }

        public Criteria andStateProvinceNotLike(String value) {
            addCriterion("STATE_PROVINCE not like", value, "stateProvince");
            return (Criteria) this;
        }

        public Criteria andStateProvinceIn(List<String> values) {
            addCriterion("STATE_PROVINCE in", values, "stateProvince");
            return (Criteria) this;
        }

        public Criteria andStateProvinceNotIn(List<String> values) {
            addCriterion("STATE_PROVINCE not in", values, "stateProvince");
            return (Criteria) this;
        }

        public Criteria andStateProvinceBetween(String value1, String value2) {
            addCriterion("STATE_PROVINCE between", value1, value2, "stateProvince");
            return (Criteria) this;
        }

        public Criteria andStateProvinceNotBetween(String value1, String value2) {
            addCriterion("STATE_PROVINCE not between", value1, value2, "stateProvince");
            return (Criteria) this;
        }

        public Criteria andCountryIdIsNull() {
            addCriterion("COUNTRY_ID is null");
            return (Criteria) this;
        }

        public Criteria andCountryIdIsNotNull() {
            addCriterion("COUNTRY_ID is not null");
            return (Criteria) this;
        }

        public Criteria andCountryIdEqualTo(String value) {
            addCriterion("COUNTRY_ID =", value, "countryId");
            return (Criteria) this;
        }

        public Criteria andCountryIdNotEqualTo(String value) {
            addCriterion("COUNTRY_ID <>", value, "countryId");
            return (Criteria) this;
        }

        public Criteria andCountryIdGreaterThan(String value) {
            addCriterion("COUNTRY_ID >", value, "countryId");
            return (Criteria) this;
        }

        public Criteria andCountryIdGreaterThanOrEqualTo(String value) {
            addCriterion("COUNTRY_ID >=", value, "countryId");
            return (Criteria) this;
        }

        public Criteria andCountryIdLessThan(String value) {
            addCriterion("COUNTRY_ID <", value, "countryId");
            return (Criteria) this;
        }

        public Criteria andCountryIdLessThanOrEqualTo(String value) {
            addCriterion("COUNTRY_ID <=", value, "countryId");
            return (Criteria) this;
        }

        public Criteria andCountryIdLike(String value) {
            addCriterion("COUNTRY_ID like", value, "countryId");
            return (Criteria) this;
        }

        public Criteria andCountryIdNotLike(String value) {
            addCriterion("COUNTRY_ID not like", value, "countryId");
            return (Criteria) this;
        }

        public Criteria andCountryIdIn(List<String> values) {
            addCriterion("COUNTRY_ID in", values, "countryId");
            return (Criteria) this;
        }

        public Criteria andCountryIdNotIn(List<String> values) {
            addCriterion("COUNTRY_ID not in", values, "countryId");
            return (Criteria) this;
        }

        public Criteria andCountryIdBetween(String value1, String value2) {
            addCriterion("COUNTRY_ID between", value1, value2, "countryId");
            return (Criteria) this;
        }

        public Criteria andCountryIdNotBetween(String value1, String value2) {
            addCriterion("COUNTRY_ID not between", value1, value2, "countryId");
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