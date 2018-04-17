package com.jyss.yqy.service;


import com.jyss.yqy.entity.JBonusResult;
import org.apache.ibatis.annotations.Param;


public interface JBonusZljService {

	JBonusResult getTotalAndZl(@Param("uId") Integer uId);

	JBonusResult getJBonusZlj(@Param("uId") Integer uId, @Param("sId") Integer sId);

	JBonusResult selectJBonusZljByDay(@Param("uId") Integer uId, @Param("sId") Integer sId, @Param("page") int page,
									  @Param("limit") int limit, @Param("beginTime") String beginTime,
									  @Param("endTime") String endTime);

	JBonusResult selectJBonusZljByMonth(@Param("uId") Integer uId, @Param("sId") Integer sId, @Param("page") int page,
										@Param("limit") int limit,@Param("month") String month);
    


}
