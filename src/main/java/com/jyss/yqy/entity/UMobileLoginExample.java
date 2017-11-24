package com.jyss.yqy.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UMobileLoginExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UMobileLoginExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUUuidIsNull() {
            addCriterion("u_uuid is null");
            return (Criteria) this;
        }

        public Criteria andUUuidIsNotNull() {
            addCriterion("u_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andUUuidEqualTo(String value) {
            addCriterion("u_uuid =", value, "uUuid");
            return (Criteria) this;
        }

        public Criteria andUUuidNotEqualTo(String value) {
            addCriterion("u_uuid <>", value, "uUuid");
            return (Criteria) this;
        }

        public Criteria andUUuidGreaterThan(String value) {
            addCriterion("u_uuid >", value, "uUuid");
            return (Criteria) this;
        }

        public Criteria andUUuidGreaterThanOrEqualTo(String value) {
            addCriterion("u_uuid >=", value, "uUuid");
            return (Criteria) this;
        }

        public Criteria andUUuidLessThan(String value) {
            addCriterion("u_uuid <", value, "uUuid");
            return (Criteria) this;
        }

        public Criteria andUUuidLessThanOrEqualTo(String value) {
            addCriterion("u_uuid <=", value, "uUuid");
            return (Criteria) this;
        }

        public Criteria andUUuidLike(String value) {
            addCriterion("u_uuid like", value, "uUuid");
            return (Criteria) this;
        }

        public Criteria andUUuidNotLike(String value) {
            addCriterion("u_uuid not like", value, "uUuid");
            return (Criteria) this;
        }

        public Criteria andUUuidIn(List<String> values) {
            addCriterion("u_uuid in", values, "uUuid");
            return (Criteria) this;
        }

        public Criteria andUUuidNotIn(List<String> values) {
            addCriterion("u_uuid not in", values, "uUuid");
            return (Criteria) this;
        }

        public Criteria andUUuidBetween(String value1, String value2) {
            addCriterion("u_uuid between", value1, value2, "uUuid");
            return (Criteria) this;
        }

        public Criteria andUUuidNotBetween(String value1, String value2) {
            addCriterion("u_uuid not between", value1, value2, "uUuid");
            return (Criteria) this;
        }

        public Criteria andSUuidIsNull() {
            addCriterion("s_uuid is null");
            return (Criteria) this;
        }

        public Criteria andSUuidIsNotNull() {
            addCriterion("s_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andSUuidEqualTo(String value) {
            addCriterion("s_uuid =", value, "sUuid");
            return (Criteria) this;
        }

        public Criteria andSUuidNotEqualTo(String value) {
            addCriterion("s_uuid <>", value, "sUuid");
            return (Criteria) this;
        }

        public Criteria andSUuidGreaterThan(String value) {
            addCriterion("s_uuid >", value, "sUuid");
            return (Criteria) this;
        }

        public Criteria andSUuidGreaterThanOrEqualTo(String value) {
            addCriterion("s_uuid >=", value, "sUuid");
            return (Criteria) this;
        }

        public Criteria andSUuidLessThan(String value) {
            addCriterion("s_uuid <", value, "sUuid");
            return (Criteria) this;
        }

        public Criteria andSUuidLessThanOrEqualTo(String value) {
            addCriterion("s_uuid <=", value, "sUuid");
            return (Criteria) this;
        }

        public Criteria andSUuidLike(String value) {
            addCriterion("s_uuid like", value, "sUuid");
            return (Criteria) this;
        }

        public Criteria andSUuidNotLike(String value) {
            addCriterion("s_uuid not like", value, "sUuid");
            return (Criteria) this;
        }

        public Criteria andSUuidIn(List<String> values) {
            addCriterion("s_uuid in", values, "sUuid");
            return (Criteria) this;
        }

        public Criteria andSUuidNotIn(List<String> values) {
            addCriterion("s_uuid not in", values, "sUuid");
            return (Criteria) this;
        }

        public Criteria andSUuidBetween(String value1, String value2) {
            addCriterion("s_uuid between", value1, value2, "sUuid");
            return (Criteria) this;
        }

        public Criteria andSUuidNotBetween(String value1, String value2) {
            addCriterion("s_uuid not between", value1, value2, "sUuid");
            return (Criteria) this;
        }

        public Criteria andTokenIsNull() {
            addCriterion("token is null");
            return (Criteria) this;
        }

        public Criteria andTokenIsNotNull() {
            addCriterion("token is not null");
            return (Criteria) this;
        }

        public Criteria andTokenEqualTo(String value) {
            addCriterion("token =", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotEqualTo(String value) {
            addCriterion("token <>", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenGreaterThan(String value) {
            addCriterion("token >", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenGreaterThanOrEqualTo(String value) {
            addCriterion("token >=", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenLessThan(String value) {
            addCriterion("token <", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenLessThanOrEqualTo(String value) {
            addCriterion("token <=", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenLike(String value) {
            addCriterion("token like", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotLike(String value) {
            addCriterion("token not like", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenIn(List<String> values) {
            addCriterion("token in", values, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotIn(List<String> values) {
            addCriterion("token not in", values, "token");
            return (Criteria) this;
        }

        public Criteria andTokenBetween(String value1, String value2) {
            addCriterion("token between", value1, value2, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotBetween(String value1, String value2) {
            addCriterion("token not between", value1, value2, "token");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNull() {
            addCriterion("created_at is null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNotNull() {
            addCriterion("created_at is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtEqualTo(Date value) {
            addCriterion("created_at =", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotEqualTo(Date value) {
            addCriterion("created_at <>", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThan(Date value) {
            addCriterion("created_at >", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("created_at >=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThan(Date value) {
            addCriterion("created_at <", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThanOrEqualTo(Date value) {
            addCriterion("created_at <=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIn(List<Date> values) {
            addCriterion("created_at in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotIn(List<Date> values) {
            addCriterion("created_at not in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtBetween(Date value1, Date value2) {
            addCriterion("created_at between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotBetween(Date value1, Date value2) {
            addCriterion("created_at not between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andLastAccessTimeIsNull() {
            addCriterion("last_access_time is null");
            return (Criteria) this;
        }

        public Criteria andLastAccessTimeIsNotNull() {
            addCriterion("last_access_time is not null");
            return (Criteria) this;
        }

        public Criteria andLastAccessTimeEqualTo(Integer value) {
            addCriterion("last_access_time =", value, "lastAccessTime");
            return (Criteria) this;
        }

        public Criteria andLastAccessTimeNotEqualTo(Integer value) {
            addCriterion("last_access_time <>", value, "lastAccessTime");
            return (Criteria) this;
        }

        public Criteria andLastAccessTimeGreaterThan(Integer value) {
            addCriterion("last_access_time >", value, "lastAccessTime");
            return (Criteria) this;
        }

        public Criteria andLastAccessTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("last_access_time >=", value, "lastAccessTime");
            return (Criteria) this;
        }

        public Criteria andLastAccessTimeLessThan(Integer value) {
            addCriterion("last_access_time <", value, "lastAccessTime");
            return (Criteria) this;
        }

        public Criteria andLastAccessTimeLessThanOrEqualTo(Integer value) {
            addCriterion("last_access_time <=", value, "lastAccessTime");
            return (Criteria) this;
        }

        public Criteria andLastAccessTimeIn(List<Integer> values) {
            addCriterion("last_access_time in", values, "lastAccessTime");
            return (Criteria) this;
        }

        public Criteria andLastAccessTimeNotIn(List<Integer> values) {
            addCriterion("last_access_time not in", values, "lastAccessTime");
            return (Criteria) this;
        }

        public Criteria andLastAccessTimeBetween(Integer value1, Integer value2) {
            addCriterion("last_access_time between", value1, value2, "lastAccessTime");
            return (Criteria) this;
        }

        public Criteria andLastAccessTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("last_access_time not between", value1, value2, "lastAccessTime");
            return (Criteria) this;
        }

        public Criteria andPushInfoIsNull() {
            addCriterion("push_info is null");
            return (Criteria) this;
        }

        public Criteria andPushInfoIsNotNull() {
            addCriterion("push_info is not null");
            return (Criteria) this;
        }

        public Criteria andPushInfoEqualTo(String value) {
            addCriterion("push_info =", value, "pushInfo");
            return (Criteria) this;
        }

        public Criteria andPushInfoNotEqualTo(String value) {
            addCriterion("push_info <>", value, "pushInfo");
            return (Criteria) this;
        }

        public Criteria andPushInfoGreaterThan(String value) {
            addCriterion("push_info >", value, "pushInfo");
            return (Criteria) this;
        }

        public Criteria andPushInfoGreaterThanOrEqualTo(String value) {
            addCriterion("push_info >=", value, "pushInfo");
            return (Criteria) this;
        }

        public Criteria andPushInfoLessThan(String value) {
            addCriterion("push_info <", value, "pushInfo");
            return (Criteria) this;
        }

        public Criteria andPushInfoLessThanOrEqualTo(String value) {
            addCriterion("push_info <=", value, "pushInfo");
            return (Criteria) this;
        }

        public Criteria andPushInfoLike(String value) {
            addCriterion("push_info like", value, "pushInfo");
            return (Criteria) this;
        }

        public Criteria andPushInfoNotLike(String value) {
            addCriterion("push_info not like", value, "pushInfo");
            return (Criteria) this;
        }

        public Criteria andPushInfoIn(List<String> values) {
            addCriterion("push_info in", values, "pushInfo");
            return (Criteria) this;
        }

        public Criteria andPushInfoNotIn(List<String> values) {
            addCriterion("push_info not in", values, "pushInfo");
            return (Criteria) this;
        }

        public Criteria andPushInfoBetween(String value1, String value2) {
            addCriterion("push_info between", value1, value2, "pushInfo");
            return (Criteria) this;
        }

        public Criteria andPushInfoNotBetween(String value1, String value2) {
            addCriterion("push_info not between", value1, value2, "pushInfo");
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